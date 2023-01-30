package com.example.car_elec_station_res;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.util.List;

public class ListeResAdapter extends RecyclerView.Adapter<ListeResAdapter.ViewHolder> {
    Context context;
    List<ListReservationModel> list_reservation;




    public ListeResAdapter(Context context,List<ListReservationModel> list_reservation) {
        this.context = context;
        this.list_reservation = list_reservation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list_reservation!= null && list_reservation.size()>0){
            ListReservationModel model = list_reservation.get(position);
            holder.idBorne.setText(model.getIdBrorne());
            holder.idVehicule.setText(model.getIdVehicul());
            holder.usrname.setText(model.getUsername());
            holder.dateR.setText(model.getDateR());
            holder.heurFD.setText(model.getHeureD());
        }
    }
    @Override
    public int getItemCount() {
        return list_reservation.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idBorne,idVehicule,usrname,dateR,heurFD;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idBorne = itemView.findViewById(R.id.idBorne);
            idVehicule = itemView.findViewById(R.id.idVehicule);
            usrname = itemView.findViewById(R.id.usrname);
            dateR = itemView.findViewById(R.id.dateR);
            heurFD = itemView.findViewById(R.id.heurFD);
        }
    }
}
