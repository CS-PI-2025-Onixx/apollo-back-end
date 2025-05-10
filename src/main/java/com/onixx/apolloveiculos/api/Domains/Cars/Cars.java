package com.onixx.apolloveiculos.api.Domains.Cars;

import com.onixx.apolloveiculos.api.Domains.*;
import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Direction.Direction;
import com.onixx.apolloveiculos.api.Domains.Fuel.Fuel;
import com.onixx.apolloveiculos.api.Domains.Models.Models;
import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Traction.Traction;
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

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_model")
    private Models model;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_color")
    private Colors color;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_direction")
    private Direction direction;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_bodywork")
    private Bodywork bodywork;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_fuel")
    private Fuel fuel;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_traction")
    private Traction traction;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_motor")
    private Motors motor;


}