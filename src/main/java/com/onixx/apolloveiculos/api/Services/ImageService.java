package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Images.Images;
import com.onixx.apolloveiculos.api.Repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Images create (String url, Cars car){
        Images image = new Images();
        image.setImg_url(url);
        image.setCar(car);
        return imageRepository.save(image);
    }

    public void delete(Images image){
        imageRepository.delete(image);
        
    }
}
