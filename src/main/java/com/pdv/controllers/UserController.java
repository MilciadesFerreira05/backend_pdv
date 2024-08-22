package com.pdv.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.pdv.auth.AuthRequest;
import com.pdv.auth.JwtService;
import com.pdv.auth.UserInfoDetails;
import com.pdv.models.Permission;
import com.pdv.models.Role;
import com.pdv.models.RolePermission;
import com.pdv.models.User;
import com.pdv.services.PermissionService;
import com.pdv.services.RolePermissionService;
import com.pdv.services.RoleService;
import com.pdv.services.UserInfoService;
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
    public User create(@RequestBody User user) {
        return service.save(user);
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
