package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pdv.services.BaseService;

public abstract class BaseController<T> {

    @Autowired
    protected BaseService<T> service;

    @GetMapping
    @PreAuthorize("hasAuthority('#entityName.active')")
    public ResponseEntity<Page<T>> getActive(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return ResponseEntity.ok(service.findActive(pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('#entityName.all')")
    public ResponseEntity<Page<T>> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('#entityName.read')")
    public ResponseEntity<T> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('#entityName.create')")
    public ResponseEntity<T> create(@RequestBody T entity) {
        T createdEntity = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('#entityName.update')")
    public ResponseEntity<T> update(@PathVariable Long id, @RequestBody T entity) {
        T updatedEntity = service.update(entity, id);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('#entityName.delete')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        return service.findById(id)
        .map(t -> {
            service.delete(t);
            return ResponseEntity.noContent().build();
        })
        .orElse(ResponseEntity.notFound().build());
    }

    protected abstract String getEntityName();
}
