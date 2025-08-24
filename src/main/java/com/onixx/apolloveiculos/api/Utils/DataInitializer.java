package com.onixx.apolloveiculos.api.Utils;

import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Direction.Direction;
import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Domains.User.UserRoles;
import com.onixx.apolloveiculos.api.Repositories.BodyworkRepository;
import com.onixx.apolloveiculos.api.Repositories.FuelsRepository;
import com.onixx.apolloveiculos.api.Repositories.TransmissionsRepository;
import com.onixx.apolloveiculos.api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {
   @Autowired
   private UserRepository userRepository;
    @Autowired
    private TransmissionsRepository transmissionRepository;
    @Autowired
    private FuelsRepository fuelRepository;
    @Autowired
    private BodyworkRepository bodyworkRepository;
    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if ((userRepository.findByEmail("admin@example.com")) == null) {
                User admin = new User();
                admin.setName("ADMIN");
                admin.setEmail("admin@example.com");
                String encryptedPassword = new BCryptPasswordEncoder().encode("admin123");
                admin.setPassword(encryptedPassword);
                admin.setRole(UserRoles.ROLE_ADMIN);
                userRepository.save(admin);
                System.out.println("Administrador cadastrado com sucesso!");
            }
            List.of("Automático", "Manual", "CVT", "Semi-automático").forEach(name -> {
                if (transmissionRepository.findByName(name) == null) {
                    transmissionRepository.save(new Transmissions(name));
                }
            });


            // Fuels
            List.of("Gasolina", "Álcool", "Flex", "Diesel", "Híbrido", "Elétrico").forEach(name -> {
                if (fuelRepository.findByName(name) == null) {
                    fuelRepository.save(new Fuels(name));
                }
            });

            // Bodyworks
            List.of("Sedan", "Hatchback", "SUV", "Pickup", "Conversível").forEach(name -> {
                if (bodyworkRepository.findByName(name) == null) {
                    bodyworkRepository.save(new Bodywork(name));
                }
            });

            System.out.println("Dados iniciais cadastrados com sucesso!");
        };
    }
}