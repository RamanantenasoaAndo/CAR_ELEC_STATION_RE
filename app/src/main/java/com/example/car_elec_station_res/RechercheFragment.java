package com.example.car_elec_station_res;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class RechercheFragment extends Fragment {
    RecyclerView recycler_list;
    ListeResAdapter listeResAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_recherche, container, false);
        recycler_list = view.findViewById(R.id.recycler_liste);
        setRecyclerView();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_500)));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        return  view;
    }
    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_fragment_Reservation);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_nav_menu, menu);
    }

    private void setRecyclerView() {
        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext()));
        listeResAdapter = new ListeResAdapter(getContext(),getList());
        recycler_list.setAdapter(listeResAdapter);
    }
    private List<ListReservationModel> getList(){
        List<ListReservationModel> list_res = new ArrayList<>();
        try {
            connectDB connectDB = new connectDB();
            Connection connect = connectDB.conclass();
            if (connect!=null) {
                String query = "Select IdBorne," +
                        "NumMatricVeh," +
                        "id_user," +
                        "convert (varchar(20),DateRes,111)," +
                        "convert (VARCHAR(5),HeurDRech,8) " +
                        "from reservation ";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                if(rs!=null){
                    while (rs.next()) {
                        try{
                            list_res.add(new ListReservationModel(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
                            Toast.makeText(getContext(), "Affichage des lists de  reservation avec succès ", Toast.LENGTH_LONG).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(getContext(), "Donne Non Trouver ", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getContext(), "Veuillez vérifier votre connexion Internet", Toast.LENGTH_LONG).show();
            }

        }catch(Exception ex){
            Log.e("Error : ", ex.getMessage());
        }


        return list_res;
    }

    public void checkDisplay(){

    }
}