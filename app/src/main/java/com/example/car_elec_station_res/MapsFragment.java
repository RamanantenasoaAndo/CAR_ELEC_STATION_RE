package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class MapsFragment extends Fragment {
    Toolbar toolbar;
    ExtendedFloatingActionButton floatingActionButton;

    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng sydney= new LatLng(-34,151);
    LatLng tamworth =new LatLng(-31.083332,150.916672);
    LatLng Newcastle =new LatLng(-32.916668,151.750000);
    LatLng Brisbane =new LatLng(-27.470125,153.021072);
    LatLng Dubbo =new LatLng(-32.256943,148.601105);

    //create another arraylist for names for markers
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> types = new ArrayList<String>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady( GoogleMap googleMap) {

            for (int i=0;i<arrayList.size();i++) {
                for (int j = 0; j < title.size(); j++) {
                    for (int k = 0; k < types.size(); k++) {

                        MarkerOptions markerOptions = new MarkerOptions();

                        //PROBLEME A RESOUDRE:ICONE
//                   googleMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title(String.valueOf(title.get(j))));
//                    markerOptions.draggable(true);
//                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_station));


                        //Ajouter des position
                        markerOptions.position(arrayList.get(i));
                        //Ajouter des titre
                        markerOptions.title(String.valueOf(title.get(i)))
                                .position(arrayList.get(i))
                                .draggable(true)
                                .alpha(5)
                                .snippet(String.valueOf(types.get(i)))
                                .zIndex(1.0f);

                        googleMap.addMarker(markerOptions);
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));

                }

            }
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    String markertitle = marker.getTitle();
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    intent.putExtra("title",markertitle);
                    startActivity(intent);


                }
            });

            //add on click listner for marker on map
//            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(@NonNull Marker marker) {
//                    String markertitle = marker.getTitle();
//                    Intent intent = new Intent(getContext(),MainActivity.class);
//                    intent.putExtra("title",markertitle);
//                    startActivity(intent);
//                    return false;
//                }
//            });

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_maps, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        floatingActionButton = (ExtendedFloatingActionButton) view.findViewById(R.id.state_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                View bottomSheet = getLayoutInflater().inflate(R.layout.state_layout,(LinearLayout)view.findViewById(R.id.btnState));
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
            }
        });

        return  view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        arrayList.add(sydney);
        arrayList.add(tamworth);
        arrayList.add(Newcastle);
        arrayList.add(Dubbo);
        arrayList.add(Brisbane);

        //Nom et Lieux de Station de recharge
        title.add("UDM STATION St PIERRE");
        title.add("UDM STATION CAMP LEVIEUX");
        title.add("UDM STATION PAILLE");
        title.add("UDM STATION ROSE-HILL");
        title.add("UDM STATION MOKA");

        //Types de charger dans le station
        types.add("Types 2 ,CCS/SAE");
        types.add("Types 2 ");
        types.add("Types 2 ,G");
        types.add("CCS/SAE, CHAdeMO");
        types.add("Types 2 ,UDM");
    }


}