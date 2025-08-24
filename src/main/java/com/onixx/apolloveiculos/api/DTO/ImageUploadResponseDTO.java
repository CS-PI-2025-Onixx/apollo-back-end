package com.onixx.apolloveiculos.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponseDTO {
    private boolean success;
    private String message;
    private String imageUrl;
    private String[] imageUrls;
    private String error;
    private Integer count;
}
