package com.onixx.apolloveiculos.api.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Domains.User.UserDTO;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

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

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

        @Transactional
    public User delete(UserDTO userDTO) {
        User user = (User) userRepository.findByName(userDTO.name());
        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + userDTO.name());
        }
        if (!passwordEncoder.matches(userDTO.password(), user.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta");
        }
        userRepository.deleteById(user.getId_user());
        return user;
    }
    
}
