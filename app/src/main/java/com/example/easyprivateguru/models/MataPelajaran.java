package com.example.easyprivateguru.models;

public class MataPelajaran {
    private String namaMataPelajaran;
    private Jenjang jenjang;

    public MataPelajaran(String namaMataPelajaran, Jenjang jenjang) {
        this.namaMataPelajaran = namaMataPelajaran;
        this.jenjang = jenjang;
    }

    public String getNamaMataPelajaran() {
        return namaMataPelajaran;
    }

    public Jenjang getJenjang() {
        return jenjang;
    }
}
