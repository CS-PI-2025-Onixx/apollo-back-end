package com.onixx.apolloveiculos.api.Domains.Directions;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;

import jakarta.validation.constraints.NotBlank;

public record DirectionsDTO(@NotBlank(message = "o nome da direção não pode ser vazio")
        String name, Status status) {

}
