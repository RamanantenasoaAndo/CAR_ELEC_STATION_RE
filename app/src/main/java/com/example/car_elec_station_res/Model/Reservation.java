package com.example.car_elec_station_res.Model;

public class Reservation {

    private String id;
    private String branchement;
    private String dateReservation;
    private String heureD_R;
    private String heureF_R;
    private String matricule;
    private String phone;
    private String status; // Ajout du champ pour le statut

    public Reservation() {
        // Constructeur par défaut requis pour Firebase

    }

    public Reservation(String id, String branchement, String dateReservation, String heureD_R, String heureF_R, String matricule, String phone,String status) {
        this.id = id;
        this.branchement = branchement;
        this.dateReservation = dateReservation;
        this.heureD_R = heureD_R;
        this.heureF_R = heureF_R;
        this.matricule = matricule;
        this.phone = phone;
        this.status=status;
    }

    // Ajoutez les getters et les setters ici

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchement() {
        return branchement;
    }

    public void setBranchement(String branchement) {
        this.branchement = branchement;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getHeureD_R() {
        return heureD_R;
    }

    public void setHeureD_R(String heureD_R) {
        this.heureD_R = heureD_R;
    }

    public String getHeureF_R() {
        return heureF_R;
    }

    public void setHeureF_R(String heureF_R) {
        this.heureF_R = heureF_R;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
