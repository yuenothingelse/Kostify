package com.kostify.model;

import java.math.BigDecimal;

public class Kamar {
    private int idKamar;
    private String nomor;
    private String tipe;
    private BigDecimal harga;
    private String status; // "Kosong" | "Terisi"
    private String namaPenghuniAktif; // opsional untuk tampilan daftar kamar

    public Kamar() {}

    public Kamar(int idKamar, String nomor, String tipe, BigDecimal harga, String status) {
        this.idKamar = idKamar;
        this.nomor = nomor;
        this.tipe = tipe;
        this.harga = harga;
        this.status = status;
    }

    public int getIdKamar() { return idKamar; }
    public void setIdKamar(int idKamar) { this.idKamar = idKamar; }

    public String getNomor() { return nomor; }
    public void setNomor(String nomor) { this.nomor = nomor; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNamaPenghuniAktif() { return namaPenghuniAktif; }
    public void setNamaPenghuniAktif(String namaPenghuniAktif) { this.namaPenghuniAktif = namaPenghuniAktif; }
}