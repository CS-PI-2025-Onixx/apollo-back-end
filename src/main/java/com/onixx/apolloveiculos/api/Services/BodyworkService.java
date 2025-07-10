package com.onixx.apolloveiculos.api.Services;


import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Bodywork.BodyworkDTO;
import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Bodywork.BodyworkDTO;
import com.onixx.apolloveiculos.api.Repositories.BodyworkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;

import java.util.List;

@Service
public class BodyworkService {
    private BodyworkRepository bodyworkRepository;

    public BodyworkService(BodyworkRepository bodyworkRepository){this.bodyworkRepository = bodyworkRepository;}
    public List<Bodywork> search(){return bodyworkRepository.findAll();}


    public List<Bodywork> searchByFilters(String name, String status){
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return bodyworkRepository.findByFilters(name, statusEnum);
    }

    public void buscarPorId(Integer id) {
    }

    @Transactional
    public Bodywork save(BodyworkDTO bodyworkDTO){
        Bodywork bodywork = new Bodywork(bodyworkDTO.name());
        return bodyworkRepository.save(bodywork);
    }

    @Transactional
    public Bodywork update(Long id, BodyworkDTO bodyworkDTO) {
        Bodywork existing = bodyworkRepository.findByIdBodywork(id);
        if (existing == null) {
            new IllegalArgumentException("cor não encontrada com o id" + id);
        }

        existing.setName(bodyworkDTO.name());
        existing.setStatus(bodyworkDTO.status());
        return bodyworkRepository.save(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        Bodywork bodywork = bodyworkRepository.findByIdBodywork(id);
        if (bodywork == null) {
            return false;
        }
        bodyworkRepository.delete(bodywork);
        return true;
    }
}
