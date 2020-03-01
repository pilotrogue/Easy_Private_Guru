package com.example.easyprivateguru.models;

public class Pesanan {
    private User murid, guru;
    private MataPelajaran mataPelajaran;

    public Pesanan(User murid, User guru, MataPelajaran mataPelajaran) {
        this.murid = murid;
        this.guru = guru;
        this.mataPelajaran = mataPelajaran;
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
