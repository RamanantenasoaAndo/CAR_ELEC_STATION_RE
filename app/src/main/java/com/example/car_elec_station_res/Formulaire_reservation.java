package com.example.car_elec_station_res;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Formulaire_reservation extends AppCompatActivity {

    private static final List<String> AVAILABLE_PRISE_TYPES = Arrays.asList(
            "Prise Type 2", "Prise Type 3", "prise Combo", "prise CHAdeMO"
    );
    private TextInputEditText dateResEditText;
    private Calendar calendar;
    private Button btnHeureDR;
    private Button btnHeureFR;

    private AutoCompleteTextView autoCompleteTxt;
    private ArrayAdapter<String> adapterItems;
    private TextInputEditText VehMatricul, dateRes,batteryCapacityEditText;
    private TextView borneIdText, phoneNText,chargingTimeTextView;
    private AutoCompleteTextView types_prise;
    private Dialog dialog;

    private DatabaseReference databaseReference;
    private Handler statusHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire_reservation);

        batteryCapacityEditText = findViewById(R.id.batteryCapacityEditText);
        chargingTimeTextView = findViewById(R.id.chargingTimeTextView);
        batteryCapacityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Vérifiez si le champ de capacité de la batterie est vide
                if (!TextUtils.isEmpty(editable)) {
                    double batteryCapacity = Double.parseDouble(editable.toString());

                    // Supposons que la puissance de la borne est de 7 kW (vous pouvez ajuster cela)
                    double chargingPower = 7.0;

                    // Calculez le temps de recharge estimé en heures
                    double chargingTimeHours = batteryCapacity / chargingPower;

                    // Affichez l'estimation du temps de recharge dans le TextView
                    String estimatedChargingTime = String.format("%.2f", chargingTimeHours);
                    chargingTimeTextView.setText("Estimation du temps de recharge : " + estimatedChargingTime + " heures");
                } else {
                    // Effacez le texte de l'estimation si le champ de capacité de batterie est vide
                    chargingTimeTextView.setText("");
                }
            }
        });

        dateResEditText = findViewById(R.id.dateRes);
        calendar = Calendar.getInstance();

        // Format de date pour afficher dans le champ de texte
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        dateResEditText.setText(currentDate);

        dateResEditText.setOnClickListener(v -> showDatePickerDialog());
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://car-elec-station-res-default-rtdb.firebaseio.com/");

        VehMatricul = findViewById(R.id.VehMatricule);
        dateRes = findViewById(R.id.dateRes);

        btnHeureDR = findViewById(R.id.btnHeureDR);
        btnHeureFR = findViewById(R.id.btnHeureFR);

        // Reception de l'ID de la borne.
        borneIdText = findViewById(R.id.borneId);
        String idB = getIntent().getStringExtra("idbo");
        borneIdText.setText(idB);

        // Vérifiez si l'utilisateur est déjà connecté
        // Reception du numéro de téléphone.
        phoneNText = findViewById(R.id.phoneNum);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String loggedPhone = sharedPreferences.getString("logged_in_phone", "");
        // Si l'utilisateur est connecté, affichez automatiquement le numéro de téléphone dans le champ de saisie
        if(!TextUtils.isEmpty(loggedPhone)){
            phoneNText.setText(loggedPhone);
            phoneNText.setEnabled(false);// Désactivez la modification du numéro
        }


        autoCompleteTxt = findViewById(R.id.types_prise);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_items, AVAILABLE_PRISE_TYPES);
        autoCompleteTxt.setAdapter(adapterItems);





        // Sélection de l'heure de début de réservation

        btnHeureDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(btnHeureDR);
            }
        });

        // Sélection de l'heure de fin de réservation
        btnHeureFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(btnHeureFR);
            }
        });

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReservation();
            }

        });
    }

    private void showTimePickerDialog(final Button timeInputEditText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Formulaire_reservation.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        timeInputEditText.setText(selectedTime);
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void submitReservation() {
        String idBor = borneIdText.getText().toString();
        String phoneU = phoneNText.getText().toString();
        String matrVeh = VehMatricul.getText().toString();
        String priseT = autoCompleteTxt.getText().toString();
        String datRe = dateRes.getText().toString();
        String heurD = btnHeureDR.getText().toString();
        String heurF = btnHeureFR.getText().toString();

        // Vérifier si tous les champs sont remplis
        if (idBor.isEmpty() || phoneU.isEmpty() || matrVeh.isEmpty() || priseT.isEmpty()
                || datRe.isEmpty() || heurD.isEmpty() || heurF.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            if (compareTimes(heurD, heurF) < 0) {
                // L'heure de début de recharge est avant l'heure de fin
                // Rediriger vers la page de paiement ici
                Intent paymentIntent = new Intent(Formulaire_reservation.this, PaymentActivity.class);
                // Ajouter les données nécessaires à l'intent, par exemple:
                paymentIntent.putExtra("idBor", idBor);
                paymentIntent.putExtra("phoneU", phoneU);
                paymentIntent.putExtra("matrVeh", matrVeh);
                paymentIntent.putExtra("priseT", priseT);
                paymentIntent.putExtra("datRe", datRe);
                paymentIntent.putExtra("heurD", heurD);
                paymentIntent.putExtra("heurF", heurF);
                startActivity(paymentIntent);


            } else {
                // L'heure de début de recharge est après ou égale à l'heure de fin
                Toast.makeText(this, "Start time must be before end time", Toast.LENGTH_SHORT).show();
            }
        }
//        checkExistingReservation(phoneU);
//        // Récupérez le numéro de téléphone de l'utilisateur à partir des préférences partagées
//        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        String userPhone = sharedPreferences.getString("logged_in_phone", "");

    }





    private void cancelReservation() {
        String idBor = borneIdText.getText().toString();

        // Supprimer la réservation de la base de données
        DatabaseReference reservationRef = databaseReference.child("reservation").child(idBor);
        reservationRef.removeValue();

        // Afficher un message de réussite puis terminer l'activité
        Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Méthode pour obtenir l'heure actuelle au format HH:mm
    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Autres méthodes de la classe

    public static int compareTimes(String time1, String time2) {
        String[] parts1 = time1.split(":");
        String[] parts2 = time2.split(":");

        int hour1 = Integer.parseInt(parts1[0]);
        int minute1 = Integer.parseInt(parts1[1]);

        int hour2 = Integer.parseInt(parts2[0]);
        int minute2 = Integer.parseInt(parts2[1]);

        if (hour1 < hour2) {
            return -1;
        } else if (hour1 > hour2) {
            return 1;
        } else {
            // Les heures sont égales, comparer les minutes
            if (minute1 < minute2) {
                return -1;
            } else if (minute1 > minute2) {
                return 1;
            } else {
                // Les heures et les minutes sont égales
                return 0;
            }
        }
    }
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, yearSelected, monthSelected, dayOfMonth) -> {
                    calendar.set(yearSelected, monthSelected, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateResEditText.setText(dateFormat.format(calendar.getTime()));
                },
                year, month, day
        );

        // Définir la date minimale sur la date actuelle
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void checkExistingReservation(String phoneNumber) {
        DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("reservation");

        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String existingPhoneNumber = dataSnapshot.child("PhoneNumber").getValue(String.class);

                    if (existingPhoneNumber.equals(phoneNumber)) {
                        // Une réservation existe déjà pour ce numéro de téléphone
                        // Afficher un message à l'utilisateur ou empêcher la réservation
                        showReservationAlert();

                        return;
                    }
                }

                // Aucune réservation existante pour ce numéro de téléphone
                // L'utilisateur peut continuer avec la réservation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestion des erreurs
            }
        });
    }
    private void showReservationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Réservation existante")
                .setMessage("Vous avez déjà réservé une borne en cours ou en attente. Veuillez attendre la fin de votre réservation actuelle.")
                .setPositiveButton("OK", null)
                .show();
    }


}

