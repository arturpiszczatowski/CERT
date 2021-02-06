package com.cryptocurrency.cryptoAPI.controller;

import com.cryptocurrency.cryptoAPI.service.CrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CrudController<T> {
    protected final CrudService<T> service;

    public CrudController(CrudService<T> service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<Map<String, Object>>> getALl() {
        try {
            List<T> all = service.getAll();
            List<Map<String, Object>> payload = all.stream()
                    .map(obj -> transformToDTO().apply(obj))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(payload, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable("id") Long id) {
        try {
            T obj = service.getById(id);
            Map<String, Object> payload = transformToDTO().apply(obj);
            return new ResponseEntity<>(payload, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody T t) {
        try {
            service.createOrUpdate(t);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public abstract Function<T, Map<String, Object>> transformToDTO();
}

