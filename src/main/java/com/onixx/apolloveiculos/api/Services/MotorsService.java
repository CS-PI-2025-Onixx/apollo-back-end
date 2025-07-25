package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Motors.MotorsDTO;
import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import com.onixx.apolloveiculos.api.Repositories.MotorsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class MotorsService {

    private MotorsRepository motorsRepository;

    public MotorsService(MotorsRepository motorsRepository) {
        this.motorsRepository = motorsRepository;
    }

    public List<Motors> search(){
        return motorsRepository.findAll();
    }
    public List<Motors> searchByFilters(String name, String status){
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return motorsRepository.findByFilters(name, statusEnum);
    }
    public void buscarPorId(Integer id){}

    @Transactional
    public Motors save(MotorsDTO motorsDTO) {
        Motors motor = new Motors(motorsDTO.name());
        return motorsRepository.save(motor);
    }

    @Transactional
    public Motors update(Long id, MotorsDTO motorsDTO) {
        Motors existing = motorsRepository.findByIdMotors(id);
        if(existing == null){
            new IllegalArgumentException("Motor não encontrado com o id" + id);
        }

        existing.setName(motorsDTO.name());
        existing.setStatus(motorsDTO.status());
        return motorsRepository.save(existing);
    }
    @Transactional
    public boolean delete(Long id) {
        Motors motor = motorsRepository.findByIdMotors(id);
        if(motor == null){
            return false;
        }
        motorsRepository.delete(motor);
        return true;
    }
}
