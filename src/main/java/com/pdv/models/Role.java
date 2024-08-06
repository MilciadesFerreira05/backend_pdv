package com.pdv.models;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;
  
    // @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    // private Set<RolePermission> rolePermissions;

    @ManyToMany
    @JoinTable(
        name = "role_permissions", 
        joinColumns = @JoinColumn(  name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(  name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions;


      
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + 
                '}';
    }
}
