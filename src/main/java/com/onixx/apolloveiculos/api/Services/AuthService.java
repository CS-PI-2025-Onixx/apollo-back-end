package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.*;
import com.onixx.apolloveiculos.api.Infra.Security.TokenService;
import com.onixx.apolloveiculos.api.Repositories.RefreshTokenRepository;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.Collections;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO authenticate(UserDTO userDTO, HttpServletResponse response) throws AuthenticationException {
        var authToken = new UsernamePasswordAuthenticationToken(userDTO.name(), userDTO.password());
        var auth = authenticationManager.authenticate(authToken);
        User user = (User) auth.getPrincipal();
        var accessToken = tokenService.generateToken(user);
        var refreshTokenStr = tokenService.generateRefreshToken(user);
        // Salvar refresh token no banco
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenStr);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        // Setar cookies
        String accessCookieHeader = String.format(
                "authToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                accessToken,
                24 * 60 * 60);
        String refreshCookieHeader = String.format(
                "refreshToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                refreshTokenStr,
                1 * 60 * 60);
        response.addHeader("Set-Cookie", accessCookieHeader);
        response.addHeader("Set-Cookie", refreshCookieHeader);
        // ALTERAÇÃO: retorna o access token no DTO
        return new UserResponseDTO(accessToken);
    }

    public ResponseAnyDTO register(UserRegisterDTO userRegisterDTO) {
        if (userExists(userRegisterDTO)) {
            return new ResponseAnyDTO(422, "Usuário já cadastrado com esse nome ou email", null,
                    Collections.emptyList());
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
                userRegisterDTO.cellphone());
    }

    public void logout(User user, HttpServletResponse response) {
        // Deletar refresh tokens do usuário
        refreshTokenRepository.deleteByUser(user);
        // Expirar cookies
        response.addHeader("Set-Cookie",
                "authToken=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
        response.addHeader("Set-Cookie",
                "refreshToken=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
    }

    public String refreshToken(String refreshTokenStr, HttpServletResponse response) {
        String username = tokenService.validateRefreshToken(refreshTokenStr);
        if (username.isEmpty()) {
            return null;
        }
        var refreshTokenOpt = refreshTokenRepository.findByToken(refreshTokenStr);
        if (refreshTokenOpt.isEmpty() || refreshTokenOpt.get().getExpiryDate().isBefore(Instant.now())) {
            return null;
        }
        User user = refreshTokenOpt.get().getUser();
        if (!user.getName().equals(username)) {
            return null;
        }
        // Rotação: Deletar velho refresh
        refreshTokenRepository.delete(refreshTokenOpt.get());
        // Gerar novo access e novo refresh
        var newAccessToken = tokenService.generateToken(user);
        var newRefreshTokenStr = tokenService.generateRefreshToken(user);
        // Salvar novo refresh
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(newRefreshTokenStr);
        newRefreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
        newRefreshToken.setUser(user);
        refreshTokenRepository.save(newRefreshToken);
        // Setar novos cookies
        String accessCookieHeader = String.format(
                "authToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                newAccessToken,
                24 * 60 * 60);
        String refreshCookieHeader = String.format(
                "refreshToken=%s; Path=/; Max-Age=%d; HttpOnly; SameSite=Lax",
                newRefreshTokenStr,
                1 * 60 * 60);
        response.addHeader("Set-Cookie", accessCookieHeader);
        response.addHeader("Set-Cookie", refreshCookieHeader);
        return newAccessToken;
    }
}