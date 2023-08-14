package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingUp extends AppCompatActivity   {
    Button haveAccbtn,subbtn;
    TextInputEditText passUsere,Nphonee,maile,Fnamee;
    //create object of databasereference class to access firebase's Realtime database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://car-elec-station-res-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        Fnamee = findViewById(R.id.Fname);
        maile = findViewById(R.id.mail);
        Nphonee = findViewById(R.id.Nphone);
        passUsere = findViewById(R.id.passUser);

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
        subbtn.setOnClickListener(v -> createAccount());

    }
void createAccount(){
    String Fname = Fnamee.getText().toString();
    String mail = maile.getText().toString();
    String phone = Nphonee.getText().toString();
    String pass = passUsere.getText().toString();

    //check if user fill all fields before sending data to firebase
    if(Fname.isEmpty()||mail.isEmpty()||phone.isEmpty()||pass.isEmpty()){
        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
    }

    boolean isValidated = validateData(mail,phone,pass);
    //si la validation est faux
    if (!isValidated){
        return;
    }
    createAccountInFirebase(Fname,mail,phone,pass);
}
void createAccountInFirebase(String Fname, String mail, String phone, String pass){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check if phone is not registered before
              if(snapshot.hasChild(phone)){
                  Toast.makeText(SingUp.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
              }
              else{
                  databaseReference.child("users").child(phone).child("Fullname").setValue(Fname);
                  databaseReference.child("users").child(phone).child("Email").setValue(mail);
                  databaseReference.child("users").child(phone).child("Password").setValue(pass);
//show a success message then finish the activity.
                  // Enregistrement du numéro de téléphone dans les préférences partagées
                  SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
                  SharedPreferences.Editor editor = preferences.edit();
                  editor.putString("user_phone", phone); // Remplacez "user_phone" par la clé appropriée
                  editor.apply();
                  Toast.makeText(SingUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();


                  startActivity(new Intent(SingUp.this,Login.class));
              }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

}
boolean validateData(String mail ,String phone,String pass){
        //validate the data that are input user.
    if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
        maile.setError("Email is invalid");
        return false;
    }
    if (pass.length()<4){
        passUsere.setError("Password lenght is invalid");
        return false;
    }
    if (phone.length()<8){
        Nphonee.setError("Phone Number is invalid");
        return false;
    }
    return  true;
}

    public void  successLogin(){
        Intent intent = new Intent(SingUp.this,Login.class);
        startActivity(intent);
        finish();

    }

}