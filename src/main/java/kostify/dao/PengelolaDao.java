package kostify.dao;

import kostify.model.Pengelola;

import java.util.Optional;

public interface PengelolaDao {
    Optional<Pengelola> findByEmail(String email);

    void insert(Pengelola pengelola);

    int count();
}
