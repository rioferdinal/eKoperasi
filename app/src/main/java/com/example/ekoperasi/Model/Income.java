package com.example.ekoperasi.Model;

import java.util.Date;

public class Income {
    private String nik;
    private String nama;
    private String tglpemasukan;
    private int jmlpemasukan;

    public Income() {}

    // constructor class.
    public Income(String nik, String nama, String tglpemasukan, int jmlpemasukan) {
        this.nik = nik;
        this.nama = nama;
        this.tglpemasukan = tglpemasukan;
        this.jmlpemasukan = jmlpemasukan;
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

    public String getTglpemasukan() {
        return tglpemasukan;
    }

    public void setTglpemasukan(String tglpemasukan) {
        this.tglpemasukan = tglpemasukan;
    }

    public int getJmlpemasukan() {
        return jmlpemasukan;
    }

    public void setJmlpemasukan(int jmlpemasukan) {
        this.jmlpemasukan = jmlpemasukan;
    }
}
