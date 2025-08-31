package com.onixx.apolloveiculos.api.Events;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CarDeletedEvent extends ApplicationEvent {
    private final Cars car;

    public CarDeletedEvent(Object source, Cars car) {
        super(source);
        this.car = car;
    }
}