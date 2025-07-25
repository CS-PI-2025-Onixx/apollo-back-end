package com.onixx.apolloveiculos.api.Controllers;

import java.util.Collections;
import java.util.List;

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
import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Fuels.FuelsDTO;
import com.onixx.apolloveiculos.api.Services.FuelsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fuels")
public class FuelsController {

    private FuelsService fuelsService;

    public FuelsController(FuelsService fuelsService) {
        this.fuelsService = fuelsService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseAnyDTO> search() {
        List<Fuels> fuels = fuelsService.search();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", fuels));
    }

    @GetMapping("/fetch-by-filters")
    public ResponseEntity<ResponseAnyDTO> fetchByFilters(@RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "status", defaultValue = "") String status) {
        try {
            List<Fuels> fuels = fuelsService.searchByFilters(name, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", fuels));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseAnyDTO> create(@Valid @RequestBody FuelsDTO fuels) {
        try {
            fuelsService.save(fuels);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseAnyDTO(200, "", "Combustível cadastrado com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseAnyDTO> edit(@PathVariable("id") Long id, @Valid @RequestBody FuelsDTO fuels) {
        try {
            fuelsService.update(id, fuels);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Combustível editado com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable("id") Long id) {
        try {
            boolean result = fuelsService.delete(id);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAnyDTO(204, "", "Combustível deletado com sucesso", Collections.emptyList()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseAnyDTO(404, "Combustível não encontrado", "", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
}
