package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Domains.User.UserDTO;
import com.onixx.apolloveiculos.api.Domains.User.UserRegisterDTO;
import com.onixx.apolloveiculos.api.Domains.User.UserResponseDTO;
import com.onixx.apolloveiculos.api.Services.AuthService;
import com.onixx.apolloveiculos.api.Services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

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
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            authService.logout(user, response);
            return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Logout realizado com sucesso", null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseAnyDTO(401, "Usuário não autenticado", null, null));
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
                            "role", user.getRole())));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseAnyDTO(401, "Usuário não autenticado", null, null));
    }

    @DeleteMapping("/delete/{id}/{password}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable Long id, @PathVariable String password) {
        try {
            userService.deleteUser(id, password);
            return ResponseEntity.ok()
                    .body(new ResponseAnyDTO(200, null, "Usuário deletado com sucesso", Collections.emptyList()));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("Usuário não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseAnyDTO(404, e.getMessage(), null, Collections.emptyList()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, e.getMessage(), null, Collections.emptyList()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Refresh token não fornecido", null, null));
        }
        String newAccessToken = authService.refreshToken(refreshToken, response);
        if (newAccessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Refresh token inválido ou expirado", null, null));
        }
        return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Token atualizado com sucesso", null));
    }
}