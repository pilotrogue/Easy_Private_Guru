package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalAjar {
    @Expose
    @SerializedName("id_jadwal_ajar")
    private int idJadwalAjar;
    @Expose
    @SerializedName("waktu_ajar")
    private String waktuAjar;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;

    public int getIdJadwalAjar() {
        return idJadwalAjar;
    }

    public String getWaktuAjar() {
        return waktuAjar;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }
//    private String eventId, eventTitle, eventDescription, eventLocation;
//
    public JadwalAjar(int eventId) {
        this.idJadwalAjar = eventId;
    }
//
//    public String getEventId() {
//        return eventId;
//    }
//
//    public String getEventTitle() {
//        return eventTitle;
//    }
//
//    public String getEventDescription() {
//        return eventDescription;
//    }
//
//    public String getEventLocation() {
//        return eventLocation;
//    }


}
