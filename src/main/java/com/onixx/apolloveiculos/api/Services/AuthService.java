package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.*;
import com.onixx.apolloveiculos.api.Infra.Security.TokenService;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;
import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO authenticate(UserDTO userDTO, HttpServletResponse response) throws AuthenticationException {
        var authToken = new UsernamePasswordAuthenticationToken(userDTO.name(), userDTO.password());
        var auth = authenticationManager.authenticate(authToken);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        String cookieHeader = String.format(
                "authToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                token,
                24 * 60 * 60
        );

        response.addHeader("Set-Cookie", cookieHeader);

        return new UserResponseDTO(null);
    }

    public ResponseAnyDTO register(UserRegisterDTO userRegisterDTO) {
        if (userExists(userRegisterDTO)) {
            return new ResponseAnyDTO(422, "Usuário já cadastrado com esse nome ou email", null, Collections.emptyList());
        }

        User newUser = createUser(userRegisterDTO);
        userRepository.save(newUser);

        return new ResponseAnyDTO(201, null, "Usuário cadastrado com sucesso", Collections.emptyList());
    }

    private boolean userExists(UserRegisterDTO userRegisterDTO) {
        return userRepository.findByEmail(userRegisterDTO.email()) != null ||
                userRepository.findByName(userRegisterDTO.name()) != null;
    }

    private User createUser(UserRegisterDTO userRegisterDTO) {
        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.password());
        UserRoles role = userRegisterDTO.role() != null ? userRegisterDTO.role() : UserRoles.ROLE_USER;

        return new User(
                userRegisterDTO.name(),
                userRegisterDTO.email(),
                encryptedPassword,
                role,
                userRegisterDTO.fullname(),
                userRegisterDTO.cellphone()
        );
    }
}