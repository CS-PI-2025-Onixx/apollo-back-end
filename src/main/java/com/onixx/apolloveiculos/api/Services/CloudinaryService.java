package com.onixx.apolloveiculos.api.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Faz upload de uma imagem para o Cloudinary
     * @param file Arquivo de imagem a ser enviado
     * @param folder Pasta onde a imagem será armazenada (opcional)
     * @return URL da imagem no Cloudinary
     * @throws IOException Em caso de erro no upload
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "resource_type", "image",
                "folder", folder != null ? folder : "apollo_veiculos"
        );

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Faz upload de uma imagem para o Cloudinary na pasta padrão
     * @param file Arquivo de imagem a ser enviado
     * @return URL da imagem no Cloudinary
     * @throws IOException Em caso de erro no upload
     */
    public String uploadImage(MultipartFile file) throws IOException {
        return uploadImage(file, null);
    }

    /**
     * Deleta uma imagem do Cloudinary
     * @param publicId ID público da imagem no Cloudinary
     * @return Resultado da operação de delete
     * @throws IOException Em caso de erro na operação
     */
    public Map<String, Object> deleteImage(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    /**
     * Extrai o public_id de uma URL do Cloudinary
     * @param imageUrl URL da imagem no Cloudinary
     * @return Public ID da imagem
     */
    public String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        // Extrai o public_id da URL
        String[] parts = imageUrl.split("/");
        String fileName = parts[parts.length - 1];

        // Remove a extensão do arquivo
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    /**
     * Faz upload de múltiplas imagens
     * @param files Array de arquivos de imagem
     * @param folder Pasta onde as imagens serão armazenadas
     * @return Array com URLs das imagens no Cloudinary
     * @throws IOException Em caso de erro no upload
     */
    public String[] uploadMultipleImages(MultipartFile[] files, String folder) throws IOException {
        String[] urls = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            urls[i] = uploadImage(files[i], folder);
        }

        return urls;
    }
}
