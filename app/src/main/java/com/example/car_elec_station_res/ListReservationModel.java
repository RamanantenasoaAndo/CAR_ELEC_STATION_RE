package com.example.car_elec_station_res;

public class ListReservationModel {
    String idBrorne;
    String idVehicul;
    String username;
    String dateR;
    String heureD;


    public ListReservationModel(String idBrorne, String idVehicul, String username, String dateR, String heureD) {
        this.idBrorne = idBrorne;
        this.idVehicul = idVehicul;
        this.username = username;
        this.dateR = dateR;
        this.heureD = heureD;

    }

    public String getIdBrorne() {
        return idBrorne;
    }

    public String getIdVehicul() {
        return idVehicul;
    }

    public String getUsername() {
        return username;
    }

    public String getDateR() {
        return dateR;
    }

    public String getHeureD() {
        return heureD;
    }


}
