package kostify.model;

import kostify.exception.ValidationException;
import kostify.model.enums.StatusPembayaran;

import java.time.LocalDate;

public class Pembayaran {
    private int id;
    private final Penghuni penghuni; // referensi objek, bukan salinan data
    private final String periode;     // format "yyyy-MM", contoh "2026-11"
    private final long nominal;
    private StatusPembayaran status;
    private LocalDate tanggalBayar;

    /** Tagihan baru (belum dibayar). */
    public Pembayaran(int id, Penghuni penghuni, String periode, long nominal) {
        this(id, penghuni, periode, nominal, StatusPembayaran.BELUM_BAYAR, null);
    }

    /** Dipakai DAO saat membaca data dari database. */
    public Pembayaran(int id, Penghuni penghuni, String periode, long nominal,
                      StatusPembayaran status, LocalDate tanggalBayar) {
        if (penghuni == null) {
            throw new ValidationException("Penghuni harus dipilih");
        }
        if (periode == null || !periode.matches("\\d{4}-\\d{2}")) {
            throw new ValidationException("Periode harus berformat yyyy-MM");
        }
        if (nominal <= 0) {
            throw new ValidationException("Nominal pembayaran harus lebih dari 0");
        }
        this.id = id;
        this.penghuni = penghuni;
        this.periode = periode;
        this.nominal = nominal;
        this.status = status;
        this.tanggalBayar = tanggalBayar;
    }

    /** Sesuai class diagram: method bayar(). */
    public void bayar(LocalDate tanggal) {
        if (status == StatusPembayaran.LUNAS) {
            throw new ValidationException("Pembayaran ini sudah lunas");
        }
        this.tanggalBayar = tanggal;
        this.status = StatusPembayaran.LUNAS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Penghuni getPenghuni() {
        return penghuni;
    }

    public String getPeriode() {
        return periode;
    }

    public long getNominal() {
        return nominal;
    }

    public StatusPembayaran getStatus() {
        return status;
    }

    public LocalDate getTanggalBayar() {
        return tanggalBayar;
    }
}
