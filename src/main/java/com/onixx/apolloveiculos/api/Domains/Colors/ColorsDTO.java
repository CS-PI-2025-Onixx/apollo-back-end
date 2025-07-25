package com.onixx.apolloveiculos.api.Domains.Colors;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.validation.constraints.NotBlank;

public record ColorsDTO(@NotBlank(message = "o nome da cor não pode ser vazio") String name, Status status){
}
