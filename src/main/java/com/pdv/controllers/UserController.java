package com.pdv.controllers;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getAll() {
        return service.findAll();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('User.active')")
    public List<User> getActive() {
        return service.findAll();
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
