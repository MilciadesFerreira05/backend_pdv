package com.pdv.models;


import java.security.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private Integer createdId;

 
    // @Column(name = "created_at", nullable = false)
    // private Timestamp createdAt;

 
    // @Column(name = "updated_at", nullable = true)
    // private Timestamp updatedAt;

    @Column(name = "isadmin")
    private boolean isAdmin;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    // public Timestamp getCreatedAt() {
    //     return createdAt;
    // }

    // public void setCreatedAt(Timestamp createdAt) {
    //     this.createdAt = createdAt;
    // }

    // public Timestamp getUpdatedAt() {
    //     return updatedAt;
    // }

    // public void setUpdatedAt(Timestamp updatedAt) {
    //     this.updatedAt = updatedAt;
    // }

    


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}

