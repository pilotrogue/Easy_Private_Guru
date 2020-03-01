package com.example.easyprivateguru.models;

import java.util.Date;

public class Pembayaran {
    private Date tanggalUpah;
    private int jumlahUpah;
    private boolean status = false;

    public Pembayaran(Date tanggalUpah, int jumlahUpah) {
        this.tanggalUpah = tanggalUpah;
        this.jumlahUpah = jumlahUpah;
    }

    public Date getTanggalUpah() {
        return tanggalUpah;
    }

    public int getJumlahUpah() {
        return jumlahUpah;
    }

    public boolean isStatusDone() {
        return status;
    }
}
