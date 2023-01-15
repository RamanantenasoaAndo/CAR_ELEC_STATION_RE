package com.example.car_elec_station_res;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Acceuil extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    MapsFragment mapsFragment = new MapsFragment();
    HomeFragment homeFragment =new HomeFragment();
    ProfilFragment profilFragment = new ProfilFragment();
    ParmsFragment parmsFragment = new ParmsFragment();
    RechercheFragment rechercheFragment = new RechercheFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_acceuil);


        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,mapsFragment)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_nav_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,mapsFragment).commit();
                    return  true;
                    case R.id.bottom_nav_params:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,parmsFragment).commit();
                        return true;
                    case R.id.bottom_nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profilFragment).commit();
                        return true;
                    case R.id.bottom_nav_recherche:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,rechercheFragment).commit();
                        return true;

                }
                return false;
            }
        });
    }
}