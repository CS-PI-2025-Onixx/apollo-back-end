package com.onixx.apolloveiculos.api.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.FavoriteCars;
import com.onixx.apolloveiculos.api.Domains.User.User;

@Repository
public interface FavoriteCarsRepository extends JpaRepository<FavoriteCars, Long>{
    
    boolean existsByUserAndCar(User user, Cars car);

    Optional<FavoriteCars> findByUserAndCar(User user, Cars car);

    List<FavoriteCars> findByUser(User user);

    void deleteByUserAndCar(User user, Cars car);
    
}
