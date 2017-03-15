package co.broccli.spacify.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.profile.User;
import co.broccli.spacify.Profile.JoinedSpaces.JoinedSpacesFragmant;
import co.broccli.spacify.Profile.MySpaces.MySpacesFragment;
import co.broccli.spacify.R;
import co.broccli.spacify.Settings.SettingsActivity;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private TextView userName;
    private SimpleDraweeView profilePhoto;
    private SimpleDraweeView headerBackground;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FancyButton settingsButton;
    private FancyButton editButton;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePhoto = (SimpleDraweeView) view.findViewById(R.id.user_pic_view);
        headerBackground = (SimpleDraweeView) view.findViewById(R.id.header_background);
        userName = (TextView) view.findViewById(R.id.userName);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.tab_pager);
        settingsButton = (FancyButton) view.findViewById(R.id.settingsButton);
        editButton = (FancyButton) view.findViewById(R.id.editButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setProfileTabs ();
        getProfileData ();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSettings();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEdit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1)
            getProfileData();
    }

    /**
     * ToDo: Add Description
     */
    private void getProfileData () {
        SpacifyApi.profile().getUserProfile(getContext(), new Callback<User>() {
            @Override
            public void onResult(User user) {
                setUsername(user.getName());
                setProfilePhoto (user.getPhoto());
                if (firebaseUser != null ){
                    if (firebaseUser.getEmail() != user.getEmail())
                        firebaseUser.updateEmail(user.getEmail());
                    if (firebaseUser.getDisplayName() != user.getName() ||
                            firebaseUser.getPhotoUrl() != Uri.parse("https://spacify.s3-accelerate.amazonaws.com/" + user.getPhoto())) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(user.getName())
                                .setPhotoUri(Uri.parse("https://spacify.s3-accelerate.amazonaws.com/" + user.getPhoto()))
                                .build();
                        firebaseUser.updateProfile(profileUpdates);
                    }

                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    /**
     * ToDo: Add Description
     */
    private void setProfilePhoto (String url) {

        Uri uri = Uri.parse("https://spacify.s3-accelerate.amazonaws.com/" + url);

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

                    RenderScript rs = RenderScript.create(getContext());
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
                        .setOldController(profilePhoto.getController())
                        .build();
        PipelineDraweeController backgroundController =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(backgroundPhotoRequest)
                        .setOldController(headerBackground.getController())
                        .build();

        profilePhoto.setController(photoController);
        headerBackground.setController(backgroundController);
    }

    /**
     * ToDo: Add Description
     */
    private void setUsername (String name) {
        userName.setText(name);
    }


    /**
     * ToDo: Add Description
     */
    private void setProfileTabs () {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add(R.string.joined_spaces, JoinedSpacesFragmant.class)
                .add(R.string.my_spaces, MySpacesFragment.class)
                .create());
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.joined_spaces), 0);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.my_spaces), 1);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /**
     * ToDo: Add Description
     */
    private void onClickEdit() {
        FragmentManager fm = getFragmentManager();
        SupportBlurDialogFragment editProfile = new EditProfileDialog();
        editProfile.setTargetFragment(this, 0);
        editProfile.show(fm, "fragment_edit_profile");
    }

    /**
     * ToDo: Add Description
     */
    private void onClickSettings() {
        Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
        getActivity().startActivity(settingsIntent);
    }

}