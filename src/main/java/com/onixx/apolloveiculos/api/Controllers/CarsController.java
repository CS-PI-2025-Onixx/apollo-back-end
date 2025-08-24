package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarsController {

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<ResponseAnyDTO> create(
            @ModelAttribute Cars car,
            @RequestPart(value = "car_images", required = false) List<MultipartFile> imageFiles) {

        try {
            Cars savedCar = carService.create(car, imageFiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseAnyDTO(200, "", "Carro salvo com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAnyDTO> findById(@PathVariable Long id) {
        try {
            Cars car = carService.findById(id);
            if (car != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "Carro encontrado com sucesso",car));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<ResponseAnyDTO> findAll() {
        try {
            List<Cars> cars = carService.findAll();
            return ResponseEntity.ok().body(new ResponseAnyDTO(200, "", "Carros encontrados com sucesso", cars));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cars> update(
            @PathVariable Long id,
            @RequestPart("car") Cars carData,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImageFiles) {

        try {
            Cars updatedCar = carService.update(id, carData, newImageFiles);
            return ResponseEntity.ok(updatedCar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            carService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Cars>> findByFilters(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer yearMin,
            @RequestParam(required = false) Integer yearMax,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String fuel,
            @RequestParam(required = false) String vehicleCondition) {

        try {
            List<Cars> cars = carService.findByFilters(
                    brand, model, color, yearMin, yearMax,
                    priceMin, priceMax, fuel, vehicleCondition
            );
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/trade")
    public ResponseEntity<List<Cars>> findWithTrade() {
        try {
            List<Cars> cars = carService.findAll().stream()
                    .filter(Cars::isTrade)
                    .toList();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}