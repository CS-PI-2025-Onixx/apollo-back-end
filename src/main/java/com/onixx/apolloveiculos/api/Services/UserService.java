package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User createUser(User user){
        if(userRepository.findByEmail(user.getEmail())!=null || userRepository.findByName(user.getName())!=null) {
            throw new IllegalArgumentException("E-mail já cadastrado " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }
}
