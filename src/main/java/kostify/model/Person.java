package kostify.model;

import kostify.exception.ValidationException;

/** Kelas abstrak untuk semua orang di sistem (abstraction + inheritance). */
public abstract class Person {
    private int id;
    private String nama;

    protected Person(int id, String nama) {
        this.id = id;
        setNama(nama);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public final void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new ValidationException("Nama tidak boleh kosong");
        }
        this.nama = nama.trim();
    }

    /** Polimorfisme: tiap subclass menyebutkan perannya sendiri. */
    public abstract String getPeran();
}
