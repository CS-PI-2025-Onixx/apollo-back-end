package com.onixx.apolloveiculos.api.Controllers;

import java.util.Collections;
import java.util.List;

import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Bodywork.BodyworkDTO;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Bodywork.BodyworkDTO;
import com.onixx.apolloveiculos.api.Services.BodyworkService;

@RestController
@RequestMapping("/bodywork")
public class BodyworkController {

    @Autowired
    private BodyworkService bodyworkService;

    @PostMapping("/create")
    public ResponseEntity<Bodywork> create(@Valid @RequestBody BodyworkDTO bodywork){
        return ResponseEntity.ok(bodyworkService.save(bodywork));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseAnyDTO> search() {
        List<Bodywork> bodyworks = bodyworkService.search();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", bodyworks));
    }

    @GetMapping("/fetch-by-filters")
    public ResponseEntity<ResponseAnyDTO> fetchByFilters(@RequestParam(value = "name", defaultValue = "") String name,
                                                         @RequestParam(value = "status", defaultValue = "") String status) {
        try {
            List<Bodywork> bodyworks = bodyworkService.searchByFilters(name, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", bodyworks));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }

    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseAnyDTO> edit(@PathVariable("id") Long id, @Valid @RequestBody BodyworkDTO bodyworks) {
        try {
            bodyworkService.update(id, bodyworks);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Carroceria editada com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable("id") Long id) {
        try {
            boolean result = bodyworkService.delete(id);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAnyDTO(204, "", "Carroceria deletada com sucesso", Collections.emptyList()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseAnyDTO(404, "Carroceria não encontrada", "", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
}