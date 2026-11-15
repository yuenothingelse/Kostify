package kostify.model;

import kostify.exception.ValidationException;

import java.time.LocalDate;

public class Penghuni extends Person {
    private String noHP;
    private String alamat;
    private Kamar kamar; // composition: penghuni menempati satu kamar
    private LocalDate tanggalMasuk;
    private LocalDate tanggalKeluar;

    public Penghuni(int id, String nama, String noHP, String alamat, Kamar kamar, LocalDate tanggalMasuk) {
        super(id, nama);
        setNoHP(noHP);
        this.alamat = alamat;
        this.kamar = kamar;
        setTanggalMasuk(tanggalMasuk);
    }

    public String getNoHP() {
        return noHP;
    }

    public final void setNoHP(String noHP) {
        if (noHP == null || !noHP.trim().matches("\\+?[0-9]{8,15}")) {
            throw new ValidationException("Nomor HP harus berisi 8-15 angka");
        }
        this.noHP = noHP.trim();
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Kamar getKamar() {
        return kamar;
    }

    public void setKamar(Kamar kamar) {
        this.kamar = kamar;
    }

    public LocalDate getTanggalMasuk() {
        return tanggalMasuk;
    }

    public final void setTanggalMasuk(LocalDate tanggalMasuk) {
        if (tanggalMasuk == null) {
            throw new ValidationException("Tanggal masuk tidak boleh kosong");
        }
        this.tanggalMasuk = tanggalMasuk;
    }

    public LocalDate getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(LocalDate tanggalKeluar) {
        if (tanggalKeluar != null && tanggalKeluar.isBefore(tanggalMasuk)) {
            throw new ValidationException("Tanggal keluar tidak boleh sebelum tanggal masuk");
        }
        this.tanggalKeluar = tanggalKeluar;
    }

    public boolean isAktif() {
        return tanggalKeluar == null;
    }

    @Override
    public String getPeran() {
        return "Penghuni";
    }
}
