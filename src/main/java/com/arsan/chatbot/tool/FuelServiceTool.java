package com.arsan.chatbot.tool;

import com.arsan.chatbot.model.FuelSlip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class FuelServiceTool {

    @Tool(
            name = "getBlockInFuel",
            description = "Returns BigDecimal fuel quantity when aircraft arrives at gate. Used in FUEL QUANTITY CHECK and REMAINING FUEL VALIDATION steps. Requires regisNbr parameter."
    )
    public BigDecimal getBlockInFuel(String regisNbr) {
        log.info("Model fetched blockInFuel from FuelReportDTO for {}", regisNbr);
        return BigDecimal.valueOf(27000);
    }

    @Tool(
            name = "getFirstFuelSlip",
            description = "Returns FuelSlip object with fob (BigDecimal fuel on board) and location (String airport code). Used in FUEL QUANTITY CHECK and LOCATION CONSISTENCY steps. Requires regisNbr parameter."
    )
    public FuelSlip getFirstFuelSlip(String regisNbr) {
        log.info("Model fetched location from FirstFuelSlip for {}", regisNbr);
        FuelSlip fuelSlip = new FuelSlip();
        fuelSlip.setFob(BigDecimal.valueOf(27000));
        fuelSlip.setLocation("MEM");
        return fuelSlip;
    }

    @Tool(
            name = "getInRangeRemainingFuel",
            description = "Returns BigDecimal calculated remaining fuel within acceptable variance. Used in REMAINING FUEL VALIDATION step. Requires regisNbr parameter."
    )
    public BigDecimal getInRangeRemainingFuel(String regisNbr) {
        log.info("Model fetched inRangeRemainingFuel from EfitForteLegExt for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(
            name = "getAircraftLocation",
            description = "Returns String current aircraft parking location code (e.g., 'MEM', 'ARA'). Used in LOCATION CONSISTENCY step. Requires regisNbr parameter."
    )
    public String getAircraftLocation(String regisNbr) {
        log.info("Model fetched location from EfitAircraftExt for {}", regisNbr);
        return "ARA";
    }
}
