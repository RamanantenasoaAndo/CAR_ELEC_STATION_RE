package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.car_elec_station_res.Model.Borne;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Toolbar toolbar;
    ExtendedFloatingActionButton floatingActionButton;

    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    LatLng stPierre = new LatLng(-20.219098, 57.528456);
    LatLng roseHill = new LatLng(-20.266776, 57.462898);
    LatLng rosebelle = new LatLng(-20.403906, 57.594789);
    LatLng tamarain = new LatLng(-20.361361, 57.374841);
    LatLng arsenal = new LatLng(-20.128342, 57.530298);

    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> types = new ArrayList<String>();
    ArrayList<Integer> idBorne = new ArrayList<Integer>();
    ArrayList<Integer> borneIds = new ArrayList<Integer>();
    ArrayList<String> modesRecharge = new ArrayList<String>();

    private GoogleMap mMap;
    private Marker mMarker;
    private LatLng mMarkerPosition = new LatLng(-20.0, 47.0);





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();

        // Initialisation de l'AlarmManager
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        // Création de l'intent pour le BroadcastReceiver
        Intent intent = new Intent(requireContext(), RefreshReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Configuration de l'alarme pour se déclencher toutes les 5 minutes
        long intervalMillis = 3 * 60 * 1000; // 5 minutes en millisecondes
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                intervalMillis,
                pendingIntent
        );

        floatingActionButton = (ExtendedFloatingActionButton) view.findViewById(R.id.state_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheet = getLayoutInflater().inflate(R.layout.activity_state, null);

                LinearLayout linearLayout = bottomSheet.findViewById(R.id.borneListLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                int marginBottom = getResources().getDimensionPixelSize(R.dimen.list_item_margin_bottom); // Récupère la valeur de la marge depuis les dimensions
                params.setMargins(0, 0, 0, marginBottom);
                // Récupérer les données de Firebase BORNE

// Récupérer la liste des clés primaires de votre nœud "Reservation"
                DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation");
                reservationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Créer la liste des clés primaires des réservations
                            List<String> reservations = new ArrayList<>();

                            // Parcourir les enfants du nœud "Reservation" pour récupérer les clés primaires
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String reservationKey = childSnapshot.getKey();
                                reservations.add(reservationKey);
                            }

                            // Récupérer les données de Firebase BORNE
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("bornes");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Parcourir les données de Firebase BORNE
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            // Récupérer les données de chaque clé primaire
                                            int idB = dataSnapshot.child("idBorne").getValue(Integer.class);
                                            String titre = dataSnapshot.child("titre").getValue(String.class);
                                            String typesPrise = dataSnapshot.child("typesPrise").getValue(String.class);

                                            // Vérifier si le numéro de borne est présent dans la liste des réservations
                                            boolean estOccupe = reservations.contains(String.valueOf(idB));

                                            // Créer une vue pour afficher les données et l'état de la réservation
                                            View itemView = getLayoutInflater().inflate(R.layout.item_borne, null);
                                            TextView textViewIdBorne = itemView.findViewById(R.id.textViewIdBorne);
                                            TextView textViewTitre = itemView.findViewById(R.id.textViewTitre);
                                            TextView textViewTypesPrise = itemView.findViewById(R.id.textViewTypesPrise);
                                            TextView textViewEtat = itemView.findViewById(R.id.textViewEtat);

                                            // Afficher les données dans les vues
                                            textViewIdBorne.setText("B: " + idB);
                                            textViewTitre.setText(titre);
                                            textViewTypesPrise.setText(typesPrise);

                                            // Afficher l'état de la réservation
                                            if (estOccupe) {
                                                // La borne est occupée
                                                textViewEtat.setText("OCCUPE");
                                                textViewEtat.setTextColor(Color.RED);
                                            } else {
                                                // La borne est disponible
                                                textViewEtat.setText("DISPONIBLE");
                                                textViewEtat.setTextColor(Color.GREEN);
                                            }

                                            // Ajouter la vue à la liste des bornes
                                            linearLayout.addView(itemView,params);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Gérer l'annulation de la lecture de la base de données
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Gérer les erreurs de récupération des données
                    }
                });

                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.charging);
        int largeur = 60;
        int hauteur = 80;
        Bitmap bitmapRedimensionne = Bitmap.createScaledBitmap(bitmap, largeur, hauteur, false);
        BitmapDescriptor icone = BitmapDescriptorFactory.fromBitmap(bitmapRedimensionne);

        // Obtenir une référence à la base de données Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("bornes");

        for (int i = 0; i < arrayList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(arrayList.get(i))
                    .title(title.get(i))
                    .snippet(types.get(i))
                    .icon(icone);

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));

            // Créer un nouvel objet Borne avec les informations
            Borne borne = new Borne();
            borne.setIdBorne(idBorne.get(i));
            borne.setTypesPrise(types.get(i));
            borne.setTitre(title.get(i));
            borne.setPosition(arrayList.get(i));
            // Utiliser la méthode push() pour générer une nouvelle clé unique dans Firebase
            DatabaseReference newBorneRef = reference.child(String.valueOf(idBorne.get(i)));
            // Stocker les informations de la borne dans Firebase
            newBorneRef.setValue(borne);

            borneIds.add(idBorne.get(i)); // Ajouter l'ID de la borne correspondante à la liste borneIds
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                String markerTitle = marker.getTitle();
                String typePrise = marker.getSnippet();
                int index = Integer.parseInt(marker.getId().substring(1));
                int idBorne = borneIds.get(index);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("title", markerTitle);
                intent.putExtra("types", typePrise);
                intent.putExtra("idBorne", String.valueOf(idBorne));

                startActivity(intent);
            }
        });

        if (!arrayList.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : arrayList) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            int padding = 100;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(cameraUpdate);
                }
            });
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList.add(stPierre);
        arrayList.add(roseHill);
        arrayList.add(rosebelle);
        arrayList.add(tamarain);
        arrayList.add(arsenal);

        title.add("UDM STATION St pierre");
        title.add("UDM STATION Camp Levieux");
        title.add("UDM STATION Paille");
        title.add("UDM STATION Rose-Hill");
        title.add("UDM STATION Moka");

        types.add("Types 2 ,CCS/SAE");
        types.add("Prise Combo ");
        types.add("Types 2 ,G");
        types.add("CCS/SAE, CHAdeMO");
        types.add("Types 2 ,UDM");

        idBorne.add(11);
        idBorne.add(12);
        idBorne.add(13);
        idBorne.add(14);
        idBorne.add(15);

        modesRecharge.add("Normal 2-5kw");
        modesRecharge.add("Accelerated 16-30kw");
        modesRecharge.add("Fast 30-350kw");
        modesRecharge.add("Fast 30-350kw");
        modesRecharge.add("Normal 2-5kw");

        // Obtenir une référence à la base de données Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("bornes");

        for (int i = 0; i < arrayList.size(); i++) {
            // Créer un nouvel objet Borne avec les informations
            Borne borne = new Borne();
            borne.setIdBorne(idBorne.get(i));
            borne.setTypesPrise(types.get(i));
            borne.setTitre(title.get(i));
            borne.setPosition(arrayList.get(i));

            // Utiliser la méthode push() pour générer une nouvelle clé unique dans Firebase
            DatabaseReference newBorneRef = reference.child(String.valueOf(idBorne.get(i)));




            // Stocker les informations de la borne dans Firebase
            newBorneRef.setValue(borne);

            borneIds.add(idBorne.get(i)); // Ajouter l'ID de la borne correspondante à la liste borneIds
        }

        // Obtention de la référence au fragment de carte
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
