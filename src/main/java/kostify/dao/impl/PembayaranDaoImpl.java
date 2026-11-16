package kostify.dao.impl;

import kostify.dao.PembayaranDao;
import kostify.model.Pembayaran;

import java.util.List;
import java.util.Optional;

/** TODO (anggota yang memegang modul Pembayaran): implementasikan dengan JDBC, ikuti KamarDaoImpl. */
public class PembayaranDaoImpl implements PembayaranDao {

    private static UnsupportedOperationException todo() {
        return new UnsupportedOperationException("TODO: implementasikan query JDBC (lihat KamarDaoImpl)");
    }

    @Override
    public List<Pembayaran> findAll() {
        throw todo();
    }

    @Override
    public Optional<Pembayaran> findById(int id) {
        throw todo();
    }

    @Override
    public void insert(Pembayaran pembayaran) {
        throw todo();
    }

    @Override
    public void update(Pembayaran pembayaran) {
        throw todo();
    }

    @Override
    public void delete(int id) {
        throw todo();
    }

    @Override
    public List<Pembayaran> findByPenghuni(int penghuniId) {
        throw todo();
    }
}
