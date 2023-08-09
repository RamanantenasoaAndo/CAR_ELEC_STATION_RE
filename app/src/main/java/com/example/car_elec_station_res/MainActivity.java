package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    CollapsingToolbarLayout markertxt;
    Button btn_reservation, btn_other;
    TextView markertypes, markertypesB, markeridBorn,markerDispo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_reservation = findViewById(R.id.btn_reserver);

        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Formulaire_reservation.class);
                startActivity(intent);
                finish();
            }
        });

        btn_other = findViewById(R.id.btn_other);
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // BottomSheetDialog code...
            }
        });

        markertxt = findViewById(R.id.collapsingToolbar);
        String title = getIntent().getStringExtra("title");
        markertxt.setTitle(title);

        // for Subtitle // for typesBranchement
        markertypes = findViewById(R.id.subtitle);
        markertypesB = findViewById(R.id.typesB);
        markeridBorn = findViewById(R.id.idStation);
        markerDispo = findViewById(R.id.textViewDisponibiliter);

        Toolbar toolbar = findViewById(R.id.toolbarLayout);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque le bouton retour est cliqué
                onBackPressed();
            }
        });

        btn_reservation = findViewById(R.id.btn_reserver);
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                String idB = markeridBorn.getText().toString().replace("Id Borne: ", ""); // Récupérer l'idBorne sans le préfixe
                intent.putExtra("idbo", idB); // Passer l'idBorne à l'activité de réservation
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Récupérer les données de l'intent
        String types = getIntent().getStringExtra("types");
        ImageView imageViewPrise = findViewById(R.id.imageViewPrise); // Supposons que vous ayez une ImageView pour afficher la photo de la prise

        if (types.equals("Types 2 ,CCS/SAE")) {
            imageViewPrise.setImageResource(R.drawable.combo_cs);
        } else if (types.equals("Prise Combo")) {
            imageViewPrise.setImageResource(R.drawable.combo_cs);
        } else if (types.equals("Types 2 ,G")) {
            imageViewPrise.setImageResource(R.drawable.type2);
        } else if (types.equals("CCS/SAE, CHAdeMO")) {
            imageViewPrise.setImageResource(R.drawable.chademo);
        } else if (types.equals("Types 2 ,UDM")) {
            imageViewPrise.setImageResource(R.drawable.type2);
        } else {
            imageViewPrise.setImageResource(R.drawable.df); // Une photo par défaut si le type de prise n'est pas reconnu
        }

        String idB = getIntent().getStringExtra("idBorne");
        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservation").child(idB);
        reservationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Le primary key correspond à l'id de borne
                    markerDispo.setText("Occupé");
                    markerDispo.setTextColor(Color.RED);
                } else {
                    // Le primary key ne correspond pas à l'id de borne
                    markerDispo.setText("Disponible");
                    markerDispo.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs de récupération des données
            }
        });


        // Mettre à jour les TextView avec les données récupérées
        markertypes.setText(types);
        markertypesB.setText(types);
        markeridBorn.setText("Id Borne: " + idB);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reser, menu);
        return true;
    }
}
