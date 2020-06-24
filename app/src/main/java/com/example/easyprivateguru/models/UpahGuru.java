package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpahGuru {
    @Expose
    @SerializedName("id_upah_guru")
    private int idUpahGuru;

    public int getIdUpahGuru() {
        return idUpahGuru;
    }

    public void setIdUpahGuru(int idUpahGuru) {
        this.idUpahGuru = idUpahGuru;
    }
}
