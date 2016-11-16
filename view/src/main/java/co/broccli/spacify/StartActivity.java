package co.broccli.spacify;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import co.broccli.logic.SpacifyApi;
import co.broccli.spacify.Nearby.NearbyFragment;
import co.broccli.spacify.Profile.ProfileFragment;
import co.broccli.spacify.Utils.NetworkStateReceiver;


public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkStateReceiver.NetworkStateReceiverListener {

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

        networkStateReceiver = new NetworkStateReceiver();
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        assert bottomBar != null;

        fm.beginTransaction().add(R.id.fragment_container, feedFragment, "fragment_feed").commit();

        active = feedFragment;

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_feed) {
                    if(active != feedFragment)
                        fm.beginTransaction().hide(active).show(feedFragment).commit();
                    active = feedFragment;
                }

                if (tabId == R.id.tab_nearby) {
                    if (fm.findFragmentByTag("fragment_nearby") == null) {
                        fm.beginTransaction().hide(active).add(R.id.fragment_container, nearbyFragment, "fragment_nearby").commit();
                    } else {
                        fm.beginTransaction().hide(active).show(nearbyFragment).commit();
                    }
                    active = nearbyFragment;
                }

                if (tabId == R.id.tab_user) {
                    if (fm.findFragmentByTag("fragment_user") == null) {
                        fm.beginTransaction().hide(active).add(R.id.fragment_container, profileFragment, "fragment_user").commit();
                    } else {
                        fm.beginTransaction().hide(active).show(profileFragment).commit();
                    }
                    active = profileFragment;
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                fm.beginTransaction().detach(active).attach(active).commit();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkStateReceiver.removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkStateReceiver.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
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
