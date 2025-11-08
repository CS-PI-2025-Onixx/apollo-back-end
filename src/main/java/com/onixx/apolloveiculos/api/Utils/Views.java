package com.onixx.apolloveiculos.api.Utils;

/**
 * Classes de Views para controlar a serialização JSON baseada em roles
 * Views hierárquicas: Admin extends User
 */
public class Views {

    /**
     * View para usuários normais - dados básicos
     */
    public static class UserView { }

    /**
     * View para administradores - inclui dados sensíveis e de integração
     */
    public static class AdminView extends UserView { }
}

