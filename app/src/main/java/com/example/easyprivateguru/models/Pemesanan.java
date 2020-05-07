package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pemesanan {

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
    @SerializedName("jumlah_pertemuan")
    private int jumlahPertemuan;
    @Expose
    @SerializedName("status")
    private int status;
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

    public List<JadwalPemesananPerminggu> getJadwalPemesananPerminggu() {
        return jadwalPemesananPerminggu;
    }

    public MataPelajaran getMataPelajaran() {
        return mataPelajaran;
    }

    public User getGuru() {
        return guru;
    }

    public User getMurid() {
        return murid;
    }

    public int getJumlahPertemuan() {
        return jumlahPertemuan;
    }

    public int getStatus() {
        return status;
    }

    public String getWaktuPemesanan() {
        return waktuPemesanan;
    }

    public int getKelas() {
        return kelas;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public int getIdMurid() {
        return idMurid;
    }

    public int getIdGuru() {
        return idGuru;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }
}
