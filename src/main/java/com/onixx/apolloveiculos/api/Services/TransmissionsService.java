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
    private TransmissionsRepository transmissionsRepository;

    public TransmissionsService(TransmissionsRepository transmissionsRepository) {
        this.transmissionsRepository = transmissionsRepository;
    }

    public List<Transmissions> search() {
        return transmissionsRepository.findAll();
    }

    public List<Transmissions> searchByFilters(String name, String status) {
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return transmissionsRepository.findByFilters(name, statusEnum);
    }

    public void buscarPorId(Integer id) {
    }

    @Transactional
    public Transmissions save(TransmissionsDTO transmissionsDTO) {
        Transmissions transmission = new Transmissions(transmissionsDTO.name());
        return transmissionsRepository.save(transmission);
    }

    @Transactional
    public Transmissions update(Long id, TransmissionsDTO transmissionsDTO) {
        Transmissions existing = transmissionsRepository.findByIdTransmissions(id);
        if (existing == null) {
            new IllegalArgumentException("Transmissão não encontrado com o id" + id);
        }

        existing.setName(transmissionsDTO.name());
        existing.setStatus(transmissionsDTO.status());
        return transmissionsRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Transmissions transmission = transmissionsRepository.findByIdTransmissions(id);
        if (transmission == null) {
            return false;
        }
        transmissionsRepository.delete(transmission);
        return true;
    }
}
