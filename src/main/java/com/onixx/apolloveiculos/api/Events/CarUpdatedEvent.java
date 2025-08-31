package com.onixx.apolloveiculos.api.Events;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.OLXCarRequest.OLXCarParams;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CarUpdatedEvent extends ApplicationEvent {
    private final Cars car;
    private final OLXCarParams olxCarParams;

    public CarUpdatedEvent(Object source, Cars car, OLXCarParams olxCarParams) {
        super(source);
        this.car = car;
        this.olxCarParams = olxCarParams;
    }
}
