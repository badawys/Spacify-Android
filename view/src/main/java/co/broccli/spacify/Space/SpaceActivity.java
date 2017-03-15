package co.broccli.spacify.Space;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.space.CreateSpace;
import co.broccli.logic.model.space.GetSpace;
import co.broccli.spacify.R;

public class SpaceActivity extends AppCompatActivity{

    private FastItemAdapter fastAdapter = new FastItemAdapter();
    private CollapsingToolbarLayout collapsingToolbar;
    private SimpleDraweeView _SpacePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        Toolbar toolbar = (Toolbar) findViewById(R.id.spaceToolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        _SpacePhoto = (SimpleDraweeView) findViewById(R.id.space_photo);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RecyclerView mRecyclerView  = (RecyclerView) findViewById(R.id.space_posts_feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);

        int SpaceId = getIntent().getIntExtra("EXTRA_SPACE_ID", 0);

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

        fastAdapter.add(
                new SpacePostsFeedItem(),
                new SpacePostsFeedItem(),
                new SpacePostsFeedItem(),
                new SpacePostsFeedItem(),
                new SpacePostsFeedItem(),
                new SpacePostsFeedItem()
        );
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

        Uri uri = Uri.parse("https://spacify.s3-accelerate.amazonaws.com/"+url);

        ImageRequest profilePhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();

        PipelineDraweeController photoController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(profilePhotoRequest)
                        .setOldController(_SpacePhoto.getController())
                        .build();

        _SpacePhoto.setController(photoController);
    }
}
