package com.onixx.apolloveiculos.api.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.FavoriteCars;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Repositories.FavoriteCarsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteCarsService {

    @Autowired
    private final FavoriteCarsRepository favoriteCarsRepository;

    @Autowired
    private final CarService carsService;

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof User) {
            return (User) principal;
        }
        
        throw new IllegalStateException("Usuário não autenticado");
    }

    @Transactional
    public boolean toggleFavorite(Long carId) {
        User user = getAuthenticatedUser();
        Cars car = carsService.findById(carId);

        if (favoriteCarsRepository.existsByUserAndCar(user, car)) {
            favoriteCarsRepository.deleteByUserAndCar(user, car);
            return false;
        } else {
            FavoriteCars favoriteCar = new FavoriteCars();
            favoriteCar.setUser(user);
            favoriteCar.setCar(car);
            favoriteCarsRepository.save(favoriteCar);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<FavoriteCars> getMyFavorites() {
        User user = getAuthenticatedUser();
        return favoriteCarsRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Cars> getMyFavoriteCarsListByUser() {
        User user = getAuthenticatedUser();
        return favoriteCarsRepository.findByUser(user)
                .stream()
                .map(FavoriteCars::getCar)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Long carId) {
        User user = getAuthenticatedUser();
        Cars car = carsService.findById(carId);
        return favoriteCarsRepository.existsByUserAndCar(user, car);
    }

    @Transactional(readOnly = true)
    public long countMyFavorites() {
        User user = getAuthenticatedUser();
        return favoriteCarsRepository.findByUser(user).size();
    }
}