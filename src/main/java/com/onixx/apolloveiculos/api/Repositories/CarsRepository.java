package com.onixx.apolloveiculos.api.Repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.VehicleTypes;
import com.onixx.apolloveiculos.api.Domains.Cars.VehiclesStatus;

public interface CarsRepository extends JpaRepository<Cars, Long> {

    Page<Cars> findAll(Pageable pageable);

    @Query("SELECT c FROM Cars c WHERE c.id_car = :id")
    Cars findbyIdCar(Long id);

    @Query("SELECT c FROM Cars c LEFT JOIN FETCH c.images WHERE c.id_car = :id")
    Cars findByIdCarWithImages(@Param("id") Long id);

    @Query("SELECT c FROM Cars c LEFT JOIN FETCH c.images")
    List<Cars> findAllWithImages();

    @Query("SELECT DISTINCT c FROM Cars c WHERE " +
            "(:brand IS NULL OR LOWER(c.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:model IS NULL OR LOWER(c.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
            "(:color IS NULL OR LOWER(c.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
            "(:yearMin IS NULL OR c.year >= :yearMin) AND " +
            "(:yearMax IS NULL OR c.year <= :yearMax) AND " +
            "(:mileageMin IS NULL OR c.mileage >= :mileageMin) AND " +
            "(:mileageMax IS NULL OR c.mileage <= :mileageMax) AND " +
            "(:priceMin IS NULL OR c.vehiclePrice >= :priceMin) AND " +
            "(:priceMax IS NULL OR c.vehiclePrice <= :priceMax) AND " +
            "(:#{#fuel == null || #fuel.isEmpty()} = true OR c.fuel IN :fuel) AND " +
            "(:#{#bodywork == null || #bodywork.isEmpty()} = true OR c.bodywork IN :bodywork) AND " +
            "(:#{#transmission == null || #transmission.isEmpty()} = true OR c.transmission IN :transmission) AND " +
            "(:#{#direction == null || #direction.isEmpty()} = true OR c.direction IN :direction) AND " +
            "(:vehicleCondition IS NULL OR c.vehicleCondition = :vehicleCondition) AND " +
            "(:carType IS NULL OR c.carType = :carType) " +
            "AND c.dt_delete IS NULL " +
            "ORDER BY c.id_car DESC")
    List<Cars> findByFilters(@Param("brand") String brand,
                             @Param("model") String model,
                             @Param("color") String color,
                             @Param("yearMin") Integer yearMin,
                             @Param("yearMax") Integer yearMax,
                             @Param("mileageMin") Integer mileageMin,
                             @Param("mileageMax") Integer mileageMax,
                             @Param("priceMin") BigDecimal priceMin,
                             @Param("priceMax") BigDecimal priceMax,
                             @Param("fuel") List<String> fuel,
                             @Param("bodywork") List<String> bodywork,
                             @Param("transmission") List<String> transmission,
                             @Param("direction") List<String> direction,
                             @Param("vehicleCondition") String vehicleCondition,
                             @Param("carType") VehicleTypes carType);

    @Query("SELECT c FROM Cars c WHERE c.vehicleStatus = :status AND c.vehicleStatusChangedAt BETWEEN :start AND :end AND c.dt_delete IS NULL ORDER BY c.vehicleStatusChangedAt DESC")
    List<Cars> findByStatusChangedBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") VehiclesStatus status
    );

    @Query("SELECT COUNT(c) FROM Cars c WHERE c.vehicleStatus = :status AND c.vehicleStatusChangedAt BETWEEN :start AND :end AND c.dt_delete IS NULL")
    long countByStatusChangedBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") VehiclesStatus status
    );
}
