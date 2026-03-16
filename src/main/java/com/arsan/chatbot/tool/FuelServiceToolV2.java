package com.arsan.chatbot.tool;

import com.arsan.chatbot.model.FuelSlip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class FuelServiceToolV2 {

    @Tool(
            name = "getBlockInFuel",
            description = """
                    Retrieve the aircraft block-in fuel quantity recorded when the aircraft arrives at the gate.
                    This value is required at the start of the workflow and is used in STEP 1 - FUEL QUANTITY GAP CHECK.
                    """
    )
    public BigDecimal getBlockInFuel(String regisNbr) {
        log.info("Fetching block-in fuel for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(
            name = "getFirstFuelSlip",
            description = """
                    Retrieve the first fuel slip for the aircraft.
                    
                    Returns:
                    - fob : fuel on board recorded in the fuel slip
                    - location : airport code where fueling occurred
                    
                    Used in STEP 1 - FUEL QUANTITY GAP CHECK and STEP 3 - COMPLETION TIME CHECK.
                    """
    )
    public FuelSlip getFirstFuelSlip(String regisNbr) {
        log.info("Fetching fuel slip for {}", regisNbr);

        FuelSlip slip = new FuelSlip();
        slip.setFob(BigDecimal.valueOf(27000));
        slip.setLocation("MEM");

        return slip;
    }

    @Tool(
            name = "getInRangeRemainingFuel",
            description = """
                    Retrieve the calculated remaining fuel within acceptable variance.
                    
                    Used in STEP 2 - BAD BLOCK-IN FUEL CHECK to determine whether the remaining fuel calculation is incorrect.
                    """
    )
    public BigDecimal getInRangeRemainingFuel(String regisNbr) {
        log.info("Fetching remaining fuel for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(
            name = "getAircraftLocation",
            description = """
                    Retrieve the aircraft parking location code.
                    
                    Used in STEP 3 - COMPLETION TIME CHECK to compare aircraft location with fuel slip location.
                    """
    )
    public String getAircraftLocation(String regisNbr) {
        log.info("Fetching aircraft location for {}", regisNbr);
        return "ARA";
    }

    @Tool(
            name = "isWrongAircraft",
            description = """
                    Determine whether the fuel slip belongs to the correct aircraft registration number.
                    
                    Used in STEP 4 - WRONG AIRCRAFT CHECK.
                    """
    )
    public boolean isWrongAircraft(String regisNbr) {
        log.info("Checking wrong aircraft for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingUpliftFuelSlip",
            description = """
                    Check whether an uplift fuel slip is missing when a positive fuel gap exists.
                    
                    Used in STEP 6 - MISSING UPLIFT FUEL SLIP CHECK.
                    """
    )
    public boolean isMissingUpliftFuelSlip(String regisNbr) {
        log.info("Checking missing uplift slip for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingApuRunFuelSlip",
            description = """
                    Check whether an APU run fuel slip is missing when a negative fuel gap exists.
                    
                    Used in STEP 7 - MISSING APU RUN FUEL SLIP CHECK.
                    """
    )
    public boolean isMissingApuRunFuelSlip(String regisNbr) {
        log.info("Checking missing APU run slip for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isBadFuelSlip",
            description = """
                    Check whether the fuel slip contains incorrect or inconsistent data entries.
                    
                    Used in STEP 8 - BAD FUEL SLIP CHECK.
                    """
    )
    public boolean isBadFuelSlip(String regisNbr) {
        log.info("Checking bad fuel slip for {}", regisNbr);
        return false;
    }
}
