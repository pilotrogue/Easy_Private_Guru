package com.example.easyprivateguru.models;

public class Jenjang {
    private String namaJenjang;
    private int upahGuru, potongan;

    public Jenjang(String namaJenjang, int upahGuru, int potongan) {
        this.namaJenjang = namaJenjang;
        this.upahGuru = upahGuru;
        this.potongan = potongan;
    }

    public String getNamaJenjang() {
        return namaJenjang;
    }

    public int getUpahGuru() {
        return upahGuru;
    }

    public int getPotongan() {
        return potongan;
    }
}
