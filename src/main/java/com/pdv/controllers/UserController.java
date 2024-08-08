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
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("addNewUser")
    public ResponseEntity<User> addUser(@RequestBody User userInfo) {
        User user = service.addUser(userInfo);
        return ResponseEntity.ok(user);
    }

    @GetMapping("roles")
    public ResponseEntity<String> getRole() {

        String encoded = encoder.encode("123");
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("admin", "123")
            );
            return ResponseEntity.ok().body(encoded);
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
        
    }


    @GetMapping("user/userProfile")
    @PreAuthorize("hasAuthority('Caja.cobrar')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("admin/adminProfile")
    @PreAuthorize("hasAuthority('Caja.abrir')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("generateToken")
    public ResponseEntity<HashMap<String, Object>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
      
        try {

            System.out.println("authRequest: " + authRequest);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
               
                String username = authentication.getName();
                Long id = ((UserInfoDetails) authentication.getPrincipal()).getId();
                List<String> authorities = ((UserInfoDetails) authentication.getPrincipal()).getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
                String token = jwtService.generateToken(authRequest.getUsername());

                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("username", username);
                map.put("authorities", authorities);
                map.put("token", token);

                return ResponseEntity.ok().body(map);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

}
