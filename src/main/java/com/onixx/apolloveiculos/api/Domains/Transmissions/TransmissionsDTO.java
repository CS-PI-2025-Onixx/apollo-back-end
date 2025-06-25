package com.onixx.apolloveiculos.api.Domains.Transmissions;

import com.onixx.apolloveiculos.api.Domains.Standard.Status;
import jakarta.validation.constraints.NotBlank;

public record TransmissionsDTO(@NotBlank(message = "O nome da transmissão não pode ser vazio") String name, Status status) {
    
}
