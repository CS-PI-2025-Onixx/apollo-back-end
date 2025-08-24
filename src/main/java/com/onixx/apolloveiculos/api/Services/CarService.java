package com.onixx.apolloveiculos.api.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Images.Images;
import com.onixx.apolloveiculos.api.Repositories.CarsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CarService {

    @Autowired
    private CarsRepository carsRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Cloudinary cloudinary;

    public Cars create(Cars car, List<MultipartFile> imageFiles) {
        Cars savedCar = carsRepository.save(car);
        log.info("Carro salvo com ID: " + savedCar.getId_car());
        log.info("Processando upload de imagens..." + (imageFiles != null ? imageFiles.size() : 0) + " imagens recebidas.");
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imageUrls = uploadImagesToCloudinary(imageFiles);
            saveCarImages(savedCar, imageUrls);
        }

        return carsRepository.findByIdCarWithImages(savedCar.getId_car());
    }

    public Cars findById(Long id) {
        return carsRepository.findByIdCarWithImages(id);
    }
    public List<Cars> findAll() {
        return carsRepository.findAllWithImages();
    }

    public Cars update(Long id, Cars carData, List<MultipartFile> newImageFiles) {
        Cars existingCar = carsRepository.findByIdCarWithImages(id);
        if (existingCar == null) {
            throw new RuntimeException("Carro não encontrado");
        }

        updateCarData(existingCar, carData);
        Cars updatedCar = carsRepository.save(existingCar);

        if (carData.getImages() != null) {
            List<String> newImageUrls = carData.getImages().stream()
                    .map(Images::getImg_url)
                    .toList();

            existingCar.getImages().stream()
                    .filter(image -> !newImageUrls.contains(image.getImg_url()))
                    .forEach(image -> {
                        deleteImageFromCloudinary(image.getImg_url());
                        imageService.delete(image);
                    });
        }

        if (newImageFiles != null && !newImageFiles.isEmpty()) {
            List<String> newImageUrls = uploadImagesToCloudinary(newImageFiles);
            saveCarImages(updatedCar, newImageUrls);
        }

        return carsRepository.findByIdCarWithImages(updatedCar.getId_car());
    }

    public void delete(Long id) {
        Cars car = carsRepository.findByIdCarWithImages(id);
        if (car == null) {
            throw new RuntimeException("Carro não encontrado");
        }

        if (car.getImages() != null) {
            for (Images image : car.getImages()) {
                deleteImageFromCloudinary(image.getImg_url());
                imageService.delete(image);
            }
        }

        carsRepository.deleteById(id);
    }

    public List<Cars> findByFilters(String brand, String model, String color,
                                   Integer yearMin, Integer yearMax,
                                   BigDecimal priceMin, BigDecimal priceMax,
                                   String fuel, String vehicleCondition) {
        return carsRepository.findAll().stream()
                .filter(car -> brand == null || car.getBrand().toLowerCase().contains(brand.toLowerCase()))
                .filter(car -> model == null || car.getModel().toLowerCase().contains(model.toLowerCase()))
                .filter(car -> color == null || car.getColor().toLowerCase().contains(color.toLowerCase()))
                .filter(car -> yearMin == null || car.getYear() >= yearMin)
                .filter(car -> yearMax == null || car.getYear() <= yearMax)
                .filter(car -> priceMin == null || car.getVehiclePrice().compareTo(priceMin) >= 0)
                .filter(car -> priceMax == null || car.getVehiclePrice().compareTo(priceMax) <= 0)
                .filter(car -> fuel == null || car.getFuel().toLowerCase().contains(fuel.toLowerCase()))
                .filter(car -> vehicleCondition == null || car.getVehicleCondition().equals(vehicleCondition))
                .toList();
    }


    private List<String> uploadImagesToCloudinary(List<MultipartFile> imageFiles) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", "cars",
                                    "resource_type", "image"
                            ));

                    String imageUrl = (String) uploadResult.get("secure_url");
                    imageUrls.add(imageUrl);

                } catch (IOException e) {
                    throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage());
                }
            }
        }

        return imageUrls;
    }

    private void saveCarImages(Cars car, List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            imageService.create(imageUrl, car);
        }
    }

    private void deleteImageFromCloudinary(String imageUrl) {
        try {
            // Extrair public_id da URL do Cloudinary
            String publicId = extractPublicIdFromUrl(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            System.err.println("Erro ao deletar imagem do Cloudinary: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        // Lógica para extrair o public_id da URL do Cloudinary
        String[] parts = imageUrl.split("/");
        String filename = parts[parts.length - 1];
        return "cars/" + filename.substring(0, filename.lastIndexOf('.'));
    }

    private void updateCarData(Cars existingCar, Cars newData) {
        if (newData.getDescription() != null) existingCar.setDescription(newData.getDescription());
        if (newData.getVehiclePrice() != null) existingCar.setVehiclePrice(newData.getVehiclePrice());
        if (newData.getYear() != null) existingCar.setYear(newData.getYear());
        if (newData.getMileage() != null) existingCar.setMileage(newData.getMileage());
        if (newData.getModel() != null) existingCar.setModel(newData.getModel());
        if (newData.getColor() != null) existingCar.setColor(newData.getColor());
        if (newData.getBrand() != null) existingCar.setBrand(newData.getBrand());
        if (newData.getFuel() != null) existingCar.setFuel(newData.getFuel());
        if (newData.getVehicleCondition() != null) existingCar.setVehicleCondition(newData.getVehicleCondition());
        existingCar.setTrade(newData.isTrade());
        existingCar.setArmored(newData.isArmored());
    }
}