package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalAvailable {
    @Expose
    @SerializedName("jadwal_pemesanan_perminggu")
    private JadwalPemesananPerminggu jadwalPemesananPerminggu;
    @Expose
    @SerializedName("available")
    private int available;
    @Expose
    @SerializedName("end")
    private String end;
    @Expose
    @SerializedName("start")
    private String start;
    @Expose
    @SerializedName("hari")
    private String hari;
    @Expose
    @SerializedName("id_user")
    private int idUser;
    @Expose
    @SerializedName("id_jadwal_available")
    private int idJadwalAvailable;
    private int changedAvailability = 99;

    public JadwalPemesananPerminggu getJadwalPemesananPerminggu() {
        return jadwalPemesananPerminggu;
    }

    public int getAvailable() {
        return available;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public String getHari() {
        return hari;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdJadwalAvailable() {
        return idJadwalAvailable;
    }

    public int getChangedAvailability() {
        return changedAvailability;
    }

    public void setJadwalPemesananPerminggu(JadwalPemesananPerminggu jadwalPemesananPerminggu) {
        this.jadwalPemesananPerminggu = jadwalPemesananPerminggu;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setIdJadwalAvailable(int idJadwalAvailable) {
        this.idJadwalAvailable = idJadwalAvailable;
    }

    public void setChangedAvailability(int changedAvailability) {
        this.changedAvailability = changedAvailability;
    }
}
