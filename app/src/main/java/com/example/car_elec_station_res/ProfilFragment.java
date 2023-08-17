package com.example.car_elec_station_res;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class ProfilFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        // Trouver le bouton de déconnexion dans la mise en page
        View logoutButton = view.findViewById(R.id.outbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        // Attacher un OnClickListener au ConstraintLayout "informationsLayout"
        androidx.constraintlayout.widget.ConstraintLayout informationsLayout = view.findViewById(R.id.informationsLayout);
        informationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), InformationActivity.class);
                startActivity(intent);
            }
        });

        androidx.constraintlayout.widget.ConstraintLayout vehiculeLayout = view.findViewById(R.id.vehicule);
        vehiculeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });

        androidx.constraintlayout.widget.ConstraintLayout notificationLayout = view.findViewById(R.id.notification);
        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });

        androidx.constraintlayout.widget.ConstraintLayout compteLayout = view.findViewById(R.id.compter);
        compteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });

        androidx.constraintlayout.widget.ConstraintLayout histoLayout = view.findViewById(R.id.historique);
        histoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });

        androidx.constraintlayout.widget.ConstraintLayout paramLayout = view.findViewById(R.id.parametre);
        paramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_fragment_title);
    }

    private void logoutUser() {
        // Effacer les informations de connexion des SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", false);
        editor.putString("logged_in_phone", "");
        editor.apply();

        // Rediriger vers l'écran de connexion
        Intent intent = new Intent(requireContext(), MapsFragment.class);
        startActivity(intent);
        requireActivity().finish(); // Fermer l'activité actuelle
    }
    private void showInformationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Informations");
        builder.setMessage("Ceci est un exemple de boîte de dialogue d'informations su l'APP.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action à effectuer lorsque l'utilisateur clique sur le bouton OK
                dialog.dismiss(); // Fermer la boîte de dialogue
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
