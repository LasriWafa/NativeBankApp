package ma.bankati.dao;

import java.util.List;

public interface CrudDao<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
    void update(T entity);
    void delete(T entity);
    void deleteById(ID id);
} 