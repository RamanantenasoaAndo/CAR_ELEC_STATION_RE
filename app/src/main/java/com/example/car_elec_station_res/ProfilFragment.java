package com.example.car_elec_station_res;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
}
