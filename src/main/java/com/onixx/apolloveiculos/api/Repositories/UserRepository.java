package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    UserDetails findByName(String name);
}
