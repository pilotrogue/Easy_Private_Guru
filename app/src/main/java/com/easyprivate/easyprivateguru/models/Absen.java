package com.easyprivate.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Absen {

    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("jadwal_pengganti")
    private JadwalPengganti jadwalPengganti;
    @Expose
    @SerializedName("waktu_absen")
    private String waktuAbsen;
    @Expose
    @SerializedName("id_jadwal_pengganti")
    private int idJadwalPengganti;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;
    @Expose
    @SerializedName("id_absen")
    private int idAbsen;

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public JadwalPengganti getJadwalPengganti() {
        return jadwalPengganti;
    }

    public String getWaktuAbsen() {
        return waktuAbsen;
    }

    public int getIdJadwalPengganti() {
        return idJadwalPengganti;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public int getIdAbsen() {
        return idAbsen;
    }
}
