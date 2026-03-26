package com.arsan.chatbot.tool;

import com.arsan.chatbot.dto.FuelSlip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class FuelServiceToolV2 {

    // STEP 1 & STEP 2: FUEL QUANTITY & BLOCK-IN VALIDATION
    @Tool(name = "getBlockInFuel", description = "Returns aircraft block-in fuel in gallons. Requires regisNbr.")
    public BigDecimal getBlockInFuel(String regisNbr) {
        log.info("Fetched blockInFuel for {}", regisNbr);
        return BigDecimal.valueOf(20000);
    }

    @Tool(name = "getFirstFuelSlip", description = "Returns FuelSlip object. Required for gap, location, and slip validation checks. Requires regisNbr.")
    public FuelSlip getFirstFuelSlip(String regisNbr) {
        log.info("Fetched first fuel slip for {}", regisNbr);
        FuelSlip slip = new FuelSlip();
        slip.setPreFob(BigDecimal.valueOf(22000));
        slip.setFob(BigDecimal.valueOf(25000));
        slip.setLocation("MEM");
        slip.setComputed(BigDecimal.valueOf(10000));
        slip.setIndicated(BigDecimal.valueOf(10000));
        slip.setMeteredFuel(BigDecimal.valueOf(1000));
        slip.setDensity(7.7);
        return slip;
    }

    @Tool(name = "getInRangeRemainingFuel", description = "Returns calculated remaining fuel within acceptable variance. STEP 2 validation. Requires regisNbr.")
    public BigDecimal getInRangeRemainingFuel(String regisNbr) {
        log.info("Fetched inRangeRemainingFuel for {}", regisNbr);
        return BigDecimal.valueOf(25000);
    }

    // STEP 3: COMPLETION TIME CHECK
    @Tool(name = "getAircraftLocation", description = "Returns current aircraft location code (3-letter). Requires regisNbr.")
    public String getAircraftLocation(String regisNbr) {
        log.info("Fetched aircraft location for {}", regisNbr);
        return "MEM";
    }

    // STEP 4: WRONG AIRCRAFT CHECK
    @Tool(name = "isWrongAircraft", description = "Returns true if fuel slip is associated with wrong aircraft. Requires regisNbr.")
    public boolean isWrongAircraft(String regisNbr) {
        log.info("Checked wrong aircraft for {}", regisNbr);
        return false;
    }

    // STEP 6: MISSING UPLIFT FUEL SLIP
    @Tool(name = "isMissingUpliftFuelSlip", description = "Returns true if uplift fuel slip is missing. Requires regisNbr.")
    public boolean isMissingUpliftFuelSlip(String regisNbr) {
        log.info("Checked missing uplift fuel slip for {}", regisNbr);
        return false;
    }

    // STEP 7: MISSING APU RUN FUEL SLIP
    @Tool(name = "isMissingApuRunFuelSlip", description = "Returns true if APU run fuel slip is missing. Requires regisNbr.")
    public boolean isMissingApuRunFuelSlip(String regisNbr) {
        log.info("Checked missing APU run fuel slip for {}", regisNbr);
        return false;
    }
}