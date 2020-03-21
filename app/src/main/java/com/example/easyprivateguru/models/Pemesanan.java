package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pemesanan {
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;

    @Expose
    @SerializedName("id_guru")
    private int idGuru;

    @Expose
    @SerializedName("id_murid")
    private int idMurid;

    @Expose
    @SerializedName("id_mapel")
    private int idMapel;

    @Expose
    @SerializedName("waktu_pemesanan")
    private String waktuPemesanan;

    @Expose
    @SerializedName("status")
    private int status;

    @Expose
    @SerializedName("jumlah_pertemuan")
    private int jumlahPertemuan;

    @Expose
    @SerializedName("murid")
    private User murid;

    @Expose
    @SerializedName("guru")
    private User guru;

    @Expose
    @SerializedName("mata_pelajaran")
    private MataPelajaran mataPelajaran;

    public Pemesanan(User murid, User guru, MataPelajaran mataPelajaran) {
        this.murid = murid;
        this.guru = guru;
        this.mataPelajaran = mataPelajaran;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public int getIdGuru() {
        return idGuru;
    }

    public int getIdMurid() {
        return idMurid;
    }

    public String getWaktuPemesanan() {
        return waktuPemesanan;
    }

    public int getStatus() {
        return status;
    }

    public int getJumlahPertemuan() {
        return jumlahPertemuan;
    }

    public User getMurid() {
        return murid;
    }

    public User getGuru() {
        return guru;
    }

    public MataPelajaran getMataPelajaran() {
        return mataPelajaran;
    }
}
