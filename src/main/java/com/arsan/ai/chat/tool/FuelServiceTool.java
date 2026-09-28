package com.arsan.ai.chat.tool;

import com.arsan.ai.chat.model.aircraft.FuelDetails;
import com.arsan.ai.chat.model.aircraft.FuelGapType;
import com.arsan.ai.chat.model.aircraft.FuelSlip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Slf4j
@Component
public class FuelServiceTool {

    private static final Pattern REGIS_NBR_PATTERN = Pattern.compile("^N\\d{3}[A-Z]{2}$");
    private static final BigDecimal FUEL_TOLERANCE_LBS = BigDecimal.valueOf(50);

    private void validateRegisNbr(String regisNbr) {
        if (regisNbr == null || !REGIS_NBR_PATTERN.matcher(regisNbr).matches()) {
            throw new IllegalArgumentException(
                    "Invalid aircraft registration/tail number: '" + regisNbr
                            + "'. Expected format is 'N' followed by 3 digits and 2 uppercase letters, e.g. 'N121FE'.");
        }
    }

    @Tool(
            name = "getBlockInFuel",
            description = "Returns the block-in fuel quantity, in pounds (lbs), on board when the aircraft arrives at the gate. Used in FUEL QUANTITY CHECK and REMAINING FUEL VALIDATION steps."
    )
    public BigDecimal getBlockInFuel(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model fetched blockInFuel for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(
            name = "getFirstFuelSlip",
            description = "Returns the first FuelSlip recorded for the aircraft. All fuel quantity fields are in pounds (lbs). Used in FUEL QUANTITY CHECK, COMPLETION TIME CHECK and BAD FUEL SLIP CHECK steps."
    )
    public FuelSlip getFirstFuelSlip(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model fetched fuel slip for {}", regisNbr);
        FuelSlip fuelSlip = new FuelSlip();
        fuelSlip.setPreFob(BigDecimal.valueOf(22000));
        fuelSlip.setFob(BigDecimal.valueOf(25000));
        fuelSlip.setLocation("MEM");
        fuelSlip.setComputed(BigDecimal.valueOf(10000));
        fuelSlip.setIndicated(BigDecimal.valueOf(10000));
        fuelSlip.setMeteredFuel(BigDecimal.valueOf(1000));
        fuelSlip.setDensity(7.9);
        return fuelSlip;
    }

    @Tool(
            name = "getInRangeRemainingFuel",
            description = "Returns the calculated remaining fuel, in pounds (lbs), within acceptable variance. Used in REMAINING FUEL VALIDATION step."
    )
    public BigDecimal getInRangeRemainingFuel(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model fetched inRangeRemainingFuel from EfitForteLegExt for {}", regisNbr);
        return BigDecimal.valueOf(25000);
    }

    @Tool(
            name = "getAircraftLocation",
            description = "Returns the current aircraft parking location code (e.g., 'MEM', 'ARA'). Used in COMPLETION TIME CHECK step."
    )
    public String getAircraftLocation(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model fetched location from EfitAircraftExt for {}", regisNbr);
        return "MEM";
    }

    @Tool(
            name = "isWrongAircraft",
            description = "Returns true if the given aircraft registration number is incorrect. Used in WRONG AIRCRAFT CHECK step."
    )
    public boolean isWrongAircraft(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model checked if {} is wrong aircraft", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingUpliftFuelSlip",
            description = "Returns true if the uplift fuel slip is missing for the aircraft. Used in MISSING UPLIFT FUEL SLIP CHECK step."
    )
    public boolean isMissingUpliftFuelSlip(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model checked if uplift fuel slip is missing for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingApuRunFuelSlip",
            description = "Returns true if the APU run fuel slip is missing for the aircraft. Used in MISSING APU RUN FUEL SLIP CHECK step."
    )
    public boolean isMissingApuRunFuelSlip(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model checked if APU run fuel slip is missing for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "getAcarsFuelDetails",
            description = "Returns ACARS fuel details for the aircraft. All fuel quantity fields are in pounds (lbs)."
    )
    public FuelDetails getAcarsFuelDetails(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        log.info("Model fetched ACARS fuel details for {}", regisNbr);
        FuelDetails fuelDetails = new FuelDetails();
        fuelDetails.setPreTotalFob(22000.0);
        fuelDetails.setPreTank1Fob(10000.0);
        fuelDetails.setPreTank2Fob(12000.0);
        fuelDetails.setPreTank3Fob(0.0);
        fuelDetails.setTotalFob(25000.0);
        fuelDetails.setTank1Fob(15000.0);
        fuelDetails.setTank2Fob(10000.0);
        fuelDetails.setTank3Fob(0.0);
        return fuelDetails;
    }

    @Tool(
            name = "isFuelQuantityMatching",
            description = "Returns true if the block-in fuel quantity matches the fuel slip's pre-FOB, within acceptable tolerance. Used in FUEL QUANTITY GAP CHECK step."
    )
    public boolean isFuelQuantityMatching(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        BigDecimal blockInFuel = getBlockInFuel(regisNbr);
        BigDecimal preFob = getFirstFuelSlip(regisNbr).getPreFob();
        boolean matching = blockInFuel.subtract(preFob).abs().compareTo(FUEL_TOLERANCE_LBS) <= 0;
        log.info("Model checked if fuel quantity is matching for {}: {}", regisNbr, matching);
        return matching;
    }

    @Tool(
            name = "isBlockInFuelCorrect",
            description = "Returns true if the block-in fuel quantity matches the in-range remaining fuel calculation, within acceptable tolerance. Used in BAD BLOCK-IN FUEL CHECK step."
    )
    public boolean isBlockInFuelCorrect(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        BigDecimal blockInFuel = getBlockInFuel(regisNbr);
        BigDecimal inRangeRemainingFuel = getInRangeRemainingFuel(regisNbr);
        boolean correct = blockInFuel.subtract(inRangeRemainingFuel).abs().compareTo(FUEL_TOLERANCE_LBS) <= 0;
        log.info("Model checked if block-in fuel is correct for {}: {}", regisNbr, correct);
        return correct;
    }

    @Tool(
            name = "getFuelGapType",
            description = "Returns the type of fuel gap (NONE, POSITIVE or NEGATIVE) between the fuel slip's pre-FOB and the block-in fuel, within acceptable tolerance. Used in GAP TYPE CHECK step."
    )
    public FuelGapType getFuelGapType(
            @ToolParam(description = "Aircraft registration/tail number, e.g. 'N121FE'") String regisNbr) {
        validateRegisNbr(regisNbr);
        BigDecimal blockInFuel = getBlockInFuel(regisNbr);
        BigDecimal preFob = getFirstFuelSlip(regisNbr).getPreFob();
        BigDecimal gap = preFob.subtract(blockInFuel);
        FuelGapType gapType;
        if (gap.abs().compareTo(FUEL_TOLERANCE_LBS) <= 0) {
            gapType = FuelGapType.NONE;
        } else if (gap.signum() > 0) {
            gapType = FuelGapType.POSITIVE;
        } else {
            gapType = FuelGapType.NEGATIVE;
        }
        log.info("Model checked fuel gap type for {}: {}", regisNbr, gapType);
        return gapType;
    }
}
