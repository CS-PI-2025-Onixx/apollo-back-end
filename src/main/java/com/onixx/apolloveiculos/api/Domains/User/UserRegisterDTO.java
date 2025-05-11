package com.onixx.apolloveiculos.api.Domains.User;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(
        @NotBlank(message = "O nome/usuário é obrigatório") String name,
        @NotBlank(message = "O email não pode ser vazio") String email,
        @NotBlank(message = "A senha não pode ser vazia") String password,
                              UserRoles role) {
}
