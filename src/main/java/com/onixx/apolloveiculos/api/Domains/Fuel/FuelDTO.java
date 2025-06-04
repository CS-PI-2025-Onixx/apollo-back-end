package com.onixx.apolloveiculos.api.Domains.Fuel;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.validation.constraints.NotBlank;

public record FuelDTO(@NotBlank(message = "O nome do combustível não pode ser vazio!") String name, Status status){

}
