package com.onixx.apolloveiculos.api.Controllers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.VehiclesStatus;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarParams;
import com.onixx.apolloveiculos.api.Services.CarService;

import lombok.extern.slf4j.Slf4j;

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

    @PostMapping("/{id}")
    public ResponseEntity<ResponseAnyDTO> update(
            @PathVariable Long id,
            @ModelAttribute Cars carData,
            @RequestPart(value = "car_images", required = false) List<MultipartFile> newImageFiles,
            @RequestParam(value = "publish_olx", required = false, defaultValue = "false") Boolean publishOlx
    ) {
        try {
            Cars updatedCar = carService.update(id, carData, newImageFiles, null, publishOlx);
            return ResponseEntity.ok().body(new ResponseAnyDTO(200, "", "Carro atualizado com sucesso", updatedCar));
        } catch (RuntimeException e) {
            log.error("Erro de runtime ao atualizar carro: ", e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro ao atualizar carro: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, "", "Erro interno do servidor", null));
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
    public ResponseEntity<ResponseAnyDTO> findByFilters(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer yearMin,
            @RequestParam(required = false) Integer yearMax,
            @RequestParam(required = false) Integer mileageMin,
            @RequestParam(required = false) Integer mileageMax,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) List<String> fuel,
            @RequestParam(required = false) List<String> bodywork,
            @RequestParam(required = false) List<String> transmission,
            @RequestParam(required = false) List<String> direction,
            @RequestParam(required = false) String vehicleCondition,
            @RequestParam(required = false) String carType) {

        try {
            List<Cars> cars = carService.findByFilters(
                    brand, model, color, yearMin, yearMax,
                    mileageMin, mileageMax,priceMin, priceMax, fuel,bodywork, transmission, direction, vehicleCondition, carType
            );

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "Carros encontrados com sucesso", cars));
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

    @GetMapping("/cars/status")
    public ResponseEntity<Map<VehiclesStatus, Long>> getStatusCounters(@RequestParam Long days) {
        return ResponseEntity.ok(carService.getStatusCountersForPeriod(days));
    }

    @GetMapping("/cars/status-changed")
    public ResponseEntity<List<Cars>> findCarsChangedToStatusInPeriod(
        @RequestParam VehiclesStatus status,
        @RequestParam int days) {
            List<Cars> cars = carService.findCarsChangedToStatusInPeriod(status, days);
            return ResponseEntity.ok(cars);
    }
}