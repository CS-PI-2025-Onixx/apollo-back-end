package com.onixx.apolloveiculos.api.Utils;

import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Directions.Directions;
import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Domains.User.UserRoles;
import com.onixx.apolloveiculos.api.Repositories.*;
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
    CommandLineRunner initDatabase(DirectionsRepository directionsRepository, ColorsRepository colorsRepository) {
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
            List.of("Manual", "Automático", "CVT", "Semi-automático", "Automatizado").forEach(name -> {
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
            List.of("Hidráulica", "Elétrica", "Mêcanica, Eletro-hidráulica").forEach(name -> {
                if (directionsRepository.findByName(name) == null) {
                    directionsRepository.save(new Directions(name));
                }
            });

            List.of("Preto", "Branco", "Prata", "Vermelho", "Cinza", "Azul", "Amarelo", "Verde", "Laranja", "Outra").forEach(name -> {
                if (colorsRepository.findByName(name) == null) {
                    colorsRepository.save(new Colors(name));
                }
            });

            System.out.println("Dados iniciais cadastrados com sucesso!");
        };
    }
}