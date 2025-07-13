package com.onixx.apolloveiculos.api.Domains.Bodywork;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.validation.constraints.NotBlank;

public record BodyworkDTO(@NotBlank(message = "o nome da carroceria nao pode ser vazio") String name, Status status) {
}
