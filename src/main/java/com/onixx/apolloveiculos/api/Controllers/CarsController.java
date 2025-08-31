package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarParams;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarRequest;
import com.onixx.apolloveiculos.api.Services.CarService;
import com.onixx.apolloveiculos.api.Services.OLXIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cars")
public class CarsController {

    @Autowired
    private CarService carService;
    @PostMapping
    public ResponseEntity<ResponseAnyDTO> create(
            @ModelAttribute Cars car,
            @RequestPart(value = "car_images", required = false) List<MultipartFile> imageFiles,
            @ModelAttribute OLXCarParams olxCarParams,
            @RequestParam(value = "publish_olx", required = false, defaultValue = "false") Boolean publishOlx
            ) {
        try {
            Cars savedCar = carService.create(car, imageFiles, olxCarParams, publishOlx);
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
            @ModelAttribute Cars carData,
            @RequestPart(value = "car_images", required = false) List<MultipartFile> newImageFiles,
            @ModelAttribute OLXCarParams olxCarParams,
            @RequestParam(value = "publish_olx", required = false, defaultValue = "false") Boolean publishOlx
            ) {
        try {
            log.info("Atualizando carro com ID: " + id);
            log.info("Images" + (newImageFiles != null ? newImageFiles.size() : 0) + " imagens recebidas para atualização.");
            Cars updatedCar = carService.update(id, carData, newImageFiles, olxCarParams, publishOlx);
            return ResponseEntity.ok(updatedCar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.info("Atualizando carro com ID: " + id);
            log.info("Images" + (newImageFiles != null ? newImageFiles.size() : 0) + " imagens recebidas para atualização.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable Long id) {
        try {
            carService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseAnyDTO(204, "", "Carro deletado com sucesso", Collections.emptyList()));
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