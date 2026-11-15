package kostify.dao;

import kostify.model.Penghuni;

import java.util.Optional;

public interface PenghuniDao extends CrudDao<Penghuni> {
    Optional<Penghuni> findByKamarId(int kamarId);
}
