package com.example.car_elec_station_res;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
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
        return  view;
    }

    private void setRecyclerView() {
        recycler_list.setHasFixedSize(true);
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext()));
        listeResAdapter= new ListeResAdapter(getContext(),getList());
        recycler_list.setAdapter(listeResAdapter);
    }
    private List<ListReservationModel> getList(){

        List<ListReservationModel> list_res = new ArrayList<>();
        list_res.add(new ListReservationModel("001","025","Andofy","11-12-01","11:00"));
        list_res.add(new ListReservationModel("002","4852","Hiaka","11-5-01","13:00"));

        return list_res;
    }
}