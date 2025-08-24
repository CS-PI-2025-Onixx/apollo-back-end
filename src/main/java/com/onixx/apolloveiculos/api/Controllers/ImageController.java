package com.onixx.apolloveiculos.api.Controllers;

import com.onixx.apolloveiculos.api.Services.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageController {

    private final CloudinaryService cloudinaryService;

    /**
     * Endpoint para upload de uma única imagem
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validações básicas
            if (file.isEmpty()) {
                response.put("error", "Arquivo não pode estar vazio");
                return ResponseEntity.badRequest().body(response);
            }

            // Verifica se é uma imagem
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("error", "Arquivo deve ser uma imagem");
                return ResponseEntity.badRequest().body(response);
            }

            // Faz o upload
            String imageUrl = cloudinaryService.uploadImage(file, folder);

            response.put("success", true);
            response.put("imageUrl", imageUrl);
            response.put("message", "Imagem enviada com sucesso");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Erro ao fazer upload da imagem: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para upload de múltiplas imagens
     */
    @PostMapping("/upload-multiple")
    public ResponseEntity<Map<String, Object>> uploadMultipleImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folder", required = false) String folder) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validações básicas
            if (files.length == 0) {
                response.put("error", "Nenhum arquivo foi enviado");
                return ResponseEntity.badRequest().body(response);
            }

            // Verifica se todos são imagens
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    response.put("error", "Um ou mais arquivos estão vazios");
                    return ResponseEntity.badRequest().body(response);
                }

                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    response.put("error", "Todos os arquivos devem ser imagens");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            // Faz o upload
            String[] imageUrls = cloudinaryService.uploadMultipleImages(files, folder);

            response.put("success", true);
            response.put("imageUrls", imageUrls);
            response.put("message", "Imagens enviadas com sucesso");
            response.put("count", imageUrls.length);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Erro ao fazer upload das imagens: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para deletar uma imagem
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        Map<String, Object> response = new HashMap<>();

        try {
            String publicId = cloudinaryService.extractPublicIdFromUrl(imageUrl);

            if (publicId == null) {
                response.put("error", "URL da imagem inválida");
                return ResponseEntity.badRequest().body(response);
            }

            Map<String, Object> deleteResult = cloudinaryService.deleteImage(publicId);

            response.put("success", true);
            response.put("message", "Imagem deletada com sucesso");
            response.put("result", deleteResult);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Erro ao deletar imagem: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
