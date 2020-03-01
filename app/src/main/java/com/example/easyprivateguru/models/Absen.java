package com.example.easyprivateguru.models;

import java.util.Date;

public class Absen {
    private Date tanggalPertemuan;
    private Pesanan pesanan;
    private boolean status = false;

    public Absen(Date tanggalPertemuan, Pesanan pesanan) {
        this.tanggalPertemuan = tanggalPertemuan;
        this.pesanan = pesanan;
    }

    public Date getTanggalPertemuan() {
        return tanggalPertemuan;
    }

    public Pesanan getPesanan() {
        return pesanan;
    }

    public boolean isStatusDone() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
