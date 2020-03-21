package com.example.easyprivateguru.models;

import java.util.Date;

public class Absen {
    private Date tanggalPertemuan;
    private Pemesanan pesanan;
    private boolean status = false;

    public Absen(Date tanggalPertemuan, Pemesanan pesanan) {
        this.tanggalPertemuan = tanggalPertemuan;
        this.pesanan = pesanan;
    }

    public Date getTanggalPertemuan() {
        return tanggalPertemuan;
    }

    public Pemesanan getPesanan() {
        return pesanan;
    }

    public boolean isStatusDone() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
