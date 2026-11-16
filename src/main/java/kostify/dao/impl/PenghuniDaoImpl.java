package kostify.dao.impl;

import kostify.dao.PenghuniDao;
import kostify.model.Penghuni;

import java.util.List;
import java.util.Optional;

/**
 * TODO (anggota yang memegang modul Penghuni): implementasikan dengan JDBC,
 * ikuti pola di KamarDaoImpl. Penghuni punya Kamar, jadi ambil kamar lewat
 * KamarDaoImpl.findById(kamar_id) atau JOIN.
 */
public class PenghuniDaoImpl implements PenghuniDao {

    private static UnsupportedOperationException todo() {
        return new UnsupportedOperationException("TODO: implementasikan query JDBC (lihat KamarDaoImpl)");
    }

    @Override
    public List<Penghuni> findAll() {
        throw todo();
    }

    @Override
    public Optional<Penghuni> findById(int id) {
        throw todo();
    }

    @Override
    public void insert(Penghuni penghuni) {
        throw todo();
    }

    @Override
    public void update(Penghuni penghuni) {
        throw todo();
    }

    @Override
    public void delete(int id) {
        throw todo();
    }

    @Override
    public Optional<Penghuni> findByKamarId(int kamarId) {
        throw todo();
    }
}
