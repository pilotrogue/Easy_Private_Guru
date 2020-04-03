package com.example.easyprivateguru.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @Expose
    @SerializedName("alamat")
    private List<Alamat> alamat;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("role")
    private int role;
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
    @SerializedName("no_handphone")
    private String noHandphone;
    @Expose
    @SerializedName("avatar")
    private String avatar;

    public List<Alamat> getAlamat() {
        return alamat;
    }

    public int getId() {
        return id;
    }

    public int getRole() {
        return role;
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

    public String getNoHandphone() {
        return noHandphone;
    }

    public String getAvatar() {
        return avatar;
    }

    public User(String name, String email, int role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
