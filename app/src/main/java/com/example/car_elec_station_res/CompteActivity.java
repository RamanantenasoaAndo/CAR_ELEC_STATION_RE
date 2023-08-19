package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompteActivity extends AppCompatActivity {


    private TextView phoneNumberTextView;
    private EditText usernameTextView;
    private EditText emailTextView;
    private com.google.android.material.textfield.TextInputEditText passTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);


        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        passTextView = findViewById(R.id.passTextView);

        // Obtenir la référence à la barre d'action (ActionBar)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Afficher la flèche de retour
        }
        Button editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les nouvelles valeurs
                String newUsername = usernameTextView.getText().toString();
                String newEmail = emailTextView.getText().toString();
                String newPassword = passTextView.getText().toString();

                // Mettre à jour les valeurs dans Firebase
                updateUserInfo(newUsername, newEmail, newPassword);
            }
        });

        // Récupérer le numéro de téléphone connecté à partir des SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String loggedPhone = sharedPreferences.getString("logged_in_phone", "");

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(loggedPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("Fullname").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String pass = snapshot.child("Password").getValue(String.class);


                    // Afficher les informations dans les TextViews
                    phoneNumberTextView.setText(loggedPhone);
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    passTextView.setText((pass));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gérer les erreurs
            }
        });
    }
    private void updateUserInfo(String newUsername, String newEmail, String newPassword) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String loggedPhone = sharedPreferences.getString("logged_in_phone", "");

        usersRef.child(loggedPhone).child("Fullname").setValue(newUsername);
        usersRef.child(loggedPhone).child("Email").setValue(newEmail);
        // Mettre à jour le mot de passe si nécessaire
        if (!TextUtils.isEmpty(newPassword)) {
            usersRef.child(loggedPhone).child("Password").setValue(newPassword);
        }

        Toast.makeText(this, "Modifications enregistrées avec succès", Toast.LENGTH_SHORT).show();
        Intent splashIntent = new Intent(this, Splash.class);
        splashIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(splashIntent);
        finish();
    }
}


