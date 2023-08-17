package com.example.car_elec_station_res;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RefreshReceiver extends BroadcastReceiver {

    private static boolean notificationSent = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("reservation");
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        String loggedPhone = sharedPreferences.getString("logged_in_phone", "");



        if (isLoggedIn) {

            reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String dateReservation = dataSnapshot.child("DateReservation").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("Phone").getValue(String.class);
                        String status = dataSnapshot.child("Status").getValue(String.class);
                        String matricule = dataSnapshot.child("Matricule").getValue(String.class); // Ajoutez cette ligne
                        String heureD_R = dataSnapshot.child("HeureD_R").getValue(String.class);
                        String heureF_R = dataSnapshot.child("HeureF_R").getValue(String.class);
                        // Convertir la date de réservation, l'heure de début et l'heure de fin en objets Calendar
                        Calendar reservationDateTime = convertToCalendar(dateReservation, heureD_R);
                        Calendar endDateTime = convertToCalendar(dateReservation, heureF_R);

                        // Vérifier si la réservation est associée au numéro de téléphone de la personne connectée

                            if (!notificationSent) {
                                if (phoneNumber != null && phoneNumber.equals(loggedPhone) && status.equals("Expired")) {
                                    showNotification(context, "Statut de réservation", "La reservation de Borne est expirée, Num MAT: " + matricule, matricule);
                                    notificationSent = true;

                                    // Mettre à jour la valeur dans SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("notification_sent", false);
                                    editor.apply();
                                }

                                if (isStartTimeApproaching(reservationDateTime) && !notificationSent) {
                                    showNotification(context, "Alerte de recharge", "Votre heure de début de recharge approche dans 10 minutes. Num MAT: " + matricule, matricule);
                                    notificationSent = true;

                                    // Mettre à jour la valeur dans SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("notification_sent", false);
                                    editor.apply();
                                }

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gestion des erreurs
                }
            });
        }


        // Obtenir la date et l'heure actuelles
        Calendar currentDateTime = Calendar.getInstance();
        int currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentDateTime.get(Calendar.MINUTE);


        reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String dateReservation = dataSnapshot.child("DateReservation").getValue(String.class);
                    String heureD_R = dataSnapshot.child("HeureD_R").getValue(String.class);
                    String heureF_R = dataSnapshot.child("HeureF_R").getValue(String.class);
                    String status = dataSnapshot.child("Status").getValue(String.class);


                    // Convertir la date de réservation, l'heure de début et l'heure de fin en objets Calendar
                    Calendar reservationDateTime = convertToCalendar(dateReservation, heureD_R);
                    Calendar endDateTime = convertToCalendar(dateReservation, heureF_R);

                    // Comparer la date et l'heure actuelles avec la date de réservation et l'heure de début/fin
                    // Vérifier si l'heure actuelle est à l'intérieur de la plage horaire de début et de fin de recharge
                    if (currentDateTime.after(reservationDateTime) && currentDateTime.before(endDateTime)) {
                        // Mettre à jour le statut en cours
                        reservationsRef.child(dataSnapshot.getKey()).child("Status").setValue("En cours");
                        Toast.makeText(context, "Statut de réservation mis à jour : En cours", Toast.LENGTH_SHORT).show();
                    } else if (currentDateTime.equals(reservationDateTime)) {
                        // La date de réservation et l'heure de début sont égales à la date et l'heure actuelles
                        // Mettre à jour le statut en cours
                        reservationsRef.child(dataSnapshot.getKey()).child("Status").setValue("En cours");
                        Toast.makeText(context, "Statut de réservation mis à jour : En cours", Toast.LENGTH_SHORT).show();
                    }else if (currentDateTime.after(reservationDateTime)) {
                        // L'heure actuelle est après la date de réservation et l'heure de début
                        if (currentDateTime.before(endDateTime)) {
                            // L'heure actuelle est avant la date de réservation et l'heure de fin
                            // Mettre à jour le statut en attente
                            reservationsRef.child(dataSnapshot.getKey()).child("Status").setValue("En attente");
                            Toast.makeText(context, "Statut de réservation mis à jour : En attente", Toast.LENGTH_SHORT).show();
                        } else {
                            // L'heure actuelle est après la date de réservation et l'heure de fin
                            // Mettre à jour le statut en tant qu'expiré
                            reservationsRef.child(dataSnapshot.getKey()).child("Status").setValue("Expired");
                            Toast.makeText(context, "Statut de réservation mis à jour : Expiré", Toast.LENGTH_SHORT).show();

                            // Transférer la réservation expirée vers la table historique
                            DatabaseReference historiqueRef = FirebaseDatabase.getInstance().getReference("historique_reservations");
                            historiqueRef.child(dataSnapshot.getKey()).setValue(dataSnapshot.getValue());
                            historiqueRef.child(dataSnapshot.getKey()).child("ExpirationTimestamp").setValue(ServerValue.TIMESTAMP);
                        }
                    } else {
                        // La date de réservation et l'heure de début sont dans le futur
                        // Mettre à jour le statut en attente
                        reservationsRef.child(dataSnapshot.getKey()).child("Status").setValue("En attente");
                        Toast.makeText(context, "Statut de réservation mis à jour : En attente", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestion des erreurs
            }
        });
    }

    // Méthode pour convertir la date et l'heure en un objet Calendar
    private Calendar convertToCalendar(String date, String time) {
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1; // Mois commence à 0
        int day = Integer.parseInt(date.substring(8, 10));
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));

        calendar.set(year, month, day, hour, minute);
        return calendar;
    }
    // Méthode pour vérifier si l'heure de début de recharge approche dans les 10 minutes
    private boolean isStartTimeApproaching(Calendar startTime) {
        Calendar tenMinutesBeforeStartTime = (Calendar) startTime.clone();
        tenMinutesBeforeStartTime.add(Calendar.MINUTE, -10);

        Calendar currentDateTime = Calendar.getInstance();

        return currentDateTime.before(startTime) && currentDateTime.after(tenMinutesBeforeStartTime);
    }


    // Méthode pour afficher une notification
    private void showNotification(Context context, String title, String message,String matricule) {
        // Créer un canal de notification (nécessaire pour Android 8.0 et versions ultérieures)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Créer et afficher la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }

}
