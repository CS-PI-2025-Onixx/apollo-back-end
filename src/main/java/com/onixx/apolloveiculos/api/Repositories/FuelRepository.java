package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.Fuel.Fuel;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Integer> {

    @Query("SELECT f FROM Fuel f WHERE f.id_fuel = :id")
    Fuel findByIdFuel(@Param("id") Long id);

    @Query("SELECT f FROM Fuel f WHERE " +
            "(:name IS NULL OR f.name = :name) AND " +
            "(:status IS NULL OR f.status = :status)")
    List<Fuel> findByFilters(@Param("name") String name, @Param("status") Status status);
}