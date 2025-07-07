package com.onixx.apolloveiculos.api.Controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Colors.ColorsDTO;
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
    public ResponseEntity<ResponseAnyDTO> search() {
        List<Colors> colors = colorsService.search();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", colors));
    }
    
    @GetMapping("/fetch-by-filters")
    public ResponseEntity<ResponseAnyDTO> fetchByFilters(@RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "status", defaultValue = "") String status) {
        try {
            List<Colors> colors = colorsService.searchByFilters(name, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", colors));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }

    }
    
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseAnyDTO> edit(@PathVariable("id") Long id, @Valid @RequestBody ColorsDTO colors) {
        try {
            colorsService.update(id, colors);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Cor editada com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable("id") Long id) {
        try {
            boolean result = colorsService.delete(id);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAnyDTO(204, "", "Cor deletada com sucesso", Collections.emptyList()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseAnyDTO(404, "Cor não encontrada", "", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
}
