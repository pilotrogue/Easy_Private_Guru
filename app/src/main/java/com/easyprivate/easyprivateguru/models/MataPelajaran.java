package com.easyprivate.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MataPelajaran{

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

    public Jenjang getJenjang() {
        return jenjang;
    }

    public void setJenjang(Jenjang jenjang) {
        this.jenjang = jenjang;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }

    public int getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(int idJenjang) {
        this.idJenjang = idJenjang;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }
}
