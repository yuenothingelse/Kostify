package kostify.controller;

import kostify.exception.DataAccessException;
import kostify.exception.DataNotFoundException;
import kostify.exception.ValidationException;
import kostify.model.Kamar;
import kostify.service.KamarService;
import kostify.view.KamarPanel;

/** Contoh controller lengkap. Pola: pasang listener di constructor, panggil service, tampilkan hasil. */
public class KamarController {
    private final KamarPanel view;
    private final KamarService service;

    public KamarController(KamarPanel view, KamarService service) {
        this.view = view;
        this.service = service;
        view.onTambah(e -> tambah());
        view.onUbah(e -> ubah());
        view.onHapus(e -> hapus());
        refresh();
    }

    public void refresh() {
        try {
            view.setData(service.semuaKamar());
        } catch (DataAccessException e) {
            view.showError("Gagal memuat data kamar");
        }
    }

    private void tambah() {
        Kamar baru = view.showForm(null);
        if (baru == null) {
            return;
        }
        try {
            service.tambah(baru);
            view.showInfo("Kamar berhasil ditambahkan");
            refresh();
        } catch (ValidationException | DataAccessException e) {
            view.showError(e.getMessage());
        }
    }

    private void ubah() {
        int id = view.getSelectedId();
        if (id < 0) {
            view.showError("Pilih kamar yang ingin diubah");
            return;
        }
        try {
            Kamar baru = view.showForm(service.cari(id));
            if (baru == null) {
                return;
            }
            baru.setId(id);
            service.ubah(baru);
            view.showInfo("Kamar berhasil diubah");
            refresh();
        } catch (ValidationException | DataNotFoundException | DataAccessException e) {
            view.showError(e.getMessage());
        }
    }

    private void hapus() {
        int id = view.getSelectedId();
        if (id < 0) {
            view.showError("Pilih kamar yang ingin dihapus");
            return;
        }
        if (!view.confirm("Yakin ingin menghapus kamar ini?")) {
            return;
        }
        try {
            service.hapus(id);
            view.showInfo("Kamar berhasil dihapus");
            refresh();
        } catch (ValidationException | DataNotFoundException | DataAccessException e) {
            view.showError(e.getMessage());
        }
    }
}
