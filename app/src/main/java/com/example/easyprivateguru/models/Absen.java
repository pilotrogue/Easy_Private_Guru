package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Absen {
    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("id_absen")
    private int idAbsen;
    @Expose
    @SerializedName("jadwal_ajar")
    private JadwalAjar jadwalAjar;
    @Expose
    @SerializedName("waktu_absen")
    private String waktuAbsen;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;
    @Expose
    @SerializedName("id_jadwal_ajar")
    private int idJadwalAjar;

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public int getIdAbsen() {
        return idAbsen;
    }

    public JadwalAjar getJadwalAjar() {
        return jadwalAjar;
    }

    public String getWaktuAbsen() {
        return waktuAbsen;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public int getIdJadwalAjar() {
        return idJadwalAjar;
    }

//    private Date tanggalPertemuan;
//    private Pemesanan pesanan;
//    private boolean status = false;
//
    public Absen(String tanggalPertemuan, Pemesanan pesanan) {
        this.waktuAbsen = tanggalPertemuan;
        this.pemesanan = pesanan;
    }
//
//    public Date getTanggalPertemuan() {
//        return tanggalPertemuan;
//    }
//
//    public Pemesanan getPesanan() {
//        return pesanan;
//    }
//
//    public boolean isStatusDone() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }


}
