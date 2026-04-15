package com.arsan.chatbot.model;

import lombok.Data;

@Data
public class FuelDetails {
    private Double preTotalFob;
    private Double preTank1Fob;
    private Double preTank2Fob;
    private Double preTank3Fob;

    private Double totalFob;
    private Double tank1Fob;
    private Double tank2Fob;
    private Double tank3Fob;
}
