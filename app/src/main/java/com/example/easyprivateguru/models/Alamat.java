package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alamat {
    @Expose
    @SerializedName("alamat_lengkap")
    private String alamatLengkap;
    @Expose
    @SerializedName("longitude")
    private double longitude;
    @Expose
    @SerializedName("latitude")
    private double latitude;
    @Expose
    @SerializedName("id_user")
    private int idUser;
    @Expose
    @SerializedName("id_alamat")
    private int idAlamat;

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdAlamat() {
        return idAlamat;
    }
}
