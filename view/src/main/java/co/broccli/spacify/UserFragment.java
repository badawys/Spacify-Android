package co.broccli.spacify;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import co.broccli.logic.SessionManager;
import co.broccli.logic.model.protectedPath.protectedPath;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import co.broccli.spacify.Profile.JoinedSpacesFragmant;
import co.broccli.spacify.Profile.MySpacesFragment;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private TextView userName;
    private SimpleDraweeView draweeView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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

        draweeView = (SimpleDraweeView) view.findViewById(R.id.user_pic_view);
        userName = (TextView) view.findViewById(R.id.userName);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.tab_pager);
        FancyButton logoutButton = (FancyButton) view.findViewById(R.id.logoutButton);

        getUsername ();
        getProfilephoto();
        getProfileTabs();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogout();
            }
        });

        return view;
    }


    /**
     * ToDo: Add Description
     */
    private void getProfilephoto () {
        Uri uri = Uri.parse("https://gitlab.com/uploads/user/avatar/56386/tt_avatar_small.jpg");
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        hierarchy.setProgressBarImage(new ProgressBarDrawable());
        draweeView.setImageURI(uri);
    }

    /**
     * ToDo: Add Description
     */
    private void getUsername () {
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
    private void getProfileTabs () {
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

        SessionManager sessionManager;

        sessionManager = new SessionManager(getContext());

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing out...");
        progressDialog.show();

        // Logout user from the server (Revoke Access Token)
        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, getContext());
        Call<ResponseBody> call = apiService.logout();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Do nothing
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Do nothing
            }
        });

        progressDialog.dismiss();

        // Logout user from app
        sessionManager.logoutUser(LoginActivity.class);
    }
}
