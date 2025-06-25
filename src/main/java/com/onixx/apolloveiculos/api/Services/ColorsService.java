package com.onixx.apolloveiculos.api.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Colors.ColorsDTO;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import com.onixx.apolloveiculos.api.Repositories.ColorsRepository;

@Service
public class ColorsService {
    private ColorsRepository colorsRepository;

    public ColorsService(ColorsRepository colorsRepository) {
        this.colorsRepository = colorsRepository;
    }

    public List<Colors> search() {
        return colorsRepository.findAll();
    }

    public List<Colors> searchByFilters(String name, String status) {
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return colorsRepository.findByFilters(name, statusEnum);
    }

    public void buscarPorId(Integer id) {
    }

    @Transactional
    public Colors save(ColorsDTO colorsDTO) {
        Colors motor = new Colors(colorsDTO.name());
        return colorsRepository.save(motor);
    }

    @Transactional
    public Colors update(Long id, ColorsDTO colorsDTO) {
        Colors existing = colorsRepository.findByIdColors(id);
        if (existing == null) {
            new IllegalArgumentException("cor não encontrada com o id" + id);
        }

        existing.setName(colorsDTO.name());
        existing.setStatus(colorsDTO.status());
        return colorsRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Colors motor = colorsRepository.findByIdColors(id);
        if (motor == null) {
            return false;
        }
        colorsRepository.delete(motor);
        return true;
    }
}
