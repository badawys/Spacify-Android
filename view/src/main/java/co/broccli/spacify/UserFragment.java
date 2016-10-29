package co.broccli.spacify;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;

import co.broccli.logic.SessionManager;
import co.broccli.logic.model.protectedPath.protectedPath;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {


    private SessionManager sessionManager;

    ContentLoadingProgressBar progressBar;
    TextView textMessage;
    FloatingActionButton logoutFAB;

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

        Uri uri = Uri.parse("https://gitlab.com/uploads/user/avatar/56386/tt_avatar_small.jpg");
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.user_pic_view);
        draweeView.setImageURI(uri);

//        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.content_loading);
//        textMessage = (TextView) view.findViewById(R.id.textMessage);
//        logoutFAB = (FloatingActionButton)  view.findViewById(R.id.logoutFAB);

//        logoutFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sessionManager = new SessionManager(getContext());
//
//                final ProgressDialog progressDialog = new ProgressDialog(getContext(),
//                        R.style.AppTheme_Dark_Dialog);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Signing out...");
//                progressDialog.show();
//
//                // Logout user from the server (Revoke Access Token)
//                ApiInterface apiService =
//                        ApiClient.createService(ApiInterface.class, getContext());
//                Call<ResponseBody> call = apiService.logout();
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        // Do nothing
//                    }
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        // Do nothing
//                    }
//                });
//
//                progressDialog.dismiss();
//
//                // Logout user from app
//                sessionManager.logoutUser(LoginActivity.class);
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ApiInterface apiService =
//                ApiClient.createService(ApiInterface.class, getContext());
//
//        Call<protectedPath> call = apiService.getUser();
//        call.enqueue(new Callback<protectedPath>() {
//            @Override
//            public void onResponse(Call<protectedPath> call, Response<protectedPath> response) {
//
//                progressBar.setVisibility(View.GONE);
//                textMessage.setText(response.body().getUser().getName());
//            }
//
//            @Override
//            public void onFailure(Call<protectedPath> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }
}
