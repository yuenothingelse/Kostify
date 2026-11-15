package kostify.dao;

import kostify.model.Pembayaran;

import java.util.List;

public interface PembayaranDao extends CrudDao<Pembayaran> {
    List<Pembayaran> findByPenghuni(int penghuniId);
}
