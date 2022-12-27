package com.example.ekoperasi.Model;

public class Karyawan {
    private String nik;
    private String nama;
    private String email;
    private String nohp;
    private String alamat;

    public Karyawan(){};
    // constructor class.
    public Karyawan(String nik, String nama, String email, String nohp) {
        this.nik = nik;
        this.nama = nama;
        this.email = email;
        this.nohp = nohp;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
