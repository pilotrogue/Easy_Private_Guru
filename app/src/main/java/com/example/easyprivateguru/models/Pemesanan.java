package com.example.easyprivateguru.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pemesanan{

    @Expose
    @SerializedName("jadwal_pemesanan_perminggu")
    private ArrayList<JadwalPemesananPerminggu> jadwalPemesananPerminggu;
    @Expose
    @SerializedName("mata_pelajaran")
    private MataPelajaran mataPelajaran;
    @Expose
    @SerializedName("guru")
    private User guru;
    @Expose
    @SerializedName("murid")
    private User murid;
    @Expose
    @SerializedName("status")
    private int status;
    @Expose
    @SerializedName("first_meet")
    private String firstMeet;
    @Expose
    @SerializedName("waktu_pemesanan")
    private String waktuPemesanan;
    @Expose
    @SerializedName("kelas")
    private int kelas;
    @Expose
    @SerializedName("id_mapel")
    private int idMapel;
    @Expose
    @SerializedName("id_murid")
    private int idMurid;
    @Expose
    @SerializedName("id_guru")
    private int idGuru;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;

    public ArrayList<JadwalPemesananPerminggu> getJadwalPemesananPerminggu() {
        return jadwalPemesananPerminggu;
    }

    public void setJadwalPemesananPerminggu(ArrayList<JadwalPemesananPerminggu> jadwalPemesananPerminggu) {
        this.jadwalPemesananPerminggu = jadwalPemesananPerminggu;
    }

    public MataPelajaran getMataPelajaran() {
        return mataPelajaran;
    }

    public void setMataPelajaran(MataPelajaran mataPelajaran) {
        this.mataPelajaran = mataPelajaran;
    }

    public User getGuru() {
        return guru;
    }

    public void setGuru(User guru) {
        this.guru = guru;
    }

    public User getMurid() {
        return murid;
    }

    public void setMurid(User murid) {
        this.murid = murid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFirstMeet() {
        return firstMeet;
    }

    public void setFirstMeet(String firstMeet) {
        this.firstMeet = firstMeet;
    }

    public String getWaktuPemesanan() {
        return waktuPemesanan;
    }

    public void setWaktuPemesanan(String waktuPemesanan) {
        this.waktuPemesanan = waktuPemesanan;
    }

    public int getKelas() {
        return kelas;
    }

    public void setKelas(int kelas) {
        this.kelas = kelas;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }

    public int getIdMurid() {
        return idMurid;
    }

    public void setIdMurid(int idMurid) {
        this.idMurid = idMurid;
    }

    public int getIdGuru() {
        return idGuru;
    }

    public void setIdGuru(int idGuru) {
        this.idGuru = idGuru;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }
}
