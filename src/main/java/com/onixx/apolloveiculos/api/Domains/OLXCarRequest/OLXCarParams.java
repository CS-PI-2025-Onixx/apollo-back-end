package com.onixx.apolloveiculos.api.Domains.OLXCarRequest;


import lombok.Data;
import java.util.List;

@Data
public class OLXCarParams {
    private String phone;
    private String zipcode;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleVersion;
    private String gearbox;
    private String fuel;
    private String doors;
    private String carSteering;
    private String carColor;
    private List<String> financial;
    private String financialStatus;
    private String renavam;
    private String vehicleTag;
    private String vehicleHistory;
    private String cpfCnpj;
    private String carType;
    private String motorPower;
    private List<String> carFeatures;
}