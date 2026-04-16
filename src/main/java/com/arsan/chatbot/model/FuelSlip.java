package com.arsan.chatbot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class FuelSlip {
    private BigDecimal preFob;
    private BigDecimal fob;
    private String location;
    private BigDecimal computed;
    private BigDecimal indicated;
    private BigDecimal meteredFuel;
    private Double density;
}
