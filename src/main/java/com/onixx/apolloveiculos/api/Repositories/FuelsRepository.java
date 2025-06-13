package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuelsRepository extends JpaRepository<Fuels, Integer> {
    Fuels findByName(String name);

    @Query("SELECT m FROM Fuels m WHERE m.id_Fuel = :id")
    Fuels findByIdFuels(@Param("id") Long id);

    @Query("SELECT m FROM Fuels m WHERE " +
            "(:name IS NULL OR m.name = :name) AND " +
            "(:status IS NULL OR m.status = :status)")
    List<Fuels> findByFilters(@Param("name") String name, @Param("status") Status status);
}
