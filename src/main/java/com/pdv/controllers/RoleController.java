package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Role;
import com.pdv.services.RoleService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/roles")
@Validated
public class RoleController {


    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Role.all')")
    public Page<Role> getAllRoles(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return roleService.findAll(pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Role.active')")
    public Page<Role> getActiveRoles(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return roleService.findActive(pageable);
    }

    // Obtener un rol por id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Role.read')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo rol
    @PostMapping
    @PreAuthorize("hasAuthority('Role.create')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role createdRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    // Actualizar un rol existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Role.update')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.update(role, id));
    }

    // Eliminar un rol
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Role.delete')")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
        return roleService.findById(id)
                .map(role -> {
                    roleService.delete(role);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
