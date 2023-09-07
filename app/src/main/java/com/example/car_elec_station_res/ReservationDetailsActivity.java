package com.example.car_elec_station_res;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReservationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        TextView reservationDetailsTextView = findViewById(R.id.reservationDetailsTextView);
        Button saveButton = findViewById(R.id.saveButton);

        // Récupérer les détails de réservation depuis l'intent
        String reservationDetails = getIntent().getStringExtra("reservationDetails");

        // Afficher les détails de réservation dans le TextView
        reservationDetailsTextView.setText(reservationDetails);

        // Ajouter un écouteur de clic pour le bouton "Save"
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enregistrer le contenu dans un fichier texte
                saveReservationToFile(reservationDetails);
            }
        });
    }

    private void saveReservationToFile(String reservationDetails) {
        // Code pour enregistrer le contenu dans un fichier texte
        // Utilisez les classes Java standard pour la manipulation de fichiers
        // Par exemple, FileWriter, BufferedWriter, etc.
        // Assurez-vous d'obtenir les permissions nécessaires pour écrire dans le stockage externe si nécessaire
    }
}
