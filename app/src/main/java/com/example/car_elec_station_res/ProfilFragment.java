package com.example.car_elec_station_res;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.car_elec_station_res.Model.Reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.core.app.NotificationCompat;

import java.util.Locale;


public class ProfilFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        // Afficher la flèche de retour dans la Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Trouver le bouton de déconnexion dans la mise en page
        View logoutButton = view.findViewById(R.id.outbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        //INFORMATION

        // Attacher un OnClickListener au ConstraintLayout "informationsLayout"
        androidx.constraintlayout.widget.ConstraintLayout informationsLayout = view.findViewById(R.id.informationsLayout);
        informationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), InformationActivity.class);
                startActivity(intent);
            }
        });

        //VEHICULE

        androidx.constraintlayout.widget.ConstraintLayout vehiculeLayout = view.findViewById(R.id.vehicule);
        vehiculeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });

//COMPTE
        androidx.constraintlayout.widget.ConstraintLayout compteLayout = view.findViewById(R.id.compter);
        compteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompteAlert();
            }
        });
        //HISTORIQUE

        androidx.constraintlayout.widget.ConstraintLayout histoLayout = view.findViewById(R.id.historique);
        histoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistorique();
            }
        });
//PARAMETRE
        androidx.constraintlayout.widget.ConstraintLayout paramLayout = view.findViewById(R.id.parametre);
        paramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParametresOptions();
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
    // RefreshReceiver.java
    private void showCompteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Informations");

        // Récupérer le numéro de téléphone connecté à partir des SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String loggedPhone = sharedPreferences.getString("logged_in_phone", "");

        // Récupérer les informations de l'utilisateur depuis Firebase
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(loggedPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("Fullname").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);


                    // Afficher les informations de l'utilisateur dans la boîte de dialogue
                    String message = "Numéro de téléphone : " + loggedPhone + "\n";
                    message += "Nom d'utilisateur : " + username + "\n";
                    message += "Email : " + email;

                    builder.setMessage(message);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Fermer la boîte de dialogue
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    // L'utilisateur n'existe pas dans la base de données
                    builder.setMessage("Aucune information disponible pour cet utilisateur.");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Fermer la boîte de dialogue
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestion des erreurs
            }
        });
    }
    private void showHistorique() {
        DatabaseReference historiqueRef = FirebaseDatabase.getInstance().getReference("historique_reservations");
        // Récupérer le numéro de téléphone connecté à partir des SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String loggedPhone = sharedPreferences.getString("logged_in_phone", "");

        historiqueRef.orderByChild("Phone").equalTo(loggedPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder historyText = new StringBuilder();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String branchement = dataSnapshot.child("Branchement").getValue(String.class);
                        String dateReservation = dataSnapshot.child("DateReservation").getValue(String.class);
                        String HeureD_R = dataSnapshot.child("HeureD_R").getValue(String.class);
                        String HeurF_R = dataSnapshot.child("HeurF_R").getValue(String.class);
                        String Matricule = dataSnapshot.child("Matricule").getValue(String.class);
                        String Phone = dataSnapshot.child("Phone").getValue(String.class);


                        historyText.append("Branchement: ").append(branchement).append("\n");
                        historyText.append("Date de réservation: ").append(dateReservation).append("\n");
                        historyText.append("Heure de début: ").append(HeureD_R).append("\n");
                        historyText.append("Heure de fin: ").append(HeurF_R).append("\n");
                        historyText.append("Matricule: ").append(Matricule).append("\n");
                        historyText.append("Statut: ").append(Phone).append("\n\n");
                    }
                    // Afficher l'historique dans une boîte de dialogue
                    showHistoryDialog(historyText.toString());
                } else {
                    // Aucun historique trouvé
                    Toast.makeText(requireContext(), "Aucun historique de réservation trouvé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestion des erreurs
            }
        });
    }

    private void showHistoryDialog(String historyText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Historique des réservations");
        builder.setMessage(historyText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showParametresOptions() {
        // Créer un AlertDialog avec une liste d'options paramétrables
        String[] parametresOptions = {"Compte ", "Langue"}; // Remplacez par vos options réelles
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Paramètres");
        builder.setItems(parametresOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Gérer le clic sur une option
                switch (item) {
                    case 0:
                        // Gérer l'option 1
                        // Ouvrir l'activité CompteActivity
                        Intent compteIntent = new Intent(requireContext(), CompteActivity.class);
                        startActivity(compteIntent);
                        break;
                    case 1:
                        // Gérer l'option 2
                        showLanguageOptions(); // Afficher la liste des langues
                        break;

                }
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showLanguageOptions() {
        String[] languages = {"English", "Français", "Español", "Deutsch", "Italiano"}; // Liste des langues

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Language");
        builder.setItems(languages, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Mettre à jour la langue de l'application
                updateLanguage(languages[item]);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateLanguage(String selectedLanguage) {
        Locale locale;
        switch (selectedLanguage) {
            case "Français":
                locale = new Locale("fr");
                break;
            case "Español":
                locale = new Locale("es");
                break;
            // Ajoutez d'autres langues ici
            default:
                locale = new Locale("en");
                break;
        }

        // Mettre à jour la configuration de la langue
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // Rafraîchir l'interface utilisateur si nécessaire
        // (actualisation du texte, des images, etc.)

        // Redémarrer l'activité pour appliquer les changements de langue
        getActivity().recreate();
    }



}











