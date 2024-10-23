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

import com.pdv.models.User;
import com.pdv.requests.UserRequest;
import com.pdv.services.UserService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('User.all')")
    public Page<User> getAll(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction,
        @RequestParam(required = false) String q
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));

        if (q != null && q.length() > 0) {
            return service.search(q, pageable);
        }

        return service.findAll(pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('User.active')")
    public Page<User> getActive(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size,
        @RequestParam(defaultValue = "id", required = false) String sort,
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
        return service.findActive(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('User.create')")
    public ResponseEntity<User> create(@RequestBody UserRequest userR) {

        User user = new User();
        user.setUsername(userR.getUsername());
        user.setEmail(userR.getEmail());
        user.setPassword(userR.getPassword());
        user.setRole(userR.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('User.update')")
    public ResponseEntity<User> update(@RequestBody UserRequest userR, @PathVariable Long id) {
        
        User user = new User();
        user.setId(id);
        user.setUsername(userR.getUsername());
        user.setEmail(userR.getEmail());
        user.setPassword(userR.getPassword());
        user.setRole(userR.getRole());

        return ResponseEntity.ok(service.update(user, id));
       
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('User.delete')")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return service.findById(id)
                .map(user -> {
                    service.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
