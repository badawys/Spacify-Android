package co.broccli.spacify.Profile.JoinedSpaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import co.broccli.spacify.Profile.ProfileTabFragmentBase;
import co.broccli.spacify.R;
import co.broccli.spacify.Space.SpaceActivity;


public class JoinedSpacesFragmant extends ProfileTabFragmentBase {

    ObservableRecyclerView mRecyclerView ;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joined_spaces, container, false);

        mRecyclerView  = (ObservableRecyclerView) view.findViewById(android.R.id.list);
        mRecyclerView .setScrollViewCallbacks(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        FastItemAdapter fastAdapter = new FastItemAdapter();
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);
        fastAdapter.add(
                new JoinedSpacesItem().withName("Space1 Name"),
                new JoinedSpacesItem().withName("Space2 Name"),
                new JoinedSpacesItem().withName("Space3 Name"),
                new JoinedSpacesItem().withName("Space4 Name"),
                new JoinedSpacesItem().withName("Space5 Name"),
                new JoinedSpacesItem().withName("Space6 Name"),
                new JoinedSpacesItem().withName("Space7 Name"),
                new JoinedSpacesItem().withName("Space8 Name"),
                new JoinedSpacesItem().withName("Space9 Name"),
                new JoinedSpacesItem().withName("Space10 Name"),
                new JoinedSpacesItem().withName("Space11 Name"),
                new JoinedSpacesItem().withName("Space12 Name"),
                new JoinedSpacesItem().withName("Space13 Name"));
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
                Intent spaceIntent = new Intent(getActivity(), SpaceActivity.class);
                getActivity().startActivity(spaceIntent);
                return true;
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setLinearLayoutManager(linearLayoutManager);
        this.setProfileHeader((LinearLayout) getActivity().findViewById(R.id.profile_header));
        this.setProfileTabLayout((LinearLayout) getActivity().findViewById(R.id.profile_tab_layout));

    }
}
