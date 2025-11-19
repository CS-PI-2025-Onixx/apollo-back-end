package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.Domains.CarOperations.CarOperation;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Repositories.CarOperationRepository;
import com.onixx.apolloveiculos.api.Repositories.CarsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CarOperationService {

    @Autowired
    private CarOperationRepository carOperationRepository;

    @Autowired
    private CarsRepository carsRepository;

    /**
     * Cria uma operação de venda ou aluguel de um veículo
     * tipoOperacao: 0 - Venda, 1 - Aluguel
     */
    @Transactional
    public CarOperation createOperation(CarOperation carOperation, Long carId) {
        try {
            // Busca o carro
            Cars car = carsRepository.findById(carId)
                    .orElseThrow(() -> new RuntimeException("Carro não encontrado com ID: " + carId));

            // Verifica se o carro já possui uma operação
            Optional<CarOperation> existingOperation = carOperationRepository.findByCarId(carId);
            if (existingOperation.isPresent()) {
                throw new RuntimeException("Este veículo já possui uma operação registrada");
            }

            // Vincula o carro à operação
            carOperation.setCar(car);

            // Atualiza as datas no carro baseado no tipo de operação
            if (carOperation.getTipoOperacao() == 0) {
                // Venda
                car.setDtSale(LocalDateTime.now());
                log.info("Registrando venda do veículo ID: {}", carId);
            } else if (carOperation.getTipoOperacao() == 1) {
                // Aluguel
                car.setDtRent(LocalDateTime.now());
                log.info("Registrando aluguel do veículo ID: {}", carId);
            } else {
                throw new IllegalArgumentException("Tipo de operação inválido. Use 0 para Venda ou 1 para Aluguel");
            }

            // Salva o carro atualizado
            carsRepository.save(car);

            // Salva a operação
            CarOperation savedOperation = carOperationRepository.save(carOperation);
            log.info("Operação criada com sucesso para o veículo ID: {}", carId);

            return savedOperation;
        } catch (Exception e) {
            log.error("Erro ao criar operação para o veículo ID: {}", carId, e);
            throw e;
        }
    }

    /**
     * Busca uma operação pelo ID do carro
     */
    public Optional<CarOperation> findByCarId(Long carId) {
        return carOperationRepository.findByCarId(carId);
    }

    /**
     * Busca todas as operações
     */
    public List<CarOperation> findAll() {
        return carOperationRepository.findAll();
    }

    /**
     * Busca operações por tipo (0 - Venda, 1 - Aluguel)
     */
    public List<CarOperation> findByTipoOperacao(int tipoOperacao) {
        if (tipoOperacao != 0 && tipoOperacao != 1) {
            throw new IllegalArgumentException("Tipo de operação inválido. Use 0 para Venda ou 1 para Aluguel");
        }
        return carOperationRepository.findByTipoOperacao(tipoOperacao);
    }

    /**
     * Busca operações por nome do cliente
     */
    public List<CarOperation> findByNomeCliente(String nomeCliente) {
        return carOperationRepository.findByNomeCliente(nomeCliente);
    }

    /**
     * Busca uma operação por ID
     */
    public Optional<CarOperation> findById(Integer id) {
        return carOperationRepository.findById(id);
    }

    /**
     * Atualiza uma operação existente
     */
    @Transactional
    public CarOperation updateOperation(Integer id, CarOperation carOperationData) {
        CarOperation existingOperation = carOperationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operação não encontrada com ID: " + id));

        // Atualiza os campos permitidos
        if (carOperationData.getNomeCliente() != null) {
            existingOperation.setNomeCliente(carOperationData.getNomeCliente());
        }
        if (carOperationData.getValor() > 0) {
            existingOperation.setValor(carOperationData.getValor());
        }
        if (carOperationData.getDataDevolucao() != null) {
            existingOperation.setDataDevolucao(carOperationData.getDataDevolucao());
        }

        return carOperationRepository.save(existingOperation);
    }

    /**
     * Deleta uma operação (soft delete)
     */
    @Transactional
    public void deleteOperation(Integer id) {
        CarOperation operation = carOperationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operação não encontrada com ID: " + id));

        // Remove as datas do carro
        Cars car = operation.getCar();
        if (operation.getTipoOperacao() == 0) {
            car.setDtSale(null);
        } else if (operation.getTipoOperacao() == 1) {
            car.setDtRent(null);
        }
        carsRepository.save(car);

        // Deleta a operação (soft delete)
        carOperationRepository.delete(operation);
        log.info("Operação deletada com sucesso ID: {}", id);
    }
}

