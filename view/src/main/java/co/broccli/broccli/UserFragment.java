package co.broccli.broccli;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

public class UserFragment extends Fragment {


    ContentLoadingProgressBar progressBar;
    UltimateRecyclerView recyclerView;

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

//        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.content_loading);
//        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.joing_spaces_recycler_view);
//
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//
//        Call<JoindResponse> call = apiService.getJoindSpaces();
//        call.enqueue(new Callback<JoindResponse>() {
//            @Override
//            public void onResponse(Call<JoindResponse> call, Response<JoindResponse> response) {
//
//                List<Joind> joindSpaces = response.body().getData();
//
//                JoindAdapter adapter = new JoindAdapter(joindSpaces);
//                recyclerView.setAdapter(adapter);
//
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<JoindResponse> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }
}
