package com.pdv.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdv.models.Role;
import com.pdv.repositories.RoleRepository;

@Service
public class RoleService extends BaseService<Role> {

    @Autowired
    private RoleRepository roleRepository;

    RoleService() {
        this.repository = roleRepository;
    }

  
    public Role update(Role role, Long id) {

        Role currentRole = repository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

        String oldRole = currentRole.toString();

        currentRole.setName(role.getName());
        currentRole.setPermissions(role.getPermissions());
        currentRole.setUpdatedBy(userInfoService.getCurrentUser());

        Role updatedRole = repository.save(currentRole);

        String newRole = updatedRole.toString();

        log("update", newRole, oldRole, userInfoService.getCurrentUser());

        return updatedRole;
    }



}
