package com.onixx.apolloveiculos.api.Controllers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity<ResponseAnyDTO> findAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            Page<Cars> carsPage = carService.listAllPaginated(page, size);
            return ResponseEntity.ok(
                    new ResponseAnyDTO(200, "", "Carros encontrados com sucesso", carsPage)
            );
        } catch (Exception e) {
            log.error("Erro ao listar carros: ", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, e.getMessage(), "Erro ao buscar veículos", null));
        }
    }

    @PostMapping
    public ResponseEntity<ResponseAnyDTO> create(
            @ModelAttribute Cars car,
            @RequestPart(value = "car_images", required = false) List<MultipartFile> imageFiles,
            @ModelAttribute OLXCarParams olxCarParams,
            @RequestParam(value = "publish_olx", required = false, defaultValue = "false") Boolean publishOlx) {
        try {
            Cars savedCar = carService.create(car, imageFiles, olxCarParams, publishOlx);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseAnyDTO(201, "", "Carro salvo com sucesso", savedCar));
        } catch (Exception e) {
            log.error("Erro ao criar carro: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao salvar carro", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAnyDTO> findById(@PathVariable Long id) {
        try {
            Cars car = carService.findById(id);
            if (car == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseAnyDTO(404, "", "Carro não encontrado", null));
            }
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Carro encontrado com sucesso", car));
        } catch (Exception e) {
            log.error("Erro ao buscar carro: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar carro", null));
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseAnyDTO> update(
            @PathVariable Long id,
            @ModelAttribute Cars carData,
            @RequestPart(value = "car_images", required = false) List<MultipartFile> newImageFiles,
            @RequestParam(value = "publish_olx", required = false, defaultValue = "false") Boolean publishOlx) {
        try {
            Cars updatedCar = carService.update(id, carData, newImageFiles, null, publishOlx);
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Carro atualizado com sucesso", updatedCar));
        } catch (RuntimeException e) {
            log.error("Erro de runtime ao atualizar carro: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, e.getMessage(), "Carro não encontrado", null));
        } catch (Exception e) {
            log.error("Erro ao atualizar carro: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao atualizar carro", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable Long id) {
        try {
            carService.delete(id);
            return ResponseEntity.ok(new ResponseAnyDTO(204, "", "Carro deletado com sucesso", Collections.emptyList()));
        } catch (RuntimeException e) {
            log.error("Erro ao deletar carro: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, e.getMessage(), "Carro não encontrado", null));
        } catch (Exception e) {
            log.error("Erro interno ao deletar carro: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao deletar carro", null));
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
                    mileageMin, mileageMax, priceMin, priceMax,
                    fuel, bodywork, transmission, direction,
                    vehicleCondition, carType);
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Carros encontrados com sucesso", cars));
        } catch (Exception e) {
            log.error("Erro ao filtrar carros: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar veículos", null));
        }
    }

    @GetMapping("/trade")
    public ResponseEntity<ResponseAnyDTO> findWithTrade() {
        try {
            List<Cars> cars = carService.findAll().stream()
                    .filter(Cars::isTrade)
                    .toList();
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Carros com troca encontrados", cars));
        } catch (Exception e) {
            log.error("Erro ao buscar carros com troca: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar carros com troca", null));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseAnyDTO> getStatusCounters(@RequestParam Long days) {
        try {
            Map<VehiclesStatus, Long> result = carService.getStatusCountersForPeriod(days);
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Contadores obtidos com sucesso", result));
        } catch (Exception e) {
            log.error("Erro ao buscar contadores de status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar contadores", null));
        }
    }

    @GetMapping("/status-changed")
    public ResponseEntity<ResponseAnyDTO> findCarsChangedToStatusInPeriod(
            @RequestParam VehiclesStatus status,
            @RequestParam int days) {
        try {
            List<Cars> cars = carService.findCarsChangedToStatusInPeriod(status, days);
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Carros encontrados com sucesso", cars));
        } catch (Exception e) {
            log.error("Erro ao buscar carros alterados de status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar carros por status", null));
        }
    }
}
