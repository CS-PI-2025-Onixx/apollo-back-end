package com.onixx.apolloveiculos.api.Domains.Standard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class Standard {
    @Column(name = "dt_create")
    private LocalDateTime dt_create;
    @JsonIgnore
    @Column(name = "dt_atualization")
    private LocalDateTime dt_atualization;
    @JsonIgnore
    @Column(name = "dt_delete")
    private LocalDateTime dt_delete;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.active;

    @PrePersist
    protected void onCreate() {
        dt_create = LocalDateTime.now();;
    }

    @PreUpdate
    protected void onUpdate() {
        dt_atualization = LocalDateTime.now();
    }
}