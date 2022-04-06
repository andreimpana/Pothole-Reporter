package com.example.potholeapp;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button markerButton;
    private List<List<Double>> markers = new ArrayList<List<Double>>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference potholesRef = db.collection("Potholes");
    private final String colourKey = "colour";
    final private MarkerOptions[] clickedMarker = {null};


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().setTitle(R.string.MapFragmentName);
        markerButton = root.findViewById(R.id.add_Marker_button);
        markerButton.setVisibility(View.INVISIBLE);
        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For dropping a marker at a point on the Map
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                final double[] uLat = {43.47377124458098};
                final double[] uLong = {-80.52733598598329};

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    uLat[0] = location.getLatitude();
                                    uLong[0] = location.getLongitude();
                                    return;
                                }
                            }
                        });

                LatLng uPos = new LatLng(uLat[0], uLong[0]);
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(uPos).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //add markers to map
                loadMarkers(googleMap);


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Toast toast = Toast.makeText(mMapView.getContext(), "Location Selected", Toast.LENGTH_SHORT);
                        toast.show();
                        markerButton.setVisibility(View.VISIBLE);

                        if (clickedMarker[0] == null) {
                            clickedMarker[0] = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("new Marker").icon(BitmapDescriptorFactory.defaultMarker(getColour()));
                            googleMap.addMarker(clickedMarker[0]);
                        } else {
                            googleMap.clear();
                            loadMarkers(googleMap);
                            clickedMarker[0] = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("new Marker").icon(BitmapDescriptorFactory.defaultMarker(getColour()));
                            googleMap.addMarker(clickedMarker[0]);
                        }
                    }
                });

                markerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetDialog(clickedMarker[0].getPosition().latitude,clickedMarker[0].getPosition().longitude);
                    }
                });
            }
        });

        mMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }
    private float getColour()
    {
        String fileName = getString(R.string.user_preference);
        SharedPreferences userPreference = getActivity().getSharedPreferences(fileName, MODE_PRIVATE);
        String colour = userPreference.getString(colourKey,"Blue");
        switch (colour){
            case "Red":
                return BitmapDescriptorFactory.HUE_RED;
            case "Azure":
                return BitmapDescriptorFactory.HUE_AZURE;
            case "Cyan":
                return BitmapDescriptorFactory.HUE_CYAN;
            case "Green":
                return BitmapDescriptorFactory.HUE_GREEN;
            case "Magenta":
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case "Orange":
                return BitmapDescriptorFactory.HUE_ORANGE;
            case "Rose":
                return BitmapDescriptorFactory.HUE_ROSE;
            case "Violet":
                return BitmapDescriptorFactory.HUE_VIOLET;
            case "Yellow":
                return BitmapDescriptorFactory.HUE_YELLOW;
        }
        return BitmapDescriptorFactory.HUE_BLUE;
    }
    private void showBottomSheetDialog(double lat, double lon){
        BottomSheetDialog markSlider = new BottomSheetDialog(getActivity());
        markSlider.setContentView(R.layout.bottom_sheet_marker);

        Button slideConfirm = markSlider.findViewById(R.id.sliderButton);
        EditText slideTitle = markSlider.findViewById(R.id.titleInput);
        EditText slideDesc = markSlider.findViewById(R.id.descInput);

        markSlider.show();

        slideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMarker(slideTitle.getText().toString(),slideDesc.getText().toString(),lat,lon);

                //TODO: Make this a custom dialog box that is cancelable
                Toast toast = Toast.makeText(mMapView.getContext(), "Marker Added", Toast.LENGTH_SHORT);
                toast.show();
                clickedMarker[0].icon(null);
                markSlider.hide();
            }
        });
    }

    private void loadMarkers(GoogleMap googleMap){
                potholesRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            MapPoint mapPoint = documentSnapshot.toObject(MapPoint.class);
                            mapPoint.setMapPointId(documentSnapshot.getId());

                            String potholeId = mapPoint.getMapPointId();
                            String title = mapPoint.getTitle();
                            String description = mapPoint.getDescription();
                            double lat = mapPoint.getLat();
                            double lon = mapPoint.getLon();
                            Log.d("TITle:", title);
                            Log.d("lat:", String.valueOf(lat));
                            Log.d("lon:", String.valueOf(lon));
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title).icon(BitmapDescriptorFactory.defaultMarker(getColour())));
                        }
                    }
                });
    }
    private void addMarker(String title, String description, double lat, double lon){
        MapPoint mapPoint = new MapPoint(title, description,lat,lon);
        potholesRef.add(mapPoint).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mapPoint.setMapPointId(documentReference.getId());
                Log.d("MapFragment:", "Marker added to firebase");
            }
        });
    }
}