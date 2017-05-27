package co.broccli.spacify.Space;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.github.clans.fab.FloatingActionButton;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.space.CreateSpace;
import co.broccli.logic.model.space.GetSpace;
import co.broccli.spacify.Auth.SignupActivity;
import co.broccli.logic.model.space.Post;
import co.broccli.logic.model.space.SpacePosts;
import co.broccli.spacify.R;

public class SpaceActivity extends AppCompatActivity{

    private FastItemAdapter fastAdapter = new FastItemAdapter();
    private CollapsingToolbarLayout collapsingToolbar;
    private SimpleDraweeView _SpacePhoto;
    private SimpleDraweeView _SpaceCover;
    private int SpaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        Toolbar toolbar = (Toolbar) findViewById(R.id.spaceToolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        _SpacePhoto = (SimpleDraweeView) findViewById(R.id.space_photo);
        _SpaceCover = (SimpleDraweeView) findViewById(R.id.cover_image);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RecyclerView mRecyclerView  = (RecyclerView) findViewById(R.id.space_posts_feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);

        SpaceId = getIntent().getIntExtra("EXTRA_SPACE_ID", 0);

        SpacifyApi.space().getSpace(this, SpaceId, new Callback<GetSpace>() {
            @Override
            public void onResult(GetSpace getSpace) {
                showData(getSpace);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(SpaceActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        final FloatingActionButton postText = (FloatingActionButton) findViewById(R.id.post_text_fab);
        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                intent.putExtra("EXTRA_SPACE_ID", SpaceId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_space, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void showData (GetSpace data) {
        collapsingToolbar.setTitle(data.getSpace().getName());
        setSpacePhoto(data.getSpace().getPhoto());
    }

    private void setSpacePhoto (String url) {

        Postprocessor blurPostprocessor = new BasePostprocessor() {

            private static final int BITMAP_SCALE_DOWN = 4;
            private static final int BLUR_RADIUS = 25;

            @Override
            public String getName() {
                return "blurPostprocessor";
            }

            public CacheKey getPostprocessorCacheKey() {
                return new SimpleCacheKey("profile_header_background");
            }

            @Override
            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {

                int width = Math.round(sourceBitmap.getWidth() / BITMAP_SCALE_DOWN);
                int height = Math.round(sourceBitmap.getHeight() / BITMAP_SCALE_DOWN);

                CloseableReference<Bitmap> sourceBitmapRef = bitmapFactory.createScaledBitmap(sourceBitmap, width, height, false);
                CloseableReference<Bitmap> distBitmapRef = bitmapFactory.createBitmap(sourceBitmapRef.get());
                try {
                    Bitmap inputBitmap  = sourceBitmapRef.get();
                    Bitmap outputBitmap = distBitmapRef.get();

                    RenderScript rs = RenderScript.create(SpaceActivity.this);
                    ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                    Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
                    Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
                    theIntrinsic.setRadius(BLUR_RADIUS);
                    theIntrinsic.setInput(tmpIn);
                    theIntrinsic.forEach(tmpOut);
                    tmpOut.copyTo(outputBitmap);

                    return CloseableReference.cloneOrNull(distBitmapRef);
                } finally {
                    CloseableReference.closeSafely(distBitmapRef);
                }
            }
        };

        Uri uri = Uri.parse("https://spacify.s3-accelerate.amazonaws.com/"+url);

        ImageRequest profilePhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();
        ImageRequest backgroundPhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setPostprocessor(blurPostprocessor)
                .build();

        PipelineDraweeController photoController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(profilePhotoRequest)
                        .setOldController(_SpacePhoto.getController())
                        .build();
        PipelineDraweeController backgroundController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(backgroundPhotoRequest)
                        .setOldController(_SpaceCover.getController())
                        .build();

        _SpacePhoto.setController(photoController);
        _SpaceCover.setController(backgroundController);
    }

    private void getPostsData (final int SpaceId) {
        SpacifyApi.space().getSpacePosts(this,
                SpaceId,
                new Callback<SpacePosts>() {
                    @Override
                    public void onResult(SpacePosts spacePosts) {
                        if (spacePosts.getData() != null) {
                            fastAdapter.clear();
                            for (Post post : spacePosts.getData()) {
                                fastAdapter.add(new SpacePostsFeedItem().withPostData(post));
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(SpaceActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostsData(SpaceId);
    }
}
