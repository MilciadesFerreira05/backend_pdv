package com.pdv.controllers;


import java.util.List;
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
import com.pdv.services.UserService;

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
        @RequestParam(defaultValue = "ASC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort));
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
        return service.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('User.create')")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }


    @PutMapping
    @PreAuthorize("hasAuthority('User.update')")
    public User update(@RequestBody User user) {
        return service.save(user);
    }


    @DeleteMapping
    @PreAuthorize("hasAuthority('User.delete')")
    public void delete(@RequestBody User user) {
        service.delete(user);
    }



}
