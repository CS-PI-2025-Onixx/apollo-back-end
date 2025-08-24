package com.onixx.apolloveiculos.api.Repositories;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CarsRepository extends JpaRepository<Cars, Long> {
    @Query("SELECT c FROM Cars c WHERE c.id_car = :id")
    Cars findbyIdCar(Long id);

    @Query("SELECT c FROM Cars c LEFT JOIN FETCH c.images WHERE c.id_car = :id")
    Cars findByIdCarWithImages(@Param("id") Long id);

    @Query("SELECT c FROM Cars c LEFT JOIN FETCH c.images")
    List<Cars> findAllWithImages();

}
