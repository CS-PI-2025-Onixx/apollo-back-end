package com.onixx.apolloveiculos.api.Utils;

import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.VehicleTypes;
import com.onixx.apolloveiculos.api.Domains.Cars.VehiclesStatus;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Directions.Directions;
import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Domains.User.UserRoles;
import com.onixx.apolloveiculos.api.Infra.EnvironmentChecker;
import com.onixx.apolloveiculos.api.Repositories.*;
import com.onixx.apolloveiculos.api.Services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.onixx.apolloveiculos.api.Domains.Cars.VehicleTypes.ALUGUEL;
import static com.onixx.apolloveiculos.api.Domains.Cars.VehicleTypes.VENDA;
import static com.onixx.apolloveiculos.api.Domains.Cars.VehiclesStatus.*;
import static com.onixx.apolloveiculos.api.Utils.ENUM_CONDITION.USADO;

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
    @Autowired
    private EnvironmentChecker environmentChecker;

    @Bean
    CommandLineRunner initDatabase(DirectionsRepository directionsRepository, ColorsRepository colorsRepository, CarService carService) {
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
            if (environmentChecker.isDevelopment()) {
                LocalDateTime date = LocalDateTime.now().minusDays(10);

                createCars(30, VENDA, DISPONIVEL, null, carService);

                createCars(10, ALUGUEL, DISPONIVEL, null, carService);

                createCars(3, ALUGUEL, ALUGADO, date, carService);

                createCars(3, VENDA, VENDIDO, date, carService);
                createMockSalesData(carService);
            }
            System.out.println("Dados iniciais cadastrados com sucesso!");
        };
    }


    private void createCars(int quantity, VehicleTypes carType, VehiclesStatus vehicleStatus, LocalDateTime date, CarService carService) {
        for (int i = 0; i < quantity; i++) {
            Cars car = new Cars();
            car.setBrand("Toyota");
            car.setModel("Corolla");
            car.setCarType(carType);
            car.setVehicleCondition(String.valueOf(USADO));
            car.setModel("XEI");
            car.setLicensePlateEnd((byte) 5);
            car.setColor("Prata");
            car.setArmored(false);
            car.setVehiclePrice(java.math.BigDecimal.valueOf(95000));
            car.setMileage(15000);
            int year = 2018 + (int) (Math.random() * ((2024 - 2018) + 1));
            car.setYear(year);
            car.setTransmission("Automático");
            car.setDirection("Elétrica");
            car.setFuel("Flex");
            car.setBodywork("Sedan");
            car.setVehicleStatus(vehicleStatus);
            car.setAcceptsExchange("Sim");
            car.setDescription("Carro em ótimo estado, único dono, com todas as revisões em dia.");
            car.setMotorPower("2.0");
            car.setVehicleTag("123123");

            if (vehicleStatus.equals(ALUGADO)) {
                car.setDtRent(date);
            } else if (vehicleStatus.equals(VENDIDO)) {
                car.setDtSale(date);
            }

            carService.createMockData(car, Collections.singletonList("https://res.cloudinary.com/dmlfcxheq/image/upload/v1757125410/vasco_pvypfx.png"));
        }
    }

    private void createMockSalesData(CarService carService) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastMonthStart = now.minusMonths(1).withDayOfMonth(1);
        LocalDateTime lastMonthEnd = lastMonthStart.plusMonths(1).minusDays(1);

        createCars(15, VENDA, VENDIDO, lastMonthEnd.minusDays(10), carService);

        createCars(15, VENDA, VENDIDO, now.minusDays(5), carService);
    }
}
