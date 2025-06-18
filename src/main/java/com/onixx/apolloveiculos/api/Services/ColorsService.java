package com.onixx.apolloveiculos.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Colors.ColorsDTO;
import com.onixx.apolloveiculos.api.Repositories.ColorsRepository;

@Service
public class ColorsService {
    @Autowired  
    private ColorsRepository colorsRepository;

    public Colors save(ColorsDTO colorsDTO){
        Colors color = new Colors(colorsDTO.name());
        return colorsRepository.save(color);
    }


}
