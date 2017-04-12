package co.broccli.spacify.Nearby;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.victor.loading.rotate.RotateLoading;

import co.broccli.logic.Callback;
import co.broccli.logic.SpacifyApi;
import co.broccli.logic.model.space.NearbySpaces;
import co.broccli.logic.model.space.SpaceData;
import co.broccli.spacify.R;
import co.broccli.spacify.Space.CreateSpaceActivity;
import co.broccli.spacify.Space.SpaceActivity;
import co.broccli.spacify.StartActivity;
import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import mehdi.sakout.fancybuttons.FancyButton;

public class NearbyFragment extends Fragment implements
        OnMapReadyCallback,
        OnLocationUpdatedListener,
        ObservableScrollViewCallbacks {

    SmartLocation smartLocation;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_ID = 1001;
    private Circle mapCircle;
    private CircleOptions circleOptions;
    private FrameLayout mapLayout;
    private LinearLayout nearbyListLayout;
    private RotateLoading rotateLoading;
    private ObservableRecyclerView mRecyclerView ;
    private LinearLayout.LayoutParams defaultNearbyListLayoutParams;
    private boolean mapLayoutIsExtended = false;
    private LinearLayoutManager linearLayoutManager;
    private FancyButton addButton;
    private FancyButton refreshButton;

    FastItemAdapter fastAdapter = new FastItemAdapter();

    public NearbyFragment() {
        // Required empty public constructor
    }

    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateloading);
        mapLayout = (FrameLayout) view.findViewById(R.id.mapLayout);
        nearbyListLayout = (LinearLayout) view.findViewById(R.id.nearby_list_layout);

        defaultNearbyListLayoutParams = (LinearLayout.LayoutParams) nearbyListLayout.getLayoutParams();
        mRecyclerView  = (ObservableRecyclerView) view.findViewById(R.id.nearbyList);
        mRecyclerView.setScrollViewCallbacks(this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        addButton = (FancyButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateSpaceActivity.class));
            }
        });

        refreshButton = (FancyButton) view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationByActivity();
            }
        });

        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Location permission not granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL).liteMode(true).mapToolbarEnabled(false);

        mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        smartLocation = new SmartLocation.Builder(getContext()).logging(true).build();

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView .setAdapter(fastAdapter);
        mRecyclerView.setItemAnimator(null);

        fastAdapter.withItemEvent(new ClickEventHook<NearbyListItem>() {
              @Nullable
              @Override
              public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
                  //return the views on which you want to bind this event
                  if (viewHolder instanceof NearbyListItem.ViewHolder) {
                      return ((NearbyListItem.ViewHolder) viewHolder).view;
                  }
                  return null;
              }

              @Override
              public void onClick(View v, int position, FastAdapter<NearbyListItem> fastAdapter, NearbyListItem item) {
                  Intent spaceIntent = new Intent(getActivity(), SpaceActivity.class);
                  spaceIntent.putExtra("EXTRA_SPACE_ID", item.spaceData.getId());
                  getActivity().startActivity(spaceIntent);
              }
          });
    }


    private void startLocation(LocationParams locationParams) {
        LocationGooglePlayServicesProvider provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);
        SmartLocation.with(getContext()).location().stop();
        smartLocation.location(provider).config(locationParams).start(this);
    }

    private void stopLocation () {
        smartLocation.location().stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationByActivity();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

        circleOptions = new CircleOptions()
                .radius(500)
                .fillColor(0x551976D2)
                .strokeColor(0x551976D2)
                .strokeWidth(0);

        rotateLoading.start();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                rotateLoading.stop();
            }
        });

        Location lastLocation = SmartLocation.with(getContext()).location().getLastLocation();

        if (lastLocation != null) {
            onLocationUpdated(lastLocation);
        }
    }

    private void startLocationByActivity () {
        SmartLocation.with(getContext()).activity().start(new OnActivityUpdatedListener() {
            @Override
            public void onActivityUpdated(DetectedActivity detectedActivity) {
                if (detectedActivity != null) {
                    switch (detectedActivity.getType()) {
                        case DetectedActivity.IN_VEHICLE:
                            startLocation(LocationParams.NAVIGATION);
                            break;
                        case DetectedActivity.ON_BICYCLE:
                            startLocation(LocationParams.NAVIGATION);
                            break;
                        case DetectedActivity.ON_FOOT:
                            startLocation(LocationParams.BEST_EFFORT);
                            break;
                        case DetectedActivity.STILL:
                            startLocation(LocationParams.LAZY);
                            break;
                        case DetectedActivity.TILTING:
                            startLocation(LocationParams.LAZY);
                            break;
                        default:
                            startLocation(LocationParams.NAVIGATION);
                    }
                }
            }
        });
    }

    @Override
    public void onLocationUpdated(Location location) {
        if (mMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14.5f);
            if (mapCircle != null)
                mapCircle.remove();
            mMap.moveCamera(cameraUpdate);
            mapCircle = mMap.addCircle(circleOptions.center(latLng));
        }

        SpacifyApi.space().getNearby(getContext(),
                location.getLongitude(),
                location.getLatitude(),
                new Callback<NearbySpaces>() {
            @Override
            public void onResult(NearbySpaces nearbySpaces) {
                fastAdapter.clear();
                for (SpaceData space : nearbySpaces.getData()) {
                    fastAdapter.add(new NearbyListItem().withSpaceData(space));
                }

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (nearbyListLayout.getTranslationY() == 0) {
                moveProfileHeader(-mapLayout.getHeight());
                if (!mapLayoutIsExtended) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nearbyListLayout.getLayoutParams();
                    lp.height = getContainerHeight();
                    nearbyListLayout.setLayoutParams(lp);
                    nearbyListLayout.requestLayout();
                    mapLayoutIsExtended = true;
                }
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                moveProfileHeader(0);
                if (mapLayoutIsExtended) {
                    nearbyListLayout.setLayoutParams(defaultNearbyListLayoutParams);
                    nearbyListLayout.requestLayout();
                    mapLayoutIsExtended = false;
                }
            }
        }
    }

    private void moveProfileHeader(float toTranslationY) {
        // Check the current translationY
        if (nearbyListLayout.getTranslationY() == toTranslationY) {
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(nearbyListLayout.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                nearbyListLayout.setTranslationY(translationY);
            }
        });
        animator.start();
    }

    private int getContainerHeight() {
        return (getActivity().findViewById(R.id.fragment_container)).getHeight();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationByActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocation();
    }
}
