package kostify.dao;

import kostify.model.Kamar;
import kostify.model.enums.StatusKamar;

import java.util.List;
import java.util.Optional;

public interface KamarDao extends CrudDao<Kamar> {
    Optional<Kamar> findByNomor(String nomor);

    List<Kamar> findByStatus(StatusKamar status);
}
