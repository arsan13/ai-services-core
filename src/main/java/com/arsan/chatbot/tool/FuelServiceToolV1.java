package com.arsan.chatbot.tool;

import com.arsan.chatbot.model.FuelSlip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class FuelServiceToolV1 {

    @Tool(
            name = "getBlockInFuel",
            description = """
        Retrieve the aircraft block-in fuel quantity recorded when the aircraft arrives at the gate.
        Used during STEP 2 (Fuel Quantity Gap Check) and STEP 3 (Remaining Fuel Validation).
        """
    )
    public BigDecimal getBlockInFuel(String regisNbr) {
        log.info("Fetching block-in fuel for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(
            name = "getFirstFuelSlip",
            description = """
        Retrieve the first fuel slip associated with the aircraft.
        Returns a FuelSlip object containing:
        - fob: fuel on board recorded in the slip
        - location: airport code where fueling occurred.
        Used in STEP 2 (Fuel Quantity Gap Check) and STEP 5 (Location Consistency Check).
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
    Used during STEP 2 (Remaining Fuel Validation).
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
    Used during STEP 5 (Location Consistency Check).
    """
    )
    public String getAircraftLocation(String regisNbr) {
        log.info("Fetching aircraft location for {}", regisNbr);
        return "ARA";
    }

    @Tool(
            name = "isWrongAircraft",
            description = """
    Determine whether the fuel slip belongs to a different aircraft.
    This tool is used during STEP 4 of the Fuel Discrepancy Detection Workflow.
    """
    )
    public boolean isWrongAircraft(String regisNbr) {
        log.info("Checking wrong aircraft for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingUpliftFuelSlip",
            description = """
        Determine whether an uplift fuel slip is missing when a positive fuel gap exists.
        Used during STEP 6 (Missing Uplift Fuel Slip Check).
        """
    )
    public boolean isMissingUpliftFuelSlip(String regisNbr) {
        log.info("Checking missing uplift slip for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isMissingApuRunFuelSlip",
            description = """
        Determine whether an APU run fuel slip is missing when a negative fuel gap exists.
        Used during STEP 7 (Missing APU Run Fuel Slip Check).
        """
    )
    public boolean isMissingApuRunFuelSlip(String regisNbr) {
        log.info("Checking missing APU run slip for {}", regisNbr);
        return false;
    }

    @Tool(
            name = "isBadFuelSlip",
            description = """
        Determine whether the fuel slip contains incorrect or inconsistent data.
        Used during STEP 8 (Bad Fuel Slip Check).
        """
    )
    public boolean isBadFuelSlip(String regisNbr) {
        log.info("Checking bad fuel slip for {}", regisNbr);
        return false;
    }
}
