package com.example.easyprivateguru.models;

public class User {
    private String namaUser, email, alamat;
    private int role;

    public User(String namaUser, String email, String alamat, int role) {
        this.namaUser = namaUser;
        this.email = email;
        this.alamat = alamat;
        this.role = role;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getEmail() {
        return email;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getRole() {
        return role;
    }
}
