package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.*;
import com.onixx.apolloveiculos.api.Infra.Security.TokenService;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;
import com.onixx.apolloveiculos.api.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(value = "/auth")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserService userService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserDTO userDTO) {
        var user = new UsernamePasswordAuthenticationToken(userDTO.name(), userDTO.password());
        var auth = this.authenticationManager.authenticate(user);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new UserResponseDTO(token));
    }
//
//    @PostMapping("/register")
//    public ResponseEntity<User> register(@Valid @RequestBody User user) {
//        User createdUser = userService.createUser(user);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }

    @PostMapping("/register")
    public ResponseEntity<ResponseAnyDTO> register(@Valid @RequestBody UserRegisterDTO user) {
        if (repository.findByEmail(user.email()) != null || repository.findByName(user.name()) != null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseAnyDTO(400, "usuário já cadastrado com esse nome ou email", null,Collections.emptyList()));
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        UserRoles role = user.role() != null ? user.role() : UserRoles.ROLE_USER;
        User newUser = new User(user.name(), user.email(), encryptedPassword, role);
        repository.save(newUser);
        return ResponseEntity.ok().body(new ResponseAnyDTO(200, null, "usuário cadastrado com sucesso", Collections.emptyList()));
    }
}