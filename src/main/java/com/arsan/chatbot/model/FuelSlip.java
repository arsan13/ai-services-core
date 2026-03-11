package com.arsan.chatbot.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FuelSlip {
    private BigDecimal fob;
    private String location;
}
