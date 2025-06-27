package com.onixx.apolloveiculos.api.Services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.Transmissions.TransmissionsDTO;
import com.onixx.apolloveiculos.api.Repositories.TransmissionsRepository;

import jakarta.transaction.Transactional;

@Component
@Service
public class TransmissionsService {
    private TransmissionsRepository TransmissionsRepository;

    public TransmissionsService(TransmissionsRepository TransmissionsRepository) {
        this.TransmissionsRepository = TransmissionsRepository;
    }

    public List<Transmissions> search() {
        return TransmissionsRepository.findAll();
    }

    public List<Transmissions> searchByFilters(String name, String status) {
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return TransmissionsRepository.findByFilters(name, statusEnum);
    }

    public void buscarPorId(Integer id) {
    }

    @Transactional
    public Transmissions save(TransmissionsDTO TransmissionsDTO) {
        Transmissions transmission = new Transmissions(TransmissionsDTO.name());
        return TransmissionsRepository.save(transmission);
    }

    @Transactional
    public Transmissions update(Long id, TransmissionsDTO TransmissionsDTO) {
        Transmissions existing = TransmissionsRepository.findByIdTransmissions(id);
        if (existing == null) {
            new IllegalArgumentException("Transmissão não encontrado com o id" + id);
        }

        existing.setName(TransmissionsDTO.name());
        existing.setStatus(TransmissionsDTO.status());
        return TransmissionsRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Transmissions transmission = TransmissionsRepository.findByIdTransmissions(id);
        if (transmission == null) {
            return false;
        }
        TransmissionsRepository.delete(transmission);
        return true;
    }
}
