package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransmissionsRepository extends JpaRepository<Transmissions, Integer> {
    Transmissions findByName(String name);

    @Query("SELECT m FROM Transmissions m WHERE m.id_transmission = :id")
    Transmissions findByIdTransmissions(@Param("id") Long id);

    @Query("SELECT m FROM Transmissions m WHERE " +
            "(:name IS NULL OR m.name = :name) AND " +
            "(:status IS NULL OR m.status = :status)")
    List<Transmissions> findByFilters(@Param("name") String name, @Param("status") Status status);
}
