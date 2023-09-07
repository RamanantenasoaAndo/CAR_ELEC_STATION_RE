package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PaymentActivity extends AppCompatActivity {
    private Dialog dialog;
    private DatabaseReference databaseReference;
    private PaymentActivity.ReservationConfirmedListener reservationConfirmedListener;
    public interface ReservationConfirmedListener {
        void onReservationConfirmed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://car-elec-station-res-default-rtdb.firebaseio.com/");
        // Récupérer les informations de l'Intent
        Intent intent = getIntent();
        String idBor = intent.getStringExtra("idBor");
        String phoneU = intent.getStringExtra("phoneU");
        String matrVeh = intent.getStringExtra("matrVeh");
        String priseT = intent.getStringExtra("priseT");
        String datRe = intent.getStringExtra("datRe");
        String heurD = intent.getStringExtra("heurD");
        String heurF = intent.getStringExtra("heurF");


        dialog = new Dialog(PaymentActivity.this);
        dialog.setContentView(R.layout.dialogue_confirmation);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button Cancel = dialog.findViewById(R.id.btn_cancel);
        Button Okay = dialog.findViewById(R.id.btn_okay);
        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer les informations de réservation depuis l'Intent
                String idBor = getIntent().getStringExtra("idBor");
                String phoneU = getIntent().getStringExtra("phoneU");
                String matrVeh = getIntent().getStringExtra("matrVeh");
                String priseT = getIntent().getStringExtra("priseT");
                String datRe = getIntent().getStringExtra("datRe");
                String heurD = getIntent().getStringExtra("heurD");
                String heurF = getIntent().getStringExtra("heurF");

                createReservationInFirebase(idBor, phoneU, matrVeh, priseT, datRe, heurD, heurF, "Pending");
                // Créer le contenu du fichier texte avec les détails de la réservation
                String reservationDetails = "Reservation Details:\n"
                        + "Date: " + datRe + "\n"
                        + "Time: " + heurD + " - " + heurF + "\n"
                        + "Vehicle: " + matrVeh + "\n"
                        + "Charging Point: " + priseT + "\n"
                        // Ajoutez d'autres détails de réservation ici
                        + "Status: Pending";

                // Passer les détails de réservation à la nouvelle activité
                Intent detailsIntent = new Intent(PaymentActivity.this, ReservationDetailsActivity.class);
                detailsIntent.putExtra("reservationDetails", reservationDetails);
                startActivity(detailsIntent);



//
                if (reservationConfirmedListener != null) {
                    reservationConfirmedListener.onReservationConfirmed();
                }
                // Suivre le statut de la réservation
                dialog.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation();
                dialog.dismiss();
            }
        });
        Button addPaymentButton = findViewById(R.id.buttonAddPaymentCard);
        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Afficher le même dialogue de confirmation que précédemment
                dialog.show();
                                                }
        });



        // Calculer la durée de recharge en heures
        double startTime = parseTimeToHours(heurD);
        double endTime = parseTimeToHours(heurF);
        double chargingDuration = endTime - startTime;

        // Calculer le coût en fonction de la durée de la recharge et du tarif de 30$ par heure
        double chargingCost = chargingDuration * 30;

        // Mettre à jour les TextView avec les informations calculées
        TextView chargingTimeTextView = findViewById(R.id.chargingTimeTextView);
        chargingTimeTextView.setText("Charging Time: " + heurD + " - " + heurF);

        TextView chargingCostTextView = findViewById(R.id.chargingCostTextView);
        chargingCostTextView.setText("Charging Cost: $" + String.format("%.2f", chargingCost));
    }

    // Méthode pour convertir l'heure au format HH:mm en heures décimales
    private double parseTimeToHours(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours + (minutes / 60.0);

    }
    private void createReservationInFirebase(String idBor, String phoneU, String matrVeh, String priseT,
                                             String datRe, String heurD, String heurF ,String status) {

        // Appelez la méthode checkReservationStatus en passant l'ID de la réservation

        databaseReference.child("reservation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(idBor)) {
                    Toast.makeText(PaymentActivity.this, "Borne reserved", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference reservationRef = databaseReference.child("reservation").child(idBor);
                    reservationRef.child("Phone").setValue(phoneU);
                    reservationRef.child("Matricule").setValue(matrVeh);
                    reservationRef.child("Branchement").setValue(priseT);
                    reservationRef.child("DateReservation").setValue(datRe);
                    reservationRef.child("HeureD_R").setValue(heurD);
                    reservationRef.child("HeureF_R").setValue(heurF);
                    reservationRef.child("Status").setValue(status);

                    // Afficher un message de réussite puis terminer l'activité
                    Toast.makeText(PaymentActivity.this, "Reservations submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to create reservation: " + error.getMessage());
            }
        });
    }




    private void cancelReservation() {
        String idBor = getIntent().getStringExtra("idBor");

        // Supprimer la réservation de la base de données
        DatabaseReference reservationRef = databaseReference.child("reservation").child(idBor);
        reservationRef.removeValue();

        // Afficher un message de réussite puis terminer l'activité
        Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }
}