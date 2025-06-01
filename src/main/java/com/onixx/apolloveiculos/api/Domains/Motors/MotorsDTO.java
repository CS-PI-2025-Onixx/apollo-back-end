package com.onixx.apolloveiculos.api.Domains.Motors;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.validation.constraints.NotBlank;

public record MotorsDTO(@NotBlank(message = "o nome do motor não pode ser vazio") String name, Status status) {
}
