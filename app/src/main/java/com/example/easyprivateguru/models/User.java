package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("alamat")
    private Alamat alamat;
    @Expose
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("role")
    private int role;
    @Expose
    @SerializedName("no_handphone")
    private String noHandphone;
    @Expose
    @SerializedName("tanggal_lahir")
    private String tanggalLahir;
    @Expose
    @SerializedName("jenis_kelamin")
    private String jenisKelamin;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("avatar")
    private String avatar;
    @Expose
    @SerializedName("id")
    private int id;

    public Alamat getAlamat() {
        return alamat;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getRole() {
        return role;
    }

    public String getNoHandphone() {
        return noHandphone;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getId() {
        return id;
    }
}
