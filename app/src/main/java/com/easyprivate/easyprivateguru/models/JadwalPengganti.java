package com.easyprivate.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalPengganti {

    @Expose
    @SerializedName("waktu_pengganti")
    private String waktuPengganti;
    @Expose
    @SerializedName("id_jadwal_pemesanan_perminggu")
    private int idJadwalPemesananPerminggu;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;
    @Expose
    @SerializedName("id")
    private int id;

    public String getWaktuPengganti() {
        return waktuPengganti;
    }

    public int getIdJadwalPemesananPerminggu() {
        return idJadwalPemesananPerminggu;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public int getId() {
        return id;
    }
}
