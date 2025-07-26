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
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.Transmissions.TransmissionsDTO;
import com.onixx.apolloveiculos.api.Services.TransmissionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transmissions")
public class TransmissionsController {

    private TransmissionsService transmissionsService;

    public TransmissionsController(TransmissionsService transmissionsService) {
        this.transmissionsService = transmissionsService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseAnyDTO> search() {
        List<Transmissions> transmissions = transmissionsService.search();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", transmissions));
    }

    @GetMapping("/fetch-by-filters")
    public ResponseEntity<ResponseAnyDTO> fetchByFilters(@RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "status", defaultValue = "") String status) {
        try {
            List<Transmissions> transmissions = transmissionsService.searchByFilters(name, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", transmissions));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseAnyDTO> create(@Valid @RequestBody TransmissionsDTO transmissions) {
        try {
            transmissionsService.save(transmissions);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseAnyDTO(200, "", "Transmissão cadastrada com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseAnyDTO> edit(@PathVariable("id") Long id,
            @Valid @RequestBody TransmissionsDTO transmissions) {
        try {
            transmissionsService.update(id, transmissions);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Transmissão editada com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable("id") Long id) {
        try {
            boolean result = transmissionsService.delete(id);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAnyDTO(204, "", "Transmissão deletada com sucesso", Collections.emptyList()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseAnyDTO(404, "Transmissão não encontrada", "", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
}
