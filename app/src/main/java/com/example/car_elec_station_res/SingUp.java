package com.example.car_elec_station_res;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SingUp extends AppCompatActivity {
    Button haveAccbtn,subbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //boutton si l'utilisateur a dea un compte
        haveAccbtn = findViewById(R.id.HaveAccountbtn);
        haveAccbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingUp.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        //boutton enregistrement
        subbtn = findViewById(R.id.submitBtn);
        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registreUser();
                finish();
            }
        });
    }
    public void  registreUser(){
        Intent intent = new Intent(SingUp.this,Login.class);
        startActivity(intent);
        finish();

    }

}