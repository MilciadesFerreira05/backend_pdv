package com.pdv.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Role;
import com.pdv.repositories.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserInfoService userInfoService;

    public List<Role> findAll() {
       return roleRepository.findAll();
    }

    public List<Role> findActive() {
        return roleRepository.findByDeletedAtIsNull(); 
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Role save(Role role) {
        role.setCreatedBy(userInfoService.getCurrentUser());
        return roleRepository.save(role);
    }

    public Role update(Role role, Long id) {
        Role currentRole = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        currentRole.setName(role.getName());
        currentRole.setUpdatedBy(userInfoService.getCurrentUser());
        return roleRepository.save(currentRole);
    }

    public void delete(Role role) {
        role.setDeletedBy( userInfoService.getCurrentUser());
        roleRepository.save(role);
    }

}
