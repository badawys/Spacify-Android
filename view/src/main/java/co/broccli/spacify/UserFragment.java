package co.broccli.spacify;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import co.broccli.logic.SessionManager;
import co.broccli.logic.model.protectedPath.protectedPath;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
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

        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.content_loading);
        textMessage = (TextView) view.findViewById(R.id.textMessage);
        logoutFAB = (FloatingActionButton)  view.findViewById(R.id.logoutFAB);

        logoutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager = new SessionManager(getContext());
                sessionManager.logoutUser(LoginActivity.class);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, getContext());

        Call<protectedPath> call = apiService.getUser();
        call.enqueue(new Callback<protectedPath>() {
            @Override
            public void onResponse(Call<protectedPath> call, Response<protectedPath> response) {

                progressBar.setVisibility(View.GONE);
                textMessage.setText(response.body().getUser().getName());
            }

            @Override
            public void onFailure(Call<protectedPath> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
