package com.roshan.traversity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Roshan on 9/14/2017.
 */

public class RouteMapFragment extends Fragment implements OnMapReadyCallback {


    public static final String TAG = "routeMapsFragment";
    GoogleMap mgoogleMap;
    MapView mapView;
    View mView;
    LocationListener locationListener;
    LocationManager locationManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.route_maps_fragment,container,false);
        return mView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){

            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                }
            }

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView= (MapView) mView.findViewById(R.id.map);

        if(mapView!=null){

            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mgoogleMap=googleMap;
        mgoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng everest = new LatLng(27.9878493, 86.9162713);

        mgoogleMap.addMarker(new MarkerOptions().position(everest).title("Liberty").snippet("Lets go"));
//        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(everest,10));


        //get current lat lng
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng currLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mgoogleMap.addMarker(new MarkerOptions().position(currLocation).title("Your current location"));
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation,12));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT < 23){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            LatLng currLocation = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            mgoogleMap.addMarker(new MarkerOptions().position(currLocation).title("Your current location"));
            mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation,12));

        }else{

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            }

        }




    }
}
