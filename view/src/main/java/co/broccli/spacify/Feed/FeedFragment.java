package co.broccli.spacify.Feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import co.broccli.spacify.R;
import co.broccli.spacify.Space.SpacePostsFeedItem;


public class FeedFragment extends Fragment {

    public FeedFragment() {
        // Required empty public constructor
    }


    public static FeedFragment newInstance() {
        return new FeedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView mRecyclerView  = (RecyclerView) view.findViewById(R.id.feed_posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        FastItemAdapter fastAdapter = new FastItemAdapter();
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);
        fastAdapter.add(
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem(),
                new FeedPostsItem()
        );

        return view;
    }

}
