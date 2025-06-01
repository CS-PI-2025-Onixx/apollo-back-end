package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Motors.MotorsDTO;
import com.onixx.apolloveiculos.api.Services.MotorsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;



@RestController
@RequestMapping("/motors")
public class MotorsController {

    private MotorsService motorsService;

    public MotorsController(MotorsService motorsService) {
        this.motorsService =  motorsService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseAnyDTO> search(){
        List<Motors> motors = motorsService.search();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", motors));
    }
    @GetMapping("/fetch-by-filters")
    public ResponseEntity<ResponseAnyDTO> fetchByFilters(@RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value="status", defaultValue = "") String status) {
        try {
        List<Motors> motors = motorsService.searchByFilters(name, status);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "", motors));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }

    }

    @PostMapping("/create")
    public
    ResponseEntity<ResponseAnyDTO> create(@Valid @RequestBody MotorsDTO motors){
        try {
        motorsService.save(motors);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseAnyDTO(200, "", "Motor cadastrado com sucesso", Collections.emptyList()));
        } catch (Exception e) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));}
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseAnyDTO> edit(@PathVariable("id") Long id,  @Valid @RequestBody MotorsDTO motors){
        try {
            motorsService.update(id, motors);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(200, "", "Motor editado com sucesso", Collections.emptyList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseAnyDTO> delete(@PathVariable("id") Long id){
        try {
            boolean result = motorsService.delete(id);
            if(result){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAnyDTO(204, "", "Motor deletado com sucesso", Collections.emptyList()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseAnyDTO(404, "Motor não encontrado", "", Collections.emptyList()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAnyDTO(400, "Bad Request", e.getMessage(), Collections.emptyList()));
        }
    }
}
