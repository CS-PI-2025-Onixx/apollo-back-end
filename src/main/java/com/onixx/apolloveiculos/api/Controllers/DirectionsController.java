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
import com.onixx.apolloveiculos.api.Domains.Directions.Directions;
import com.onixx.apolloveiculos.api.Domains.Directions.DirectionsDTO;
import com.onixx.apolloveiculos.api.Services.DirectionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/direcitons")
public class DirectionsController {

    private DirectionsService direcitonsService;

    public DirectionsController(DirectionsService direcitonsService) {
        this.direcitonsService = direcitonsService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseAnyDTO> search() {
        List<Directions> direcitons = direcitonsService.search();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", direcitons));
    }

    @GetMapping("/fetch-by-filters")
    public ResponseEntity<ResponseAnyDTO> fetchByFilters(@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "status", defaultValue = "") String status) {
        try {
            List<Directions> direcitons = direcitonsService.searchByFilters(name, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", direcitons));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseAnyDTO> create(@Valid @RequestBody DirectionsDTO direcitons) {
        try {
            direcitonsService.save(direcitons);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseAnyDTO(200, "", "Direção cadastrada com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseAnyDTO> edit(@PathVariable("id") Long id, @Valid @RequestBody DirectionsDTO direcitons) {
        try {
            direcitonsService.update(id, direcitons);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "Direção editada com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable("id") Long id) {
        try {
            boolean result = direcitonsService.delete(id);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(204, "", "Direção deletada com sucesso", Collections.emptyList()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAnyDTO(404, "Direção não encontrada", "", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
}
