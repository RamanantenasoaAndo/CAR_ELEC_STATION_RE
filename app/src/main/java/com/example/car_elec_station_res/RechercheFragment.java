package com.example.car_elec_station_res;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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