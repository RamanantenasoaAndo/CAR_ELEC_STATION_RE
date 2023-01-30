package com.example.car_elec_station_res;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SingUp extends AppCompatActivity   {
    Button haveAccbtn,subbtn;
    TextInputEditText passUsere,Nphonee,maile,nameUsere,Fnamee;
    Connection connect;
    ColorSpace.Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //boutton si l'utilisateur a des  compte
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
                checkRegistreUser();
            }

        });
    }
    private void checkRegistreUser() {
        Fnamee = findViewById(R.id.Fname);
        nameUsere = findViewById(R.id.nameUser);
        maile = findViewById(R.id.mail);
        Nphonee = findViewById(R.id.Nphone);
        passUsere = findViewById(R.id.passUser);

        String getFname = Fnamee.getText().toString();
        String getnameuser = nameUsere.getText().toString();
        String getmail = maile.getText().toString();
        String getphone = Nphonee.getText().toString();
        String getpass = passUsere.getText().toString();
        if (getFname.trim().equals("")||getnameuser.trim().equals("")||getmail.trim().equals("")||getphone.trim().equals("")||getpass.trim().equals("")){
            Toast.makeText(getApplicationContext(), "veuillez remplir le champ", Toast.LENGTH_LONG).show();

        }else {
            try {
                connectDB connectDB = new connectDB();
                connect = connectDB.conclass();
                if (connect!=null) {
                    String query = "insert INTO utilisateur (username,fullname,email,phone,pass) values ('"+nameUsere.getText()+"','"+Fnamee.getText()+"','"+maile.getText()+"','"+Nphonee.getText()+"','"+passUsere.getText()+"')";
                    Statement st = connect.createStatement();
                    if (st.executeUpdate(query) != 0)  {
                        Toast.makeText(getApplicationContext(), "Inscription réussite!", Toast.LENGTH_LONG).show();
                        successLogin();
                    } else {
                        Toast.makeText(getApplicationContext(), "inscription non validee !", Toast.LENGTH_LONG).show();
                        nameUsere.setText("");
                        passUsere.setText("");
                        Nphonee.setText("");
                        passUsere.setText("");
                        Fnamee.setText("");
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connexion Internet!", Toast.LENGTH_LONG).show();
                }

            }catch(Exception ex){
                Log.e("Error : ", ex.getMessage());
            }
        }


    }


    public void  successLogin(){
        Intent intent = new Intent(SingUp.this,Login.class);
        startActivity(intent);
        finish();

    }

}