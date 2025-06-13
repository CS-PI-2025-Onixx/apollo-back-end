package com.onixx.apolloveiculos.api.Domains.Fuels;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.validation.constraints.NotBlank;

public record FuelsDTO(@NotBlank(message = "O nome do combustível não pode ser vazio!") String name, Status status) {
}
