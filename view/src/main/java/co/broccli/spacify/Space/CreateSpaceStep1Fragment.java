package co.broccli.spacify.Space;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.victor.loading.rotate.RotateLoading;

import co.broccli.spacify.R;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;


public class CreateSpaceStep1Fragment extends Fragment implements
        OnMapReadyCallback,
        OnLocationUpdatedListener,
        ISlidePolicy {

    SmartLocation smartLocation;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_ID = 1001;
    private Circle mapCircle;
    private CircleOptions circleOptions;
    private RotateLoading rotateLoading;

    protected LatLng currentLocation;

    public CreateSpaceStep1Fragment() {
        // Required empty public constructor
    }


    public static CreateSpaceStep1Fragment newInstance() {
        CreateSpaceStep1Fragment fragment = new CreateSpaceStep1Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_space_step1, container, false);
        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateloading);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Location permission not granted
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_ID);
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
    }

    private void startLocation() {

        LocationGooglePlayServicesProvider provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);
        smartLocation = new SmartLocation.Builder(getContext()).logging(true).build();
        smartLocation.location(provider).config(LocationParams.NAVIGATION).start(this);
    }

    private void stopLocation () {
        smartLocation.location().stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
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

    @Override
    public void onLocationUpdated(Location location) {

        if (mMap != null) {
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//            createSpaceActivity.setLocation(currentLocation);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 14.5f);
            if (mapCircle != null)
                mapCircle.remove();
            mMap.animateCamera(cameraUpdate);
            mapCircle = mMap.addCircle(circleOptions.center(currentLocation));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocation();
    }

    @Override
    public boolean isPolicyRespected() {
        return currentLocation != null;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        if (getView() != null)
            Snackbar.make(getView(),
                    R.string.create_space_could_not_find_your_location_error,
                    Snackbar.LENGTH_SHORT).show();
    }
}
