package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbsenPembayaran {

    @Expose
    @SerializedName("pembayaran")
    private Pembayaran pembayaran;
    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("guru")
    private User guru;
    @Expose
    @SerializedName("murid")
    private User murid;
    @Expose
    @SerializedName("id_pembayaran")
    private Integer idPembayaran;
    @Expose
    @SerializedName("bulan")
    private Integer bulan;
    @Expose
    @SerializedName("tahun")
    private Integer tahun;
    @Expose
    @SerializedName("jumlah_absen")
    private Integer jumlahAbsen;
    @Expose
    @SerializedName("id_guru")
    private Integer idGuru;
    @Expose
    @SerializedName("id_pemesanan")
    private Integer idPemesanan;
    @Expose
    @SerializedName("id_murid")
    private Integer idMurid;

    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public void setPemesanan(Pemesanan pemesanan) {
        this.pemesanan = pemesanan;
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

    public Integer getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(Integer idPembayaran) {
        this.idPembayaran = idPembayaran;
    }

    public Integer getBulan() {
        return bulan;
    }

    public void setBulan(Integer bulan) {
        this.bulan = bulan;
    }

    public Integer getTahun() {
        return tahun;
    }

    public void setTahun(Integer tahun) {
        this.tahun = tahun;
    }

    public Integer getJumlahAbsen() {
        return jumlahAbsen;
    }

    public void setJumlahAbsen(Integer jumlahAbsen) {
        this.jumlahAbsen = jumlahAbsen;
    }

    public Integer getIdGuru() {
        return idGuru;
    }

    public void setIdGuru(Integer idGuru) {
        this.idGuru = idGuru;
    }

    public Integer getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(Integer idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public Integer getIdMurid() {
        return idMurid;
    }

    public void setIdMurid(Integer idMurid) {
        this.idMurid = idMurid;
    }
}
