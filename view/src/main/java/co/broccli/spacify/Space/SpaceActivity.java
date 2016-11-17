package co.broccli.spacify.Space;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import co.broccli.spacify.R;

public class SpaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView mRecyclerView  = (RecyclerView) findViewById(R.id.space_posts_feed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FastItemAdapter fastAdapter = new FastItemAdapter();
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);
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
}
