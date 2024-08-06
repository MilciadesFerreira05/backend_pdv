package com.pdv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    // private Set<RolePermission> rolePermissions;

    // @ManyToMany(mappedBy = "permissions")
    // private Set<Role> roles;
    

    // Getters y Setters
}

