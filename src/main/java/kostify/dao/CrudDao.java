package kostify.dao;

import java.util.List;
import java.util.Optional;

/** Kontrak CRUD generik yang dipakai semua DAO (abstraction + generics). */
public interface CrudDao<T> {
    List<T> findAll();

    Optional<T> findById(int id);

    void insert(T entity);

    void update(T entity);

    void delete(int id);
}
