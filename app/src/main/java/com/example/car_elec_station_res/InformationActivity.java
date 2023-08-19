package com.example.car_elec_station_res;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // Activer la flèche de retour
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    // Gérer le clic sur la flèche de retour
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Retour à l'activité précédente
        return true;
    }
}