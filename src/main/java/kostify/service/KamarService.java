package kostify.service;

import kostify.dao.KamarDao;
import kostify.exception.DataNotFoundException;
import kostify.exception.ValidationException;
import kostify.model.Kamar;
import kostify.model.enums.StatusKamar;

import java.util.List;

public class KamarService {
    private final KamarDao dao;

    public KamarService(KamarDao dao) {
        this.dao = dao;
    }

    public List<Kamar> semuaKamar() {
        return dao.findAll();
    }

    public Kamar cari(int id) {
        return dao.findById(id).orElseThrow(() -> new DataNotFoundException("Data kamar tidak ditemukan"));
    }

    public void tambah(Kamar kamar) {
        dao.findByNomor(kamar.getNomor()).ifPresent(x -> {
            throw new ValidationException("Nomor kamar " + kamar.getNomor() + " sudah digunakan");
        });
        dao.insert(kamar);
    }

    public void ubah(Kamar kamar) {
        cari(kamar.getId());
        dao.findByNomor(kamar.getNomor())
                .filter(x -> x.getId() != kamar.getId())
                .ifPresent(x -> {
                    throw new ValidationException("Nomor kamar " + kamar.getNomor() + " sudah digunakan");
                });
        dao.update(kamar);
    }

    public void hapus(int id) {
        Kamar kamar = cari(id);
        if (kamar.getStatus() == StatusKamar.TERISI) {
            throw new ValidationException("Kamar yang sedang terisi tidak dapat dihapus");
        }
        dao.delete(id);
    }
}
