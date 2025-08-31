package com.onixx.apolloveiculos.api.Services;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarParams;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OLXIntegrationService {

    public void publishCar(Cars car, OLXCarParams olxCarParams) {
        try {
            OLXCarRequest olxRequest = OLXCarRequest.fromCars(car, olxCarParams);

            String olxAdId = callOLXAPI(olxRequest);

            car.setOlxAdId(olxAdId);
            car.setOlxPublished(true);
            car.setOlxPublishedAt(LocalDateTime.now());

        } catch (Exception e) {
            car.setOlxPublished(false);
            car.setOlxError(e.getMessage());
            throw e;
        }
    }


    public void updateCar(Cars car, OLXCarParams olxCarParams) {
        try {
            if (car.getOlxAdId() == null) {
                throw new RuntimeException("Carro não possui anúncio na OLX");
            }

            OLXCarRequest olxRequest = OLXCarRequest.fromCars(car, olxCarParams);
            olxRequest.setOperation("update");
            olxRequest.setId(car.getOlxAdId());

            callOLXAPI(olxRequest);

            car.setOlxUpdatedAt(LocalDateTime.now());
            car.setOlxError(null);

        } catch (Exception e) {
            car.setOlxError(e.getMessage());
            throw e;
        }
    }

    public void deleteCar(Cars car) {
        try {
            if (car.getOlxAdId() == null) {
                throw new RuntimeException("Carro não possui anúncio na OLX");
            }

            OLXCarRequest olxRequest = new OLXCarRequest();
            olxRequest.setOperation("delete");
            olxRequest.setId(car.getOlxAdId());

            callOLXAPI(olxRequest);

            car.setOlxPublished(false);
            car.setOlxDeletedAt(LocalDateTime.now());
            car.setOlxError(null);

        } catch (Exception e) {
            car.setOlxError(e.getMessage());
            throw e;
        }
    }

    private String callOLXAPI(OLXCarRequest request) {
        // Simular chamada para API OLX
        System.out.println("Operação OLX: " + request.getOperation());
        return "olx_ad_" + System.currentTimeMillis();
    }
}