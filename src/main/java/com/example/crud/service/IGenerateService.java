package com.example.crud.service;

import java.util.Optional;

public interface IGenerateService<E> {
    Iterable<E> findAll();

    E save(E e);

    void delete(Long id);

    Optional<E> findOne(Long id);
}
