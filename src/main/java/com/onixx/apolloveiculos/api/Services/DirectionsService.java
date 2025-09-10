package com.onixx.apolloveiculos.api.Services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onixx.apolloveiculos.api.Domains.Directions.Directions;
import com.onixx.apolloveiculos.api.Domains.Directions.DirectionsDTO;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import com.onixx.apolloveiculos.api.Repositories.DirectionsRepository;

import jakarta.transaction.Transactional;

@Component
@Service
public class DirectionsService {

    private DirectionsRepository directionsRepository;

    public DirectionsService(DirectionsRepository directionsRepository) {
        this.directionsRepository = directionsRepository;
    }

    public List<Directions> search() {
        return directionsRepository.findAll();
    }

    public List<Directions> searchByFilters(String name, String status) {
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return directionsRepository.findByFilters(name, statusEnum);
    }

    public void buscarPorId(Integer id) {
    }

    @Transactional
    public Directions save(DirectionsDTO directionsDTO) {
        Directions direction = new Directions(directionsDTO.name());
        return directionsRepository.save(direction);
    }

    @Transactional
    public Directions update(Long id, DirectionsDTO directionsDTO) {
        Directions existing = directionsRepository.findByIdDirections(id);
        if (existing == null) {
            new IllegalArgumentException("Direção não encontrada com o id" + id);
        }

        existing.setName(directionsDTO.name());
        existing.setStatus(directionsDTO.status());
        return directionsRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Directions direction = directionsRepository.findByIdDirections(id);
        if (direction == null) {
            return false;
        }
        directionsRepository.delete(direction);
        return true;
    }
}
