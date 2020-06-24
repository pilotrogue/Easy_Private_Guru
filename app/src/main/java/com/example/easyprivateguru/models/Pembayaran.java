package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pembayaran {
    @Expose
    @SerializedName("id_pembayaran")
    private int idPembayaran;

    public int getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(int idPembayaran) {
        this.idPembayaran = idPembayaran;
    }
}
