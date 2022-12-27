package com.example.ekoperasi.Model;

public class User {

    private int id;
    private String username, email, nik;

    public User() {
    }

    public User(int id, String username, String email, String nik) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nik = nik;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }
}
