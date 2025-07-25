package com.onixx.apolloveiculos.api.Domains.User;

import jakarta.validation.constraints.NotBlank;

public record UserDTO( @NotBlank(message = "O nome não pode ser vazio")String name, @NotBlank(message = "A senha não pode ser vazio")String password) {
}
