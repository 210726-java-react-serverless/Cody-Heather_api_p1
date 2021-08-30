package com.revature.ncu.datasources.repositories;

import java.util.List;

/**
 * Generic interface for Repositories.
 * */
public interface CrudRepository<T> {

    List<T> findAll();
    T findById(String id);
    T save(T newResource);
    boolean update(T updatedResource);
    boolean deleteById(String id);

}
