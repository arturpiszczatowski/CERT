package com.cryptocurrency.cryptoAPI.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class CrudService<T> {
    protected JpaRepository<T, Long> repository;

    public CrudService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T getById(Long id){
        return repository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        Optional<T> item = repository.findById(id);

        if(item.isPresent()) {
            repository.delete(item.orElseThrow());
        }
    }

    public abstract T createOrUpdate(T updateEntity);
}
