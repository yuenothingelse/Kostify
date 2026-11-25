package com.kostify.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pembayaran {
    private int idPembayaran;
    private String kodePembayaran;
    private Integer idPenghuni;
    private String penghuni;
    private String noHp;
    private String noKamar;
    private String periode;
    private LocalDate tanggalJatuhTempo;
    private LocalDate tanggalBayar;
    private BigDecimal nominal;
    private String status; // "Lunas" | "Belum Lunas" | "Jatuh Tempo"

    public Pembayaran() {}

    public void bayar(LocalDate tanggalBayar) {
        this.status = "Lunas";
        this.tanggalBayar = tanggalBayar != null ? tanggalBayar : LocalDate.now();
    }

    public int getIdPembayaran() { return idPembayaran; }
    public void setIdPembayaran(int idPembayaran) { this.idPembayaran = idPembayaran; }

    public String getKodePembayaran() { return kodePembayaran; }
    public void setKodePembayaran(String kodePembayaran) { this.kodePembayaran = kodePembayaran; }

    public Integer getIdPenghuni() { return idPenghuni; }
    public void setIdPenghuni(Integer idPenghuni) { this.idPenghuni = idPenghuni; }

    public String getPenghuni() { return penghuni; }
    public void setPenghuni(String penghuni) { this.penghuni = penghuni; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getNoKamar() { return noKamar; }
    public void setNoKamar(String noKamar) { this.noKamar = noKamar; }

    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }

    public LocalDate getTanggalJatuhTempo() { return tanggalJatuhTempo; }
    public void setTanggalJatuhTempo(LocalDate tanggalJatuhTempo) { this.tanggalJatuhTempo = tanggalJatuhTempo; }

    public LocalDate getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(LocalDate tanggalBayar) { this.tanggalBayar = tanggalBayar; }

    public BigDecimal getNominal() { return nominal; }
    public void setNominal(BigDecimal nominal) { this.nominal = nominal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}