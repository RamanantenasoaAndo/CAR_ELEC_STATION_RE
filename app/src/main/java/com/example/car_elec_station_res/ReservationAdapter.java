package com.example.car_elec_station_res;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_elec_station_res.Model.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recherche, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        holder.textViewBranchement.setText(reservation.getBranchement());
        holder.textViewDateReservation.setText(reservation.getDateReservation());
        holder.textViewHeureD_R.setText(reservation.getHeureD_R());
        holder.textViewHeureF_R.setText(reservation.getHeureF_R());
        holder.textViewMatricule.setText(reservation.getMatricule());
        holder.textViewPhone.setText(reservation.getPhone());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewBranchement;
        TextView textViewDateReservation;
        TextView textViewHeureD_R;
        TextView textViewHeureF_R;
        TextView textViewMatricule;
        TextView textViewPhone;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewBranchement = itemView.findViewById(R.id.textViewBranchement);
            textViewDateReservation = itemView.findViewById(R.id.textViewDateReservation);
            textViewHeureD_R = itemView.findViewById(R.id.textViewHeureD_R);
            textViewHeureF_R = itemView.findViewById(R.id.textViewHeureF_R);
            textViewMatricule = itemView.findViewById(R.id.textViewMatricule);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
        }
    }
}

