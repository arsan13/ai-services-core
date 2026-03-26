package com.arsan.chatbot.tool;

import com.arsan.chatbot.dto.FuelSlip;
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
        log.info("Model fetched blockInFuel for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(
            name = "getFirstFuelSlip",
            description = "Returns FuelSlip object with fob (BigDecimal fuel on board) and location (String airport code). Used in FUEL QUANTITY CHECK, COMPLETION TIME CHECK and BAD FUEL SLIP CHECK steps. Requires regisNbr parameter."
    )
    public FuelSlip getFirstFuelSlip(String regisNbr) {
        log.info("Model fetched fuel slip for {}", regisNbr);
        FuelSlip fuelSlip = new FuelSlip();
        fuelSlip.setPreFob(BigDecimal.valueOf(22000));
        fuelSlip.setFob(BigDecimal.valueOf(25000));
        fuelSlip.setLocation("MEM");
        fuelSlip.setComputed(BigDecimal.valueOf(10000));
        fuelSlip.setIndicated(BigDecimal.valueOf(10000));
        fuelSlip.setMeteredFuel(BigDecimal.valueOf(1000));
        fuelSlip.setDensity(6.7);
        return fuelSlip;
    }

    @Tool(
            name = "getInRangeRemainingFuel",
            description = "Returns BigDecimal calculated remaining fuel within acceptable variance. Used in REMAINING FUEL VALIDATION step. Requires regisNbr parameter."
    )
    public BigDecimal getInRangeRemainingFuel(String regisNbr) {
        log.info("Model fetched inRangeRemainingFuel from EfitForteLegExt for {}", regisNbr);
        return BigDecimal.valueOf(25000);
    }

    @Tool(
            name = "getAircraftLocation",
            description = "Returns String current aircraft parking location code (e.g., 'MEM', 'ARA'). Used in COMPLETION TIME CHECK step. Requires regisNbr parameter."
    )
    public String getAircraftLocation(String regisNbr) {
        log.info("Model fetched location from EfitAircraftExt for {}", regisNbr);
        return "MEM";
    }

    @Tool(
            name = "isWrongAircraft",
            description = "Returns boolean indicating if the aircraft registration number is incorrect. Used in WRONG AIRCRAFT CHECK step. Requires regisNbr parameter."
    )
    public boolean isWrongAircraft(String regisNbr) {
        log.info("Model checked if {} is wrong aircraft", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingUpliftFuelSlip",
            description = "Returns boolean indicating if the uplift fuel slip is missing. Used in MISSING UPLIFT FUEL SLIP CHECK step. Requires regisNbr parameter."
    )
    public boolean isMissingUpliftFuelSlip(String regisNbr) {
        log.info("Model checked if uplift fuel slip is missing for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingApuRunFuelSlip",
            description = "Returns boolean indicating if the apu run fuel slip is missing. Used in MISSING APU RUN FUEL SLIP CHECK step. Requires regisNbr parameter."
    )
    public boolean isMissingApuRunFuelSlip(String regisNbr) {
        log.info("Model checked if APU run fuel slip is missing for {}", regisNbr);
        return false;
    }
}
