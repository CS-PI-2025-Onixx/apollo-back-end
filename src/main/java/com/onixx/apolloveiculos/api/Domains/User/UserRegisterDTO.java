package com.onixx.apolloveiculos.api.Domains.User;

public record UserRegisterDTO(String name, String email, String password, UserRoles role) {
}
