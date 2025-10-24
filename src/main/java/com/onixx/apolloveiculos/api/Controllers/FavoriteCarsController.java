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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onixx.apolloveiculos.api.DTO.ResponseAnyDTO;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Cars.FavoriteCars;
import com.onixx.apolloveiculos.api.Services.FavoriteCarsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/favorite-cars")
public class FavoriteCarsController {

    @Autowired
    private FavoriteCarsService favoriteCarsService;

    @GetMapping("/fetch")
    public ResponseEntity<ResponseAnyDTO> getMyFavorites() {
        try {
            List<FavoriteCars> favorites = favoriteCarsService.getMyFavorites();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Favoritos encontrados com sucesso", favorites));
        } catch (IllegalStateException e) {
            log.error("Erro de autenticação ao buscar favoritos: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Unauthorized", e.getMessage(), Collections.emptyList()));
        } catch (Exception e) {
            log.error("Erro ao buscar favoritos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, "Internal Server Error", e.getMessage(), Collections.emptyList()));
        }
    }

    @GetMapping("/fetch-cars")
    public ResponseEntity<ResponseAnyDTO> getMyFavoriteCars() {
        try {
            List<Cars> cars = favoriteCarsService.getMyFavoriteCarsListByUser();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Carros favoritos encontrados com sucesso", cars));
        } catch (IllegalStateException e) {
            log.error("Erro de autenticação ao buscar carros favoritos: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Unauthorized", e.getMessage(), Collections.emptyList()));
        } catch (Exception e) {
            log.error("Erro ao buscar carros favoritos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, "Internal Server Error", e.getMessage(), Collections.emptyList()));
        }
    }

    @PostMapping("/toggle/{carId}")
    public ResponseEntity<ResponseAnyDTO> toggleFavorite(@PathVariable Long carId) {
        try {
            boolean isFavorited = favoriteCarsService.toggleFavorite(carId);
            String message = isFavorited ? "Carro adicionado aos favoritos" : "Carro removido dos favoritos";
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", message, Collections.singletonMap("isFavorited", isFavorited)));
        } catch (IllegalStateException e) {
            log.error("Erro de autenticação ao alternar favorito: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Unauthorized", e.getMessage(), Collections.emptyList()));
        } catch (RuntimeException e) {
            log.error("Erro ao alternar favorito: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, "Not Found", e.getMessage(), Collections.emptyList()));
        } catch (Exception e) {
            log.error("Erro interno ao alternar favorito: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, "Internal Server Error", e.getMessage(), Collections.emptyList()));
        }
    }

    @GetMapping("/is-favorite/{carId}")
    public ResponseEntity<ResponseAnyDTO> isFavorite(@PathVariable Long carId) {
        try {
            boolean isFavorite = favoriteCarsService.isFavorite(carId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "", Collections.singletonMap("isFavorite", isFavorite)));
        } catch (IllegalStateException e) {
            log.error("Erro de autenticação ao verificar favorito: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Unauthorized", e.getMessage(), Collections.emptyList()));
        } catch (RuntimeException e) {
            log.error("Erro ao verificar favorito: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseAnyDTO(404, "Not Found", e.getMessage(), Collections.emptyList()));
        } catch (Exception e) {
            log.error("Erro interno ao verificar favorito: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, "Internal Server Error", e.getMessage(), Collections.emptyList()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseAnyDTO> countMyFavorites() {
        try {
            long count = favoriteCarsService.countMyFavorites();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAnyDTO(200, "", "Contagem realizada com sucesso", Collections.singletonMap("count", count)));
        } catch (IllegalStateException e) {
            log.error("Erro de autenticação ao contar favoritos: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseAnyDTO(401, "Unauthorized", e.getMessage(), Collections.emptyList()));
        } catch (Exception e) {
            log.error("Erro ao contar favoritos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAnyDTO(500, "Internal Server Error", e.getMessage(), Collections.emptyList()));
        }
    }
}