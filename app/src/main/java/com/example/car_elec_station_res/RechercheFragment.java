package com.example.car_elec_station_res;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recherche, container, false);


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
                        View itemView = getLayoutInflater().inflate(R.layout.item_recherche, null);
                        TextView textViewBranchement = itemView.findViewById(R.id.textViewBranchement);
                        TextView textViewDateReservation = itemView.findViewById(R.id.textViewDateReservation);
                        TextView textViewHeureDR = itemView.findViewById(R.id.textViewHeureD_R);
                        TextView textViewHeureFR = itemView.findViewById(R.id.textViewHeureF_R);
                        TextView textViewMatricule = itemView.findViewById(R.id.textViewMatricule);
                        TextView textViewPhone = itemView.findViewById(R.id.textViewPhone);
                        TextView textViewStatus = itemView.findViewById(R.id.textViewStatus);

                        // Afficher les données dans les vues
                        textViewBranchement.setText(branchement);
                        textViewDateReservation.setText(dateReservation);
                        textViewHeureDR.setText(heureDR);
                        textViewHeureFR.setText(heureFR);
                        textViewMatricule.setText(matricule);
                        textViewPhone.setText(phone);
                        textViewStatus.setText(status);

                        // Ajouter la vue à la liste des réservations
                        linearLayout.addView(itemView);
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

