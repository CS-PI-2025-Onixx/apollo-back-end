package com.onixx.apolloveiculos.api.Infra.Exception;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import java.util.Collections;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseAnyDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String field = ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getField() : "desconhecido";
        String message = ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage() : "Dados inválidos";
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ResponseAnyDTO(422, "Requisição Inválida", message, Collections.emptyList()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseAnyDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseAnyDTO(400, "Conflito", ex.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseAnyDTO> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseAnyDTO(401, "Não Autorizado", "Credenciais inválidas",Collections.emptyList()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseAnyDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseAnyDTO(401, "Não Autorizado", "Usuário ou senha inválidos",Collections.emptyList()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseAnyDTO> handleAccessDeniedException(AccessDeniedException ex) {
        System.out.println("Access Denied: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseAnyDTO(403, "Proibido", "Acesso negado: você não tem permissão para acessar este recurso",Collections.emptyList()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseAnyDTO> handleGenericException(Exception ex) {
        if (ex instanceof NoResourceFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, "Não Encontrado", "O recurso solicitado não foi encontrado no servidor.",Collections.emptyList()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseAnyDTO(500, "Erro Interno", "Ocorreu um erro inesperado no servidor.",Collections.emptyList()));
    }
}