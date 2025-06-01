package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public  interface MotorsRepository extends JpaRepository<Motors, Integer> {
    Motors findByName(String name);

    @Query("SELECT m FROM Motors m WHERE m.id_motors = :id")
    Motors findByIdMotors(@Param("id") Long id);

    @Query("SELECT m FROM Motors m WHERE " +
            "(:name IS NULL OR m.name = :name) AND " +
            "(:status IS NULL OR m.status = :status)")
    List<Motors> findByFilters(@Param("name") String name, @Param("status") Status status);
}
