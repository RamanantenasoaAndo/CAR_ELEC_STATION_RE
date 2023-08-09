package com.example.car_elec_station_res.Model;

import com.google.android.gms.maps.model.LatLng;

public class Borne {
    private int idBorne;
    private String typesPrise;
    private String titre;
    private LatLng position;

    public int getIdBorne() {
        return idBorne;
    }

    public void setIdBorne(int idBorne) {
        this.idBorne = idBorne;
    }

    public String getTypesPrise() {
        return typesPrise;
    }

    public void setTypesPrise(String typesPrise) {
        this.typesPrise = typesPrise;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}

