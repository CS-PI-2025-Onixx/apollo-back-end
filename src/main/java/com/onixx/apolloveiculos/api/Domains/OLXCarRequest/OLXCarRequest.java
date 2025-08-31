package com.onixx.apolloveiculos.api.Domains.OLXCarRequest;

import com.onixx.apolloveiculos.api.Domains.Cars.Cars;
import com.onixx.apolloveiculos.api.Domains.Images.Images;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OLXCarRequest {
    private String id;
    private String operation = "insert";
    private String category = "2020";
    private String subject;
    private String body;
    private String phone;
    private String type = "s";
    private Integer price;
    private String zipcode;
    private Map<String, Object> params;
    private List<String> images;

    // Método estático para converter Cars + dados OLX
    public static OLXCarRequest fromCars(Cars car, OLXCarParams olxParams) {
        OLXCarRequest olxRequest = new OLXCarRequest();

        // Dados básicos
        olxRequest.setId(generateId());
        olxRequest.setSubject(car.getModel() + " " + car.getYear());
        olxRequest.setBody(car.getDescription());
        olxRequest.setPrice(car.getVehiclePrice().intValue());
        olxRequest.setPhone(olxParams.getPhone());
        olxRequest.setZipcode(olxParams.getZipcode());

        Map<String, Object> params = new HashMap<>();
        params.put("vehicle_brand", olxParams.getVehicleBrand());
        params.put("vehicle_model", olxParams.getVehicleModel());
        params.put("vehicle_version", olxParams.getVehicleVersion());
        params.put("regdate", car.getYear().toString());
        params.put("gearbox", olxParams.getGearbox());
        params.put("fuel", olxParams.getFuel());
        params.put("mileage", car.getMileage());
        params.put("doors", olxParams.getDoors());
        params.put("car_steering", olxParams.getCarSteering());
        params.put("carcolor", olxParams.getCarColor());
        params.put("financial", olxParams.getFinancial());
        params.put("financial_status", olxParams.getFinancialStatus());
        params.put("renavam", olxParams.getRenavam());
        params.put("vehicle_tag", olxParams.getVehicleTag());
        params.put("vehicle_history", olxParams.getVehicleHistory());
        params.put("cpf_cnpj", olxParams.getCpfCnpj());
        params.put("cartype", olxParams.getCarType());
        params.put("motorpower", olxParams.getMotorPower());
        params.put("car_features", olxParams.getCarFeatures());
        olxRequest.setParams(params);
        if (car.getImages() != null) {
            olxRequest.setImages(car.getImages().stream()
                    .map(img -> img.getImg_url())
                    .toList());
        } else {
            olxRequest.setImages(Collections.emptyList());
        }
        return olxRequest;
    }

    private static String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }
}