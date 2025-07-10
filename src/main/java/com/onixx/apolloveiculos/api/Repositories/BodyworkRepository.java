package com.onixx.apolloveiculos.api.Repositories;
import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Bodywork.BodyworkDTO;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodyworkRepository extends JpaRepository<Bodywork, Integer>{
    Bodywork findByName(String name);
    @Query("SELECT b FROM Bodywork b WHERE b.id_bodywork = :id")
    Bodywork findByIdBodywork(@Param("id") Long id);

    @Query("SELECT b FROM Colors b WHERE " +
            "(:name IS NULL OR b.name = :name) AND " +
            "(:status IS NULL OR b.status = :status)")
    List<Bodywork> findByFilters(@Param("name")String name, @Param("status") Status status);
    BodyworkDTO save(BodyworkDTO bodyworkDTO);
}