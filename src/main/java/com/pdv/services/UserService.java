package com.pdv.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pdv.models.User;
import com.pdv.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> findAll() {
       return userRepository.findAll();
    }

    public List<User> findActive() {
        return userRepository.findByDeletedAtIsNull(); 
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        user.setCreatedBy(userInfoService.getCurrentUser());
        return userRepository.save(user);
    }

    public User update(User user, Long id) {
        User currentUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(encoder.encode(user.getPassword()));
        currentUser.setRole(user.getRole());
        currentUser.setUpdatedBy(userInfoService.getCurrentUser());

        return userRepository.save(currentUser);
    }
    public void delete(User user) {
        user.setDeletedBy(userInfoService.getCurrentUser());
        userRepository.save(user);
    }
}
