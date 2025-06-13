package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Fuels.FuelsDTO;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import com.onixx.apolloveiculos.api.Repositories.FuelsRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class FuelsService {
    private FuelsRepository fuelsRepository;

    public FuelsService(FuelsRepository fuelsRepository) {
        this.fuelsRepository = fuelsRepository;
    }

    public List<Fuels> search() {
        return fuelsRepository.findAll();
    }

    public List<Fuels> searchByFilters(String name, String status) {
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return fuelsRepository.findByFilters(name, statusEnum);
    }

    public void buscarPorId(Integer id) {
    }

    @Transactional
    public Fuels save(FuelsDTO fuelsDTO) {
        Fuels fuel = new Fuels(fuelsDTO.name());
        return fuelsRepository.save(fuel);
    }

    @Transactional
    public Fuels update(Long id, FuelsDTO fuelsDTO) {
        Fuels existing = fuelsRepository.findByIdFuels(id);
        if (existing == null) {
            new IllegalArgumentException("Combustivel não encontrado com o id" + id);
        }

        existing.setName(fuelsDTO.name());
        existing.setStatus(fuelsDTO.status());
        return fuelsRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Fuels fuel = fuelsRepository.findByIdFuels(id);
        if (fuel == null) {
            return false;
        }
        fuelsRepository.delete(fuel);
        return true;
    }
}
