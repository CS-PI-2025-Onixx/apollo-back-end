package com.onixx.apolloveiculos.api.Repositories;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorsRepository extends JpaRepository<Colors, Integer> {
    Colors findByName(String name);

    @Query("SELECT c FROM Colors c WHERE c.id_color = :id")
    Colors findByIdMotors(@Param("id") Long id);

    @Query("SELECT c FROM Colors c WHERE"+
            "(:name IS NULL OR c.name = :name) AND " +
            "(:status IS NULL OR c.status)")
    List<Colors> findByFilters(@Param("name") String name, @Param("status") Status status);

}
