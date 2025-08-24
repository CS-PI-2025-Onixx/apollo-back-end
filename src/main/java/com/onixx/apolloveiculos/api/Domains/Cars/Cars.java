package com.onixx.apolloveiculos.api.Domains.Cars;

import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Direction.Direction;
import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Models.Models;
import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import com.onixx.apolloveiculos.api.Domains.Traction.Traction;
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Utils.ENUM_CONDITION;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_cars")
@SQLDelete(sql = "UPDATE tb_cars SET dt_delete = CURRENT_TIMESTAMP WHERE id_car = ?")
@Where(clause = "dt_delete IS NULL")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Cars extends Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_car")
    private Long id_car;

    @Column(name = "description")
    private String description;

    @Column(name = "highlighted")
    private boolean highlighted;

    @Column(name = "final_plate")
    private Byte finalPlate;

    @Column(name = "trade")
    private boolean trade;

    @Column(name = "armored")
    private boolean armored = false;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "kilometers")
    private Integer kilometers;

    @Column(name = "vehicle_condition")
    String vehicleCondition = String.valueOf(ENUM_CONDITION.NOVO);

//    @ElementCollection
//    @CollectionTable(name = "tb_cars_opcionais", joinColumns = @JoinColumn(name = "id_car"))
//    @Column(name = "opcional")
//    private List<String> opcionais = new ArrayList<>();
    @Column(name = "model")
    private String model;
    @Column(name = "color")
    private String color;

    @Column(name = "direction")
    private String direction;

    @Column(name = "bodywork")
    private String bodywork;

    @Column(name = "fuel")
    private String fuel;

    @Column(name = "traction")
    private String traction;

    @Column(name = "motor")
    private String motor;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "brand")
    private String brand;
}