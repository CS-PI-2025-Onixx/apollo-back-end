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
import com.onixx.apolloveiculos.api.Domains.CarOperations.CarOperation;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.VehiclesStatus;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarParams;
import com.onixx.apolloveiculos.api.Services.CarOperationService;
import com.onixx.apolloveiculos.api.Services.CarService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cars")
public class CarsController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarOperationService carOperationService;

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

    @GetMapping("/rent")
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

    // ==================== ENDPOINTS DE OPERAÇÕES (VENDA/ALUGUEL) ====================

    @PostMapping("/{carId}/operation")
    public ResponseEntity<ResponseAnyDTO> createOperation(
            @PathVariable Long carId,
            @RequestBody CarOperation carOperation) {
        try {
            CarOperation savedOperation = carOperationService.createOperation(carOperation, carId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseAnyDTO(201, "", "Operação registrada com sucesso", savedOperation));
        } catch (RuntimeException e) {
            log.error("Erro ao criar operação: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, e.getMessage(), "Erro ao registrar operação", null));
        } catch (Exception e) {
            log.error("Erro interno ao criar operação: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao registrar operação", null));
        }
    }

    @GetMapping("/{carId}/operation")
    public ResponseEntity<ResponseAnyDTO> getOperationByCarId(@PathVariable Long carId) {
        try {
            return carOperationService.findByCarId(carId)
                    .map(operation -> ResponseEntity.ok(
                            new ResponseAnyDTO(200, "", "Operação encontrada", operation)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseAnyDTO(404, "", "Operação não encontrada para este veículo", null)));
        } catch (Exception e) {
            log.error("Erro ao buscar operação: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar operação", null));
        }
    }

    @GetMapping("/operations")
    public ResponseEntity<ResponseAnyDTO> getAllOperations() {
        try {
            List<CarOperation> operations = carOperationService.findAll();
            return ResponseEntity.ok(new ResponseAnyDTO(200, "", "Operações encontradas", operations));
        } catch (Exception e) {
            log.error("Erro ao buscar operações: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar operações", null));
        }
    }

    @GetMapping("/operations/type/{tipo}")
    public ResponseEntity<ResponseAnyDTO> getOperationsByType(@PathVariable int tipo) {
        try {
            List<CarOperation> operations = carOperationService.findByTipoOperacao(tipo);
            String tipoDesc = tipo == 0 ? "vendas" : "aluguéis";
            return ResponseEntity.ok(
                    new ResponseAnyDTO(200, "", "Operações de " + tipoDesc + " encontradas", operations));
        } catch (IllegalArgumentException e) {
            log.error("Tipo de operação inválido: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, e.getMessage(), "Tipo de operação inválido", null));
        } catch (Exception e) {
            log.error("Erro ao buscar operações por tipo: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar operações", null));
        }
    }

    @GetMapping("/operations/client")
    public ResponseEntity<ResponseAnyDTO> getOperationsByClient(@RequestParam String nomeCliente) {
        try {
            List<CarOperation> operations = carOperationService.findByNomeCliente(nomeCliente);
            return ResponseEntity.ok(
                    new ResponseAnyDTO(200, "", "Operações encontradas para o cliente", operations));
        } catch (Exception e) {
            log.error("Erro ao buscar operações por cliente: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao buscar operações", null));
        }
    }

    @PutMapping("/operations/{operationId}")
    public ResponseEntity<ResponseAnyDTO> updateOperation(
            @PathVariable Integer operationId,
            @RequestBody CarOperation carOperationData) {
        try {
            CarOperation updatedOperation = carOperationService.updateOperation(operationId, carOperationData);
            return ResponseEntity.ok(
                    new ResponseAnyDTO(200, "", "Operação atualizada com sucesso", updatedOperation));
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar operação: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, e.getMessage(), "Operação não encontrada", null));
        } catch (Exception e) {
            log.error("Erro interno ao atualizar operação: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao atualizar operação", null));
        }
    }

    @DeleteMapping("/operations/{operationId}")
    public ResponseEntity<ResponseAnyDTO> deleteOperation(@PathVariable Integer operationId) {
        try {
            carOperationService.deleteOperation(operationId);
            return ResponseEntity.ok(
                    new ResponseAnyDTO(204, "", "Operação deletada com sucesso", Collections.emptyList()));
        } catch (RuntimeException e) {
            log.error("Erro ao deletar operação: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, e.getMessage(), "Operação não encontrada", null));
        } catch (Exception e) {
            log.error("Erro interno ao deletar operação: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, e.getMessage(), "Erro interno ao deletar operação", null));
        }
    }
}
