package com.onixx.apolloveiculos.api.Domains.Cars;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.onixx.apolloveiculos.api.Domains.Bodywork.Bodywork;
import com.onixx.apolloveiculos.api.Domains.Colors.Colors;
import com.onixx.apolloveiculos.api.Domains.Directions.Directions;
import com.onixx.apolloveiculos.api.Domains.Fuels.Fuels;
import com.onixx.apolloveiculos.api.Domains.Images.Images;
import com.onixx.apolloveiculos.api.Domains.Models.Models;
import com.onixx.apolloveiculos.api.Domains.Motors.Motors;
import com.onixx.apolloveiculos.api.Domains.Standard.Standard;
import com.onixx.apolloveiculos.api.Domains.Traction.Traction;
import com.onixx.apolloveiculos.api.Domains.Transmissions.Transmissions;
import com.onixx.apolloveiculos.api.Domains.User.User;
import com.onixx.apolloveiculos.api.Utils.ENUM_CONDITION;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "licensePlateEnd")
    private Byte licensePlateEnd;

    @Column(name = "trade")
    private boolean trade;

    @Column(name = "acceptsExchange")
    private String acceptsExchange;

    @Column(name = "armored")
    private boolean armored = false;

    @Column(name = "vehiclePrice")
    private BigDecimal vehiclePrice;

    @Column(name = "year")
    private Integer year;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "vehicleCondition")
    private String vehicleCondition = String.valueOf(ENUM_CONDITION.NOVO);

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

    @Column(name ="vehicleStatus")
    @Enumerated(EnumType.STRING)
    private VehiclesStatus vehicleStatus = VehiclesStatus.DISPONIVEL;

    @Column(name = "carType")
    @Enumerated(EnumType.STRING)
    private VehicleTypes carType = VehicleTypes.VENDA;

    @Column(name = "motorPower")
    private String motorPower;

    @Column(name= "vehicleTag")
    private String vehicleTag;

    @Column(name = "vehicleStatusChangedAt")
    private LocalDateTime vehicleStatusChangedAt;


    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Images> images;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;

    @Formula("(SELECT u.name FROM tb_user u WHERE u.id_user = id_user)")
    private String userName;


    @ElementCollection
    @CollectionTable(name = "tb_cars_opcionais", joinColumns = @JoinColumn(name = "id_car"))
    @Column(name = "opcional")
    @JsonManagedReference
    private List<String> opcionais;

    @Column(name = "dt_sale")
    private LocalDateTime dtSale;

    @Column(name = "dt_rent")
    private LocalDateTime dtRent;




    // Integration with OLX

    @Column(name = "olx_ad_id")
    private String olxAdId;

    @Column(name = "olx_published")
    private Boolean olxPublished = false;

    @Column(name = "olx_published_at")
    private LocalDateTime olxPublishedAt;

    @Column(name = "olx_error")
    private String olxError;

    @Column(name = "olx_update_at")
    private LocalDateTime olxUpdatedAt;
    @Column(name = "olx_deleted_at")
    private LocalDateTime olxDeletedAt;
}