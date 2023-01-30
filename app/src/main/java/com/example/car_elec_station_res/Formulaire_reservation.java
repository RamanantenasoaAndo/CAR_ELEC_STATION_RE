package com.example.car_elec_station_res;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Console;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Formulaire_reservation extends AppCompatActivity {
    String[] items =  {"Prise Type 2","Prise Type 3","prise Combo","prise CHAdeMO"};
    AutoCompleteTextView autoCompleteTxt;
    Dialog dialog;
    ArrayAdapter<String> adapterItems;
    Button submitBtn;
    TextInputEditText borneId,userId,email,VehMatricul,dateRes;
    AutoCompleteTextView types_prise,HeureDR,HeureFR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_reservation);

        autoCompleteTxt= findViewById(R.id.types_prise);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_items,items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });


        dialog= new Dialog(Formulaire_reservation.this);
        dialog.setContentView(R.layout.dialogue_confirmation);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;


        Button Cancel = dialog.findViewById(R.id.btn_cancel);
        Button Okay = dialog.findViewById(R.id.btn_okay);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ConfirmBtn();
                    dialog.dismiss();

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelPress();
                dialog.dismiss();
            }
        });

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReservation();
            }
        });
    }

    private void submitReservation() {
        borneId = findViewById(R.id.borneId);
        email = findViewById(R.id.email);
        userId = findViewById(R.id.userId);
        VehMatricul = findViewById(R.id.VehMatricul);
        dateRes = findViewById(R.id.dateRes);
        types_prise = findViewById(R.id.types_prise);
        HeureDR = findViewById(R.id.HeureDR);
        HeureFR = findViewById(R.id.HeureFR);

        String getborneId = borneId.getText().toString();
        String getemail = email.getText().toString();
        String getuserId = userId.getText().toString();
        String getVehMatricul = VehMatricul.getText().toString();
        String getDateRe = dateRes.getText().toString();
        String gettypesPrise = types_prise.getText().toString();
        String getHeureDR = HeureDR.getText().toString();
        String getHeureFR = HeureFR.getText().toString();

        if (getborneId.trim().equals("")||getuserId.trim().equals("")||getemail.trim().equals("")
                ||getVehMatricul.trim().equals("")||getDateRe.trim().equals("")||gettypesPrise.trim().equals("")
                ||getHeureDR.trim().equals("")||getHeureFR.trim().equals("")){
            Toast.makeText(getApplicationContext(), "veuillez remplir le champ", Toast.LENGTH_LONG).show();
        }else {
            try {
                dialog.show();
            } catch (Exception ex) {
                Log.e("Error : ", ex.getMessage());
            }
        }
    }

    private  void ConfirmBtn()  {
        borneId = findViewById(R.id.borneId);
        email = findViewById(R.id.email);
        userId = findViewById(R.id.userId);
        VehMatricul = findViewById(R.id.VehMatricul);
        dateRes = findViewById(R.id.dateRes);
        types_prise = findViewById(R.id.types_prise);
        HeureDR = findViewById(R.id.HeureDR);
        HeureFR = findViewById(R.id.HeureFR);
        try {
            connectDB connectDB = new connectDB();
            Connection connect = connectDB.conclass();
            if (connect != null) {
                String query = "insert INTO reservation (IdBorne,id_user,Email,NumMatricVeh,DateRes,typePrise,HeurDRech,HeurFRech)" +
                        " values " +
                        "('" + borneId.getText() + "','" + userId.getText() + "','" + email.getText() + "'," +
                        "'" + VehMatricul.getText() + "','" + dateRes.getText() + "','" + types_prise.getText() + "'," +
                        "'" + HeureDR.getText() + "', '" + HeureFR.getText() + "')";
                Statement st = connect.createStatement();
                if (st.executeUpdate(query) != 0){
                    Toast.makeText(getApplicationContext(), "Reservation Reussite!", Toast.LENGTH_LONG).show();
                    setFragment();
                }else{
                    Toast.makeText(getApplicationContext(),"Reservation Non Valide!",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connexion Internet!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Log.e("Error : ", ex.getMessage());
        }

    }

    private  void CancelPress(){
        Toast.makeText(getApplicationContext(), "Réservation annulée !", Toast.LENGTH_LONG).show();
            userId.setText("");
            borneId.setText("");
            email.setText("");
            VehMatricul.setText("");
            dateRes.setText("");
            types_prise.setText("");
            HeureDR.setText("");
            HeureFR.setText("");
        }


    //from Activity to Fragment
    protected void setFragment() {
        Fragment fragment = new RechercheFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_nav_recherche, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}