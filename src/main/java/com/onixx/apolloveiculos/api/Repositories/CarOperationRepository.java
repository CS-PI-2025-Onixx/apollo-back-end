package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.CarOperations.CarOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarOperationRepository extends JpaRepository<CarOperation, Integer> {

    @Query("SELECT co FROM CarOperation co WHERE co.car.id_car = :carId")
    Optional<CarOperation> findByCarId(@Param("carId") Long carId);

    @Query("SELECT co FROM CarOperation co WHERE co.tipoOperacao = :tipoOperacao")
    List<CarOperation> findByTipoOperacao(@Param("tipoOperacao") int tipoOperacao);

    @Query("SELECT co FROM CarOperation co WHERE LOWER(co.nomeCliente) LIKE LOWER(CONCAT('%', :nomeCliente, '%'))")
    List<CarOperation> findByNomeCliente(@Param("nomeCliente") String nomeCliente);
}

