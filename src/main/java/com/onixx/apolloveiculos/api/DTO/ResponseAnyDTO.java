package com.onixx.apolloveiculos.api.DTO;

import java.util.List;

public record ResponseAnyDTO(int status, String error, String message, List data) {
}
