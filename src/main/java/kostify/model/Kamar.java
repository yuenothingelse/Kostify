package kostify.model;

import kostify.exception.ValidationException;
import kostify.model.enums.StatusKamar;
import kostify.model.enums.TipeKamar;

public class Kamar {
    private int id;
    private String nomor;
    private TipeKamar tipe;
    private long harga;
    private StatusKamar status;

    public Kamar(int id, String nomor, TipeKamar tipe, long harga, StatusKamar status) {
        this.id = id;
        setNomor(nomor);
        setTipe(tipe);
        setHarga(harga);
        setStatus(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomor() {
        return nomor;
    }

    public final void setNomor(String nomor) {
        if (nomor == null || nomor.isBlank()) {
            throw new ValidationException("Nomor kamar tidak boleh kosong");
        }
        this.nomor = nomor.trim().toUpperCase();
    }

    public TipeKamar getTipe() {
        return tipe;
    }

    public final void setTipe(TipeKamar tipe) {
        if (tipe == null) {
            throw new ValidationException("Tipe kamar harus dipilih");
        }
        this.tipe = tipe;
    }

    public long getHarga() {
        return harga;
    }

    public final void setHarga(long harga) {
        if (harga <= 0) {
            throw new ValidationException("Harga kamar harus lebih dari 0");
        }
        this.harga = harga;
    }

    public StatusKamar getStatus() {
        return status;
    }

    public final void setStatus(StatusKamar status) {
        if (status == null) {
            throw new ValidationException("Status kamar tidak boleh kosong");
        }
        this.status = status;
    }

    public boolean isKosong() {
        return status == StatusKamar.KOSONG;
    }
}
