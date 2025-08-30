package com.onixx.apolloveiculos.api.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.User.ChangePasswordDTO;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Infra.Security.TokenService;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;
import com.onixx.apolloveiculos.api.Services.EmailService;
import com.onixx.apolloveiculos.api.Services.UserService;


@RestController
@RequestMapping("/forgot-password")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final TokenService tokenService;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, UserService userService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<ResponseAnyDTO> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(
                    new ResponseAnyDTO(404, "User not found", "Email não cadastrado", null),
                    HttpStatus.NOT_FOUND
            );
        }

        String token = tokenService.generateResetPasswordToken(user.getEmail());
        String resetLink = "http://localhost:3000/forgot-password?token=" + token;

        Context context = new Context();
        context.setVariable("link", resetLink);
        emailService.emailTemplate(user.getEmail(), "Redefinição de senha", context, "resetPasswordEmail");

        return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Link de redefinição enviado com sucesso", null));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseAnyDTO> changePasswordHandler(@RequestBody ChangePasswordDTO changePassword, @RequestParam String token) {
        String email = tokenService.validateToken(token);

        if (email == null || email.isBlank()) {
            return new ResponseEntity<>(
                    new ResponseAnyDTO(401, "Token inválido ou expirado", "Token expirado ou inválido", null),
                    HttpStatus.UNAUTHORIZED
            );
        }

        userService.updatePassword(email, changePassword);

        return ResponseEntity.ok(new ResponseAnyDTO(200, null, "Senha alterada com sucesso", null));
    }
}
