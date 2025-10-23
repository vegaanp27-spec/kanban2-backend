package com.example.todo.service;

import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() { return userRepository.findAll(); }
    public User findById(Long id) { return userRepository.findById(id).orElseThrow(); }

    @Transactional
    public User create(User u) {
        if (userRepository.existsByUsername(u.getUsername())) throw new IllegalArgumentException("username ya existe");
        if (userRepository.existsByEmail(u.getEmail())) throw new IllegalArgumentException("email ya existe");
        return userRepository.save(u);
    }

    @Transactional
    public User register(User u, String rawPassword) {
        if (userRepository.existsByUsername(u.getUsername())) throw new IllegalArgumentException("username ya existe");
        if (userRepository.existsByEmail(u.getEmail())) throw new IllegalArgumentException("email ya existe");
        String hash = passwordEncoder.encode(rawPassword);
        u.setPasswordHash(hash);
        return userRepository.save(u);
    }

    public User authenticate(String email, String rawPassword) {
        User u = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("credenciales inválidas"));
        if (!passwordEncoder.matches(rawPassword, u.getPasswordHash())) throw new IllegalArgumentException("credenciales inválidas");
        return u;
    }

    @Transactional
    public User update(Long id, User input) {
        User u = findById(id);
        if (input.getUsername() != null) u.setUsername(input.getUsername());
        if (input.getEmail() != null) u.setEmail(input.getEmail());
        if (input.getFullName() != null) u.setFullName(input.getFullName());
        // password updates must go through updatePassword to ensure hashing
        return u;
    }

    @Transactional
    public User updatePassword(Long id, String rawPassword) {
        User u = findById(id);
        String hash = passwordEncoder.encode(rawPassword);
        u.setPasswordHash(hash);
        return u;
    }

    @Transactional
    public void delete(Long id) { userRepository.deleteById(id); }
}
