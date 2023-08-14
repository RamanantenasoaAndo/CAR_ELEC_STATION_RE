package com.example.car_elec_station_res;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button buttonSignup, btnlog;
    TextInputEditText phone, pass;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://car-elec-station-res-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        buttonSignup = findViewById(R.id.buttonSignUp);
        btnlog = findViewById(R.id.btnlogin);

        // Button vers la formulaire d'enregistrement
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SingUp.class));
            }
        });

        // Button Login
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getphone = phone.getText().toString();
                String getUpass = pass.getText().toString();

                if (getphone.isEmpty() || getUpass.isEmpty()) {
                    Toast.makeText(Login.this, "Please Enter your Phone number or password", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Vérifiez si le numéro de téléphone existe dans la base de données Firebase
                            if (snapshot.hasChild(getphone)) {
                                // Le numéro de téléphone existe dans la base de données Firebase
                                // Obtenez le mot de passe de l'utilisateur depuis les données Firebase et comparez-le avec le mot de passe saisi par l'utilisateur
                                String getPassword = snapshot.child(getphone).child("Password").getValue(String.class);
                                if (getPassword.equals(getUpass)) {
                                    Toast.makeText(Login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();

                                    // Enregistrez l'état de connexion et le numéro de téléphone de l'utilisateur dans les SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("is_logged_in", true);
                                    editor.putString("logged_in_phone", getphone);
                                    editor.apply();

                                    // Ouvrez l'activité principale ou l'écran de réservation
                                    Intent intent = new Intent(getApplicationContext(), Formulaire_reservation.class);
                                    String idB = getIntent().getStringExtra("idbo");
                                    intent.putExtra("phoneuser", getphone);
                                    intent.putExtra("idbo", idB);
                                    startActivity(intent);
                                    finish(); // Fermez cette activité pour empêcher l'utilisateur de revenir en arrière

                                } else {
                                    Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Phone number is not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Gestion des erreurs
                        }
                    });
                }
            }
        });
    }
}
