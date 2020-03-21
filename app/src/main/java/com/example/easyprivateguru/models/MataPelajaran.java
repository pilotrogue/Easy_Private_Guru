package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MataPelajaran {

    @Expose
    @SerializedName("jenjang")
    private Jenjang jenjang;

    @Expose
    @SerializedName("nama_mapel")
    private String namaMapel;

    @Expose
    @SerializedName("id_jenjang")
    private int idJenjang;

    @Expose
    @SerializedName("id_mapel")
    private int idMapel;

    public MataPelajaran(String namaMapel, Jenjang jenjang) {
        this.jenjang = jenjang;
        this.namaMapel = namaMapel;
    }

    public Jenjang getJenjang(){
        return jenjang;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public int getIdJenjang() {
        return idJenjang;
    }

    public int getIdMapel() {
        return idMapel;
    }
}
