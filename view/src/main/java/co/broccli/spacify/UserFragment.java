package co.broccli.spacify;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import co.broccli.logic.SessionManager;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.protectedPath.protectedPath;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import co.broccli.spacify.Helper.FrascoRepeatedPostProcessor;
import co.broccli.spacify.Profile.JoinedSpaces.JoinedSpacesFragmant;
import co.broccli.spacify.Profile.MySpaces.MySpacesFragment;
import jp.wasabeef.fresco.processors.BlurPostprocessor;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private TextView userName;
    private SimpleDraweeView profilePhoto;
    private SimpleDraweeView headerBackground;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FancyButton logoutButton;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_user, container, false);

        profilePhoto = (SimpleDraweeView) view.findViewById(R.id.user_pic_view);
        headerBackground = (SimpleDraweeView) view.findViewById(R.id.header_background);
        userName = (TextView) view.findViewById(R.id.userName);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.tab_pager);
        logoutButton = (FancyButton) view.findViewById(R.id.logoutButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUsername ();
        setProfilephoto ();
        setHeaderBackground();
        setProfileTabs();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
            }
        });
    }

    /**
     * ToDo: Add Description
     */
    private void setHeaderBackground () {

        Uri uri = Uri.parse("https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/12314120_10206938908184467_3626450146143059050_n.jpg?oh=b434b06225d532e669fee0e7a69a9601&oe=5887EA3C");

        FrascoRepeatedPostProcessor repeatedPostProcessor = new FrascoRepeatedPostProcessor();
        ImageRequest headerBackgroundRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(repeatedPostProcessor)
                .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(headerBackgroundRequest)
                        .setOldController(headerBackground.getController())
                        .build();

        headerBackground.setController(controller);
        repeatedPostProcessor.apply(new BlurPostprocessor (getContext(), 150));
        repeatedPostProcessor.apply(new ColorFilterPostprocessor(Color.argb(80, 0, 0, 0)));

    }

    /**
     * ToDo: Add Description
     */
    private void setProfilephoto () {

        Uri uri = Uri.parse("https://scontent-cai1-1.xx.fbcdn.net/v/t1.0-9/12314120_10206938908184467_3626450146143059050_n.jpg?oh=b434b06225d532e669fee0e7a69a9601&oe=5887EA3C");

        ImageRequest profilePhotoRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .build();

        PipelineDraweeController controller =
                (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(profilePhotoRequest)
                        .setOldController(profilePhoto.getController())
                        .build();
        profilePhoto.setController(controller);
    }

    /**
     * ToDo: Add Description
     */
    private void setUsername () {
        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, getContext());
        Call<protectedPath> call = apiService.getUser();
        call.enqueue(new Callback<protectedPath>() {
            @Override
            public void onResponse(Call<protectedPath> call, Response<protectedPath> response) {

                userName.setText(response.body().getUser().getName());
            }

            @Override
            public void onFailure(Call<protectedPath> call, Throwable t) {

            }
        });
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
    private void onClickLogout () {
        SpacifyApi.auth().logout(getContext());
    }

}