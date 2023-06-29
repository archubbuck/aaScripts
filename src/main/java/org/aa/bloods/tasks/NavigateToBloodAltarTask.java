package org.aa.bloods.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.bloods.Constants;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;

import java.util.logging.Logger;

public class NavigateToBloodAltarTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {

        boolean c1 = !Constants.BLOOD_RUNE_ALTAR_AREA.contains(Players.local());
        boolean c2 = Inventory.stream().id(Constants.DARK_ESSENCE_FRAGMENTS).isNotEmpty();
        boolean c3 = Inventory.stream().id(Constants.DARK_ESSENCE_BLOCK).isNotEmpty();
        boolean c4 = Inventory.isFull();

        logger.info(
                !c1 ? "c1" : !c2 ? "c2" : !c3 ? "c3" : !c4 ? "c4" : "?????"
        );

        return c1 && c2 && c3 && c4;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to the dark altar area");

        Movement.builder(Constants.BLOOD_RUNE_ALTAR_TILE)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .setWalkUntil(() -> Constants.BLOOD_RUNE_ALTAR_AREA.contains(Players.local()))
                .move();

        boolean navigating = Condition.wait(() -> Players.local().inMotion(), 150, 15);
        logger.info(navigating ? "Successfully navigating to the blood altar area" : "Failed to start navigating to the blood altar area");
    }
}
