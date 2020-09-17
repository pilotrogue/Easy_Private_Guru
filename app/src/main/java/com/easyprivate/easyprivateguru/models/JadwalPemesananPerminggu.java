package com.easyprivate.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalPemesananPerminggu {

    @Expose
    @SerializedName("pemesanan")
    private Pemesanan pemesanan;
    @Expose
    @SerializedName("jadwal_available")
    private JadwalAvailable jadwalAvailable;
    @Expose
    @SerializedName("id_event")
    private Integer idEvent;
    @Expose
    @SerializedName("id_jadwal_available")
    private int idJadwalAvailable;
    @Expose
    @SerializedName("id_pemesanan")
    private int idPemesanan;
    @Expose
    @SerializedName("id_jadwal_pemesanan_perminggu")
    private int idJadwalPemesananPerminggu;

    public JadwalAvailable getJadwalAvailable() {
        return jadwalAvailable;
    }

    public int getIdJadwalAvailable() {
        return idJadwalAvailable;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public int getIdJadwalPemesananPerminggu() {
        return idJadwalPemesananPerminggu;
    }

    public void setJadwalAvailable(JadwalAvailable jadwalAvailable) {
        this.jadwalAvailable = jadwalAvailable;
    }

    public void setIdJadwalAvailable(int idJadwalAvailable) {
        this.idJadwalAvailable = idJadwalAvailable;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public void setIdJadwalPemesananPerminggu(int idJadwalPemesananPerminggu) {
        this.idJadwalPemesananPerminggu = idJadwalPemesananPerminggu;
    }

    public Pemesanan getPemesanan() {
        return pemesanan;
    }

    public void setPemesanan(Pemesanan pemesanan) {
        this.pemesanan = pemesanan;
    }

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }
}
