package com.example.car_elec_station_res;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    Button buttonSignup,btnlog;
    TextInputEditText username,Upass;
    Connection connect;
    String ConnectionResult="";
    TextView textViewtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Botton Login
        buttonSignup = findViewById(R.id.buttonSignUp);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SingUp.class);
                startActivity(intent);
            }
        });



//Button vers la formulaire d'enregistrement
        btnlog = findViewById(R.id.btnlogin);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

    }


    //Next activity if  login Success
    public void nextActivity(){
        Intent intent =new Intent(getApplicationContext(),Acceuil.class);
        startActivity(intent);
        finish();
    }


    //Login

    public void checkLogin(){

        username = findViewById(R.id.Uname);
        Upass = findViewById(R.id.passw);
        String  getusername = username.getText().toString();
        String getUpass = Upass.getText().toString();

        if (getUpass.trim().equals("")|| getusername.trim().equals("")){
            Toast.makeText(getApplicationContext(), "veuillez remplir le champ", Toast.LENGTH_LONG).show();
        }else {
            try {
                connectDB connectDB = new connectDB();
                connect = connectDB.conclass();
                if (connect!=null) {
                    String query = "Select * from utilisateur where username = '" + username.getText() + "' and pass = '" + Upass.getText() + "'";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    if (rs.next()) {
                        Toast.makeText(getApplicationContext(), "Success login", Toast.LENGTH_LONG).show();
                        nextActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Mot de passe ou username non valider", Toast.LENGTH_LONG).show();
                        username.setText("");
                        Upass.setText("");
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connexion Internet", Toast.LENGTH_LONG).show();
                }

            }catch(Exception ex){
                Log.e("Error : ", ex.getMessage());
            }
        }

    }






    //Display of data
    public  void getTextFromSql(){
        textViewtitle = findViewById(R.id.textBienvenue) ;
        try {
            connectDB connectDB  = new connectDB();
            connect = connectDB.conclass();
            if (connect!=null){
                String query = "Select * from utilisateur";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    textViewtitle.setText(rs.getString(2));
                }
            }else {
                ConnectionResult = "check connection";
            }

        }catch (Exception ex) {
            Log.e("Error : ", ex.getMessage());
        }

    }
}
