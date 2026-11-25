package com.kostify.model;

import java.time.LocalDate;

public class Penghuni {
    private int idPenghuni;
    private String nama;
    private String noHp;
    private String alamat;
    private Integer idKamar;
    private String noKamar;
    private LocalDate tanggalMasuk;
    private LocalDate tanggalKeluar;
    private String statusAktif; // "Aktif" | "Keluar"

    public Penghuni() {}

    public int getIdPenghuni() { return idPenghuni; }
    public void setIdPenghuni(int idPenghuni) { this.idPenghuni = idPenghuni; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public Integer getIdKamar() { return idKamar; }
    public void setIdKamar(Integer idKamar) { this.idKamar = idKamar; }

    public String getNoKamar() { return noKamar; }
    public void setNoKamar(String noKamar) { this.noKamar = noKamar; }

    public LocalDate getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(LocalDate tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public LocalDate getTanggalKeluar() { return tanggalKeluar; }
    public void setTanggalKeluar(LocalDate tanggalKeluar) { this.tanggalKeluar = tanggalKeluar; }

    public String getStatusAktif() { return statusAktif; }
    public void setStatusAktif(String statusAktif) { this.statusAktif = statusAktif; }
}