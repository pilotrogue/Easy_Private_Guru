package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Absen {
    @Expose
    @SerializedName("jadwal_pengganti")
    private JadwalPengganti jadwalPengganti;
    @Expose
    @SerializedName("jadwal_pemesanan_perminggu")
    private JadwalPemesananPerminggu jadwalPemesananPerminggu;
    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("id_absen")
    private int idAbsen;
    @Expose
    @SerializedName("waktu_absen")
    private String waktuAbsen;
    @Expose
    @SerializedName("id_jadwal_pengganti")
    private int idJadwalPengganti;
    @Expose
    @SerializedName("id_jadwal_pemesanan_perminggu")
    private int idJadwalPemesananPerminggu;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;

    public JadwalPengganti getJadwalPengganti() {
        return jadwalPengganti;
    }

    public void setJadwalPengganti(JadwalPengganti jadwalPengganti) {
        this.jadwalPengganti = jadwalPengganti;
    }

    public int getIdJadwalPengganti() {
        return idJadwalPengganti;
    }

    public void setIdJadwalPengganti(int idJadwalPengganti) {
        this.idJadwalPengganti = idJadwalPengganti;
    }

    public JadwalPemesananPerminggu getJadwalPemesananPerminggu() {
        return jadwalPemesananPerminggu;
    }

    public void setJadwalPemesananPerminggu(JadwalPemesananPerminggu jadwalPemesananPerminggu) {
        this.jadwalPemesananPerminggu = jadwalPemesananPerminggu;
    }

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public void setPemesanan(Pemesanan pemesanan) {
        this.pemesanan = pemesanan;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public int getIdAbsen() {
        return idAbsen;
    }

    public void setIdAbsen(int idAbsen) {
        this.idAbsen = idAbsen;
    }

    public String getWaktuAbsen() {
        return waktuAbsen;
    }

    public void setWaktuAbsen(String waktuAbsen) {
        this.waktuAbsen = waktuAbsen;
    }

    public int getIdJadwalPemesananPerminggu() {
        return idJadwalPemesananPerminggu;
    }

    public void setIdJadwalPemesananPerminggu(int idJadwalPemesananPerminggu) {
        this.idJadwalPemesananPerminggu = idJadwalPemesananPerminggu;
    }
}
