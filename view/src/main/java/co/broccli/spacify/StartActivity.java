package co.broccli.spacify;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import co.broccli.logic.SpacifyApi;
import co.broccli.spacify.Feed.FeedFragment;
import co.broccli.spacify.Nearby.NearbyFragment;
import co.broccli.spacify.Notification.NotificationItem;
import co.broccli.spacify.Profile.ProfileFragment;
import co.broccli.spacify.Search.SearchActivity;
import co.broccli.spacify.Utils.NetworkStateReceiver;


public class StartActivity extends AppCompatActivity
        implements NetworkStateReceiver.NetworkStateReceiverListener {

    private NetworkStateReceiver networkStateReceiver;

    private LinearLayout no_internet_layout;

    final Fragment feedFragment = FeedFragment.newInstance();
    final Fragment nearbyFragment = NearbyFragment.newInstance();
    final Fragment profileFragment = ProfileFragment.newInstance();
    Fragment active = feedFragment;
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SpacifyApi.auth().needToBeLoggedIn(this);

        no_internet_layout = (LinearLayout) findViewById(R.id.no_internet_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_notification);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottombar_feed, R.drawable.ic_bottombar_feed, R.color.primary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottombar_nearby, R.drawable.ic_bottombar_nearby, R.color.primary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottombar_user, R.drawable.ic_bottombar_user, R.color.primary);
        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#03A9F4"));
        bottomNavigation.setInactiveColor(Color.parseColor("#b2b2b2"));
        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);
        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

        fm.beginTransaction().add(R.id.fragment_container, nearbyFragment, "fragment_nearby").hide(nearbyFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, profileFragment, "fragment_user").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, feedFragment, "fragment_feed").commit();

        active = feedFragment;

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean  onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    if (active == feedFragment) {
                        fm.beginTransaction().detach(active).attach(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(feedFragment).commit();
                        active = feedFragment;
                    }
                }

                if (position == 1) {
                    if (active == nearbyFragment) {
                        fm.beginTransaction().detach(active).attach(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(nearbyFragment).commit();
                        active = nearbyFragment;
                    }
                }

                if (position == 2) {
                    if (active == profileFragment) {
                        fm.beginTransaction().detach(active).attach(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(profileFragment).commit();
                        active = profileFragment;
                    }
                }
                return true;
            }
        });

        RecyclerView mRecyclerView  = (RecyclerView) findViewById(R.id.notifications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        FastItemAdapter fastAdapter = new FastItemAdapter();
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);
        fastAdapter.add(
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem(),
                new NotificationItem()
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkStateReceiver = new NetworkStateReceiver();
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        networkStateReceiver.addListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent spaceIntent = new Intent(this, SearchActivity.class);
            this.startActivity(spaceIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void networkAvailable() {
        no_internet_layout.setVisibility(View.GONE);
    }

    @Override
    public void networkUnavailable() {
        no_internet_layout.setVisibility(View.VISIBLE);
    }
}
