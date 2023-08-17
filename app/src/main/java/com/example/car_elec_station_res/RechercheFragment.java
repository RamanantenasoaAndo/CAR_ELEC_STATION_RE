package com.example.car_elec_station_res;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.car_elec_station_res.Model.Reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class RechercheFragment extends Fragment {

    private DatabaseReference databaseReference;
    private String loggedPhone; // Numéro de téléphone de l'utilisateur connecté


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recherche, container, false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        loggedPhone = sharedPreferences.getString("logged_in_phone", "");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("reservation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LinearLayout linearLayout = view.findViewById(R.id.ReservationListLayout);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Récupérer les données de chaque réservation
                        String branchement = dataSnapshot.child("Branchement").getValue(String.class);
                        String dateReservation = dataSnapshot.child("DateReservation").getValue(String.class);
                        String heureDR = dataSnapshot.child("HeureD_R").getValue(String.class);
                        String heureFR = dataSnapshot.child("HeureF_R").getValue(String.class);
                        String matricule = dataSnapshot.child("Matricule").getValue(String.class);
                        String phone = dataSnapshot.child("Phone").getValue(String.class);
                        String status = dataSnapshot.child("Status").getValue(String.class);
                        // Créer une vue pour afficher les données
                        if (phone != null && phone.equals(loggedPhone)) {
                            View itemView = getLayoutInflater().inflate(R.layout.item_recherche, null);
                            TextView textViewBranchement = itemView.findViewById(R.id.textViewBranchement);
                            TextView textViewDateReservation = itemView.findViewById(R.id.textViewDateReservation);
                            TextView textViewHeureDR = itemView.findViewById(R.id.textViewHeureD_R);
                            TextView textViewHeureFR = itemView.findViewById(R.id.textViewHeureF_R);
                            TextView textViewMatricule = itemView.findViewById(R.id.textViewMatricule);
                            TextView textViewPhone = itemView.findViewById(R.id.textViewPhone);
                            TextView textViewStatus = itemView.findViewById(R.id.textViewStatus);
                            Button buttonModifier = itemView.findViewById(R.id.buttonModifier);
                            Button buttonSupprimer = itemView.findViewById(R.id.buttonSupprimer);

                            // Afficher les données dans les vues
                            textViewBranchement.setText(branchement);
                            textViewDateReservation.setText(dateReservation);
                            textViewHeureDR.setText(heureDR);
                            textViewHeureFR.setText(heureFR);
                            textViewMatricule.setText(matricule);
                            textViewPhone.setText(phone);
                            if ("Expired".equals(status)) {
                                textViewStatus.setTextColor(getResources().getColor(R.color.red)); // Assurez-vous que la couleur rouge est définie dans vos ressources

                            }
                            textViewStatus.setText(status);

                            // Ajouter les boutons "Modifier" et "Supprimer"
                            buttonModifier = new Button(getContext());
                            buttonModifier.setText("Modifier");
                            buttonSupprimer = new Button(getContext());
                            buttonSupprimer.setText("Supprimer");
                            // Ajouter les boutons à la vue
                            LinearLayout buttonLayout = new LinearLayout(getContext());
                            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                            buttonLayout.addView(buttonModifier);
                            buttonLayout.addView(buttonSupprimer);

                            linearLayout.addView(buttonLayout); // Ajouter les boutons à la liste des réservations
                            // Ajouter la vue à la liste des réservations
                            linearLayout.addView(itemView);

                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

