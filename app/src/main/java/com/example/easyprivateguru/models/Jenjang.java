package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Jenjang {

    @Expose
    @SerializedName("upah_guru_per_pertemuan")
    private int upahGuruPerPertemuan;

    @Expose
    @SerializedName("harga_per_pertemuan")
    private int hargaPerPertemuan;

    @Expose
    @SerializedName("nama_jenjang")
    private String namaJenjang;

    @Expose
    @SerializedName("id_jenjang")
    private int idJenjang;

    public Jenjang(String namaJenjang, int upahGuruPerPertemuan, int hargaPerPertemuan) {
        this.upahGuruPerPertemuan = upahGuruPerPertemuan;
        this.hargaPerPertemuan = hargaPerPertemuan;
        this.namaJenjang = namaJenjang;
    }

    public int getUpahGuruPerPertemuan() {
        return upahGuruPerPertemuan;
    }

    public int getHargaPerPertemuan() {
        return hargaPerPertemuan;
    }

    public String getNamaJenjang() {
        return namaJenjang;
    }

    public int getIdJenjang() {
        return idJenjang;
    }
}
