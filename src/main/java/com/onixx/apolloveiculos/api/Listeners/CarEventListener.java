package com.onixx.apolloveiculos.api.Listeners;

import com.onixx.apolloveiculos.api.Events.CarCreatedEvent;
import com.onixx.apolloveiculos.api.Events.CarUpdatedEvent;
import com.onixx.apolloveiculos.api.Events.CarDeletedEvent;
import com.onixx.apolloveiculos.api.Services.CarService;
import com.onixx.apolloveiculos.api.Services.OLXIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CarEventListener {

    private static final Logger logger = LoggerFactory.getLogger(CarEventListener.class);

    @Autowired
    private OLXIntegrationService olxIntegrationService;

    @Autowired
    private CarService carService;

    @EventListener
    @Async
    public void handleCarCreated(CarCreatedEvent event) {
        try {
            olxIntegrationService.publishCar(event.getCar(), event.getOlxCarParams());
            carService.updateOlxInfo(event.getCar());

        } catch (Exception e) {
            logger.error("Erro ao publicar carro {} na OLX: {}", event.getCar().getId_car(), e.getMessage());
            carService.updateOlxInfo(event.getCar());
        }
    }

    @EventListener
    @Async
    public void handleCarUpdated(CarUpdatedEvent event) {
        try {
            olxIntegrationService.updateCar(event.getCar(), event.getOlxCarParams());
            carService.updateOlxInfo(event.getCar());

        } catch (Exception e) {
            logger.error("Erro ao atualizar carro {} na OLX: {}", event.getCar().getId_car(), e.getMessage());
            carService.updateOlxInfo(event.getCar());
        }
    }

    @EventListener
    @Async
    public void handleCarDeleted(CarDeletedEvent event) {
        try {
            olxIntegrationService.deleteCar(event.getCar());
            carService.updateOlxInfo(event.getCar());

        } catch (Exception e) {
            logger.error("Erro ao remover carro {} da OLX: {}", event.getCar().getId_car(), e.getMessage());
            carService.updateOlxInfo(event.getCar());
        }
    }
}