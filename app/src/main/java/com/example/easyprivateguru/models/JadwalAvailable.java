package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalAvailable {

    @Expose
    @SerializedName("available")
    private int available;
    @Expose
    @SerializedName("end")
    private String end;
    @Expose
    @SerializedName("start")
    private String start;
    @Expose
    @SerializedName("hari")
    private String hari;
    @Expose
    @SerializedName("id_user")
    private int idUser;
    @Expose
    @SerializedName("id_jadwal_available")
    private int idJadwalAvailable;

    public int getAvailable() {
        return available;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public String getHari() {
        return hari;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdJadwalAvailable() {
        return idJadwalAvailable;
    }
}
