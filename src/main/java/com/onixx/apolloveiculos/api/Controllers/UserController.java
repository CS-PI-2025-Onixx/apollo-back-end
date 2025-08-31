package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.*;
import com.onixx.apolloveiculos.api.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onixx.apolloveiculos.api.Services.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO, HttpServletResponse response) {
        try {
            UserResponseDTO responseDTO = authService.authenticate(userDTO, response);
            return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Login realizado com sucesso", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Credenciais inválidas", null, null));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addHeader("Set-Cookie",
                "authToken=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");

        return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Logout realizado com sucesso", null));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseAnyDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        ResponseAnyDTO response = authService.register(userRegisterDTO);

        if (response.status() == 422) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Usuário autenticado",
                    Map.of(
                            "id", user.getId_user(),
                            "name", user.getName(),
                            "role", user.getRole()
                    )));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseAnyDTO(401, "Usuário não autenticado", null, null));
    }

    @DeleteMapping("/delete/{id}/{password}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable Long id, @PathVariable String password) {
        try {
            userService.deleteUser(id, password);
            return ResponseEntity.ok().body(new ResponseAnyDTO(200, null, "Usuário deletado com sucesso", Collections.emptyList()));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("Usuário não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAnyDTO(404, e.getMessage(), null, Collections.emptyList()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, e.getMessage(), null, Collections.emptyList()));
        }
    }
}