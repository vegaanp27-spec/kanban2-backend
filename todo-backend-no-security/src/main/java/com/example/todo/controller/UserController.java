package com.example.todo.controller;

import com.example.todo.dto.UserDtos.CreateUserRequest;
import com.example.todo.dto.UserDtos.UpdateUserRequest;
import com.example.todo.dto.UserDtos.UserResponse;
import com.example.todo.dto.UserDtos.LoginRequest;
import com.example.todo.dto.UserDtos.AuthResponse;
import com.example.todo.entity.User;
import com.example.todo.service.UserService;
import com.example.todo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@CrossOrigin(origins = "*") // o 5173 si usas Vite
@RestController


@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestión de usuarios")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios")
    public ResponseEntity<List<UserResponse>> list() {
        List<UserResponse> out = userService.findAll().stream()
                .map(u -> new UserResponse(u.getId().longValue(), u.getUsername(), u.getEmail(), u.getFullName()))
                .toList();
        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por id")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        User u = userService.findById(id);
        return ResponseEntity.ok(new UserResponse(u.getId().longValue(), u.getUsername(), u.getEmail(), u.getFullName()));
    }


//    @Operation(summary = "Crear usuario")
//    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest req) {
//        User u = new User();
//        u.setUsername(req.getUsername());
//        u.setEmail(req.getEmail());
//        u.setFullName(req.getFullName());
//        // ensure password is hashed when creating via this endpoint
//        User created = userService.register(u, req.getPassword());
//        return ResponseEntity.created(URI.create("/api/users/" + created.getId()))
//                .body(new UserResponse(created.getId().longValue(), created.getUsername(), created.getEmail(), created.getFullName()));
//    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario (registra y devuelve token)")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid CreateUserRequest req) {
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setFullName(req.getFullName());
        User created = userService.register(u, req.getPassword());
        String token = jwtUtil.generateToken(created);
        AuthResponse auth = new AuthResponse(token, new UserResponse(created.getId().longValue(), created.getUsername(), created.getEmail(), created.getFullName()));
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(auth);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        User u = userService.authenticate(req.getEmail(), req.getPassword());
        String token = jwtUtil.generateToken(u);
        AuthResponse auth = new AuthResponse(token, new UserResponse(u.getId().longValue(), u.getUsername(), u.getEmail(), u.getFullName()));
        return ResponseEntity.ok(auth);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest req) {
        User input = new User();
        input.setUsername(req.getUsername());
        input.setEmail(req.getEmail());
        input.setFullName(req.getFullName());
        User u = userService.update(id, input);
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u = userService.updatePassword(id, req.getPassword());
        }
        return ResponseEntity.ok(new UserResponse(u.getId().longValue(), u.getUsername(), u.getEmail(), u.getFullName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
