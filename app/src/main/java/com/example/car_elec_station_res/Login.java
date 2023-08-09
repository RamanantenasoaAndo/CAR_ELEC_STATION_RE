package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    Button buttonSignup,btnlog;
    TextInputEditText phone,pass;

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

        //Button vers la formulaire d'enregistrement
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SingUp.class));
            }
        });

        //Button Login
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  getphone = phone.getText().toString();
                String getUpass = pass.getText().toString();

                if(getphone.isEmpty()|| getUpass.isEmpty()){
                    Toast.makeText(Login.this, "Please Enter your Phone number or password", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if mobile/phone is exist in  firebase database
                        if(snapshot.hasChild(getphone)){
                            //mobil is exist in firebase database
                            //get password of user from firebase data and match it with user entered password
                            String getPassword = snapshot.child(getphone).child("Password").getValue(String.class);
                            if(getPassword.equals(getUpass)){
                                Toast.makeText(Login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                //open Acceuil activity on success.
                                Intent intent = new Intent(getApplicationContext(),Formulaire_reservation.class);
                                String  getphone = phone.getText().toString();
                                String idB = getIntent().getStringExtra("idbo");

                                intent.putExtra("phoneuser",getphone);
                                intent.putExtra("idbo",idB);
                                startActivity(intent);


                            }
                            else {
                                Toast.makeText(Login.this, "wrong password", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Login.this, "phone number is not exist", Toast.LENGTH_SHORT).show();
                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}
