package com.onixx.apolloveiculos.api.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onixx.apolloveiculos.api.Domains.Directions.Directions;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;

@Repository
public interface DirectionsRepository extends JpaRepository<Directions, Integer> {

    Directions findByName(String name);

    @Query("SELECT d FROM Directions d WHERE d.id_direction = :id")
    Directions findByIdDirections(@Param("id") Long id);

    @Query("SELECT d FROM Directions d WHERE"
            + "(:name IS NULL OR d.name = :name) AND "
            + "(:status IS NULL OR d.status = :status)")
    List<Directions> findByFilters(@Param("name") String name, @Param("status") Status status);
}
