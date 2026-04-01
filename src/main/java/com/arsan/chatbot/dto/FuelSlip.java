package com.arsan.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FuelSlip {
    private BigDecimal preFob;
    private BigDecimal fob;
    private String location;
    private BigDecimal computed;
    private BigDecimal indicated;
    private BigDecimal meteredFuel;
    private Double density;
}
