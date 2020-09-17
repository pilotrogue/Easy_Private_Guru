package com.easyprivate.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TanggalPengganti {

    @Expose
    @SerializedName("tanggal_pengganti")
    private String tanggalPengganti;
    @Expose
    @SerializedName("id_jadwal_perminggu")
    private int idJadwalPerminggu;

    public String getTanggalPengganti() {
        return tanggalPengganti;
    }

    public int getIdJadwalPerminggu() {
        return idJadwalPerminggu;
    }
}
