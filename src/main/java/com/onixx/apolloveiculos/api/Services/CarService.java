package com.onixx.apolloveiculos.api.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Images.Images;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarParams;
import com.onixx.apolloveiculos.api.Events.CarDeletedEvent;
import com.onixx.apolloveiculos.api.Events.CarUpdatedEvent;
import com.onixx.apolloveiculos.api.Repositories.CarsRepository;
import com.onixx.apolloveiculos.api.Events.CarCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Cars create(Cars car, List<MultipartFile> imageFiles, OLXCarParams olxCarParams, boolean publishOlx) {
        Cars savedCar = carsRepository.save(car);
        log.info("Carro salvo com ID: " + savedCar.getId_car());
        log.info("Processando upload de imagens..." + (imageFiles != null ? imageFiles.size() : 0) + " imagens recebidas.");
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imageUrls = uploadImagesToCloudinary(imageFiles);
            saveCarImages(savedCar, imageUrls);
        }
        Cars carWithImages = carsRepository.findByIdCarWithImages(savedCar.getId_car());

        if(publishOlx && olxCarParams != null){
            eventPublisher.publishEvent(new CarCreatedEvent(this, carWithImages, olxCarParams));
        }

        return carsRepository.findByIdCarWithImages(savedCar.getId_car());
    }

    public Cars findById(Long id) {
        return carsRepository.findByIdCarWithImages(id);
    }
    public List<Cars> findAll() {
        return carsRepository.findAllWithImages();
    }

    public Cars update(Long id, Cars carData, List<MultipartFile> newImageFiles, OLXCarParams olxCarParams, boolean publishOlx) {
        Cars existingCar = carsRepository.findbyIdCar(id);
        if (existingCar == null) {
            throw new RuntimeException("Carro não encontrado");
        }
        log.warn("Carro atualização" + carData);
        log.warn("Carro No banco" + existingCar);


        updateCarData(existingCar, carData);

        carData.setId_car(id);

        Cars updatedCar = carsRepository.save(existingCar);


         /* Código precisa de analise, pois se eu cadastrar uma imagem nova no front end
         não vai vir o img_url, por enquanto no front vou enviar apenas imagens novas
         não sendo possível editar as imagens que já foram cadastradas
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
         * */
        if (newImageFiles != null && !newImageFiles.isEmpty()) {
            List<String> newImageUrls = uploadImagesToCloudinary(newImageFiles);
            saveCarImages(updatedCar, newImageUrls);
        }

        // Publicar evento para atualizar na OLX (se já estava publicado)
        if (updatedCar.getOlxPublished() != null && updatedCar.getOlxPublished() && publishOlx) {
            eventPublisher.publishEvent(new CarUpdatedEvent(this, updatedCar, olxCarParams));
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
        // Publicar evento para remover da OLX (se estava publicado)
        if (car.getOlxPublished() != null && car.getOlxPublished()) {
            eventPublisher.publishEvent(new CarDeletedEvent(this, car));
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
                                    "resource_type", "image",
                                    "timeout", 60000, // 60 segundos timeout
                                    "retry_delay", 3000, // 3 segundos entre tentativas
                                    "max_retries", 3
                            ));
                    log.warn("resultado" + uploadResult);
                    String imageUrl = (String) uploadResult.get("secure_url");
                    imageUrls.add(imageUrl);

                } catch (IOException e) {
                    log.error(e.toString());
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

            if (newData.getDescription() != null && !newData.getDescription().trim().isEmpty()) {
                existingCar.setDescription(newData.getDescription().trim());
            }
            if (newData.getModel() != null && !newData.getModel().trim().isEmpty()) {
                existingCar.setModel(newData.getModel().trim());
            }
            if (newData.getColor() != null && !newData.getColor().trim().isEmpty()) {
                existingCar.setColor(newData.getColor().trim());
            }
            if (newData.getBrand() != null && !newData.getBrand().trim().isEmpty()) {
                existingCar.setBrand(newData.getBrand().trim());
            }
            if (newData.getFuel() != null && !newData.getFuel().trim().isEmpty()) {
                existingCar.setFuel(newData.getFuel().trim());
            }
            if (newData.getVehicleCondition() != null && !newData.getVehicleCondition().trim().isEmpty()) {
                existingCar.setVehicleCondition(newData.getVehicleCondition().trim());
            }
            if (newData.getLicensePlateEnd() != null) {
                existingCar.setLicensePlateEnd(newData.getLicensePlateEnd());
            }
            if(newData.getVehicleTag()!=null) {
                existingCar.setVehicleTag(newData.getVehicleTag().trim());
            }
            if(newData.getMotorPower()!=null) {
                existingCar.setMotorPower(newData.getMotorPower().trim());
            }
            if (newData.getTransmission() != null && !newData.getTransmission().trim().isEmpty()) {
                existingCar.setTransmission(newData.getTransmission().trim());
            }
            if (newData.getBodywork() != null && !newData.getBodywork().trim().isEmpty()) {
                existingCar.setBodywork(newData.getBodywork().trim());
            }
            if( newData.getDirection() !=null){
                existingCar.setDirection(newData.getDirection().trim());
            }

            if(newData.getCarType()!=null) {
                existingCar.setCarType(newData.getCarType());
            }

            if(newData.getVehicleStatus()!=null){
                existingCar.setVehicleStatus(newData.getVehicleStatus());
            }
            if(newData.getOpcionais() !=null){
                existingCar.setOpcionais(newData.getOpcionais());
            }
            // Campos numéricos com validação
            if (newData.getVehiclePrice() != null && newData.getVehiclePrice().compareTo(BigDecimal.ZERO) > 0) {
                existingCar.setVehiclePrice(newData.getVehiclePrice());
            }
            if (newData.getYear() != null && newData.getYear() > 1900 && newData.getYear() <= java.time.Year.now().getValue() + 1) {
                existingCar.setYear(newData.getYear());
            }
            if (newData.getMileage() != null && newData.getMileage() >= 0) {
                existingCar.setMileage(newData.getMileage());
            }
        if (newData.getAcceptsExchange() != null) existingCar.setAcceptsExchange(newData.getAcceptsExchange());

        existingCar.setTrade(newData.isTrade());
        existingCar.setArmored(newData.isArmored());
    }

    public void updateOlxInfo(Cars car) {
        carsRepository.save(car);
    }
}