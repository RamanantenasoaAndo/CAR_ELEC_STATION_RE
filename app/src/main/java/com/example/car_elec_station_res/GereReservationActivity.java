package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class GereReservationActivity extends AppCompatActivity {

    private TextView textViewPrimaryKey;
    private TextView textViewStatus;
    private DatabaseReference reservationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gere_reservation);

        // Initialisez la référence à votre base de données
        reservationsRef = FirebaseDatabase.getInstance().getReference("reservation");

        // Récupérez le Primary Key et le Statut à partir de la base de données
        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LinearLayout linearLayout = findViewById(R.id.ReservationStatus);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Récupérer les données de chaque réservation
                        String primaryKey = dataSnapshot.getKey();
                        String status = dataSnapshot.child("Status").getValue(String.class);
                        String heureFinReservation = dataSnapshot.child("HeureF_R").getValue(String.class);

                        // Créer une vue pour afficher les données
                        View itemView = getLayoutInflater().inflate(R.layout.item_reservation, null);

                        // Récupérer les TextView de la vue
                        TextView textViewPrimaryKey = itemView.findViewById(R.id.textViewPrimaryKey);
                        TextView textViewStatus = itemView.findViewById(R.id.textViewStatus);
                        TextView textViewHeureFinReservation = itemView.findViewById(R.id.textViewHeureFinReservation);

                        // Afficher les données dans les TextView
                        textViewPrimaryKey.setText("Borne: " + primaryKey);
                        textViewStatus.setText("Status: " + status);
                        textViewHeureFinReservation.setText("Heure de fin de réservation: " + heureFinReservation);

                        // Comparer l'heure de fin de réservation avec l'heure actuelle
                        Calendar currentTime = Calendar.getInstance();
                        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = currentTime.get(Calendar.MINUTE);

                        int reservationHour = Integer.parseInt(heureFinReservation.substring(0, 2));
                        int reservationMinute = Integer.parseInt(heureFinReservation.substring(3, 5));

                        if (currentHour > reservationHour || (currentHour == reservationHour && currentMinute >= reservationMinute)) {
                            // L'heure de fin de réservation est dépassée ou égale à l'heure actuelle
                            // Faites quelque chose ici, par exemple mettre à jour le statut de la réservation
                            DatabaseReference reservationRef = reservationsRef.child(primaryKey);
                            reservationRef.child("Status").setValue("Expired");
                            Toast.makeText(GereReservationActivity.this, "L'heure de fin de réservation est dépassée", Toast.LENGTH_SHORT).show();
                            // Appeler la méthode pour vérifier à nouveau le statut de la réservation
                            checkReservationStatus(primaryKey);
                        }

                        // Ajouter la vue à la liste des réservations
                        linearLayout.addView(itemView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestion des erreurs lors de la récupération des données
            }
        });
    }
    // Méthode pour vérifier à nouveau le statut de la réservation
    private void checkReservationStatus(String primaryKey) {
        reservationsRef.child(primaryKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("Status").getValue(String.class);
                    // Mettez à jour l'affichage du statut de la réservation dans votre application, par exemple :
                     textViewStatus.setText("Status: " + status);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestion des erreurs lors de la récupération des données
            }
        });
    }
}


