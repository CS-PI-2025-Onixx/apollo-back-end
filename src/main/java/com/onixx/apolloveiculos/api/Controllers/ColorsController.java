package com.onixx.apolloveiculos.api.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Colors.ColorsDTO;
import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Services.ColorsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colors")
public class ColorsController {

    @Autowired
    private ColorsService colorsService;
    
    @PostMapping("/create")
    public ResponseEntity<Colors> create(@Valid @RequestBody ColorsDTO color){
        return ResponseEntity.ok(colorsService.save(color));
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<Colors>> search(){
        return ResponseEntity.ok(colorsService.search());
    }
}
