package com.onixx.apolloveiculos.api.Domains.User;

public enum UserRoles {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private String role;
    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
