package org.aa.bloods.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.bloods.Constants;
import org.aa.bloods.ReactionGenerator;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

public class NavigateToDarkAltarTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {

        boolean c1 = !Constants.DARK_ALTAR_AREA.contains(Players.local());
        boolean c2 = Inventory.stream().id(Constants.DENSE_ESSENCE_BLOCK).isNotEmpty();
        boolean c3 = Inventory.isFull();

        logger.info(
                !c1 ? "c1" : !c2 ? "c2" : !c3 ? "c3" : "?????"
        );

        return c1 && c2 && c3;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to the dark altar area");

        if (Constants.DENSE_ESSENCE_AREA.contains(Players.local())) {
            logger.info("We are in the dense essence area");

            Movement.builder(Constants.SHORTCUT_ROCK_TILE_S)
                .setRunMin(15)
                .setRunMax(80)
                .setUseTeleports(false)
                .setWalkUntil(() -> Players.local().distanceTo(Constants.SHORTCUT_ROCK_TILE_S) <= 8)
                .move();

            logger.info("dax finished");

            GameObject rocks = Objects.stream().id(Constants.ROCK_ID).action("Climb").within(8).nearest().first();
            if (rocks.valid()) {
                logger.info("Interacting with the rocks");
                boolean interacted = rocks.interact("Climb") && Condition.wait(() -> !Constants.DENSE_ESSENCE_AREA.contains(Players.local()), 500, 25);;
                logger.info(interacted ? "Successfully interacted with the rocks" : "Failed to interact with the rocks");
                return;
            }

            Condition.sleep(ReactionGenerator.getPredictable());
            return;
        }

        if (!Constants.DARK_ALTAR_AREA.contains(Players.local()))
        {
            Movement.builder(Constants.DARK_ALTAR_TILE)
                    .setRunMin(15)
                    .setRunMax(80)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> Constants.DARK_ALTAR_AREA.contains(Players.local()))
                    .move();
            Condition.sleep(ReactionGenerator.getPredictable());
        }

        Movement.builder(Constants.DARK_ALTAR_TILE)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .setWalkUntil(() -> Constants.DARK_ALTAR_AREA.contains(Players.local()))
                .move();

        boolean navigating = Condition.wait(() -> Players.local().inMotion(), 150, 15);
        logger.info(navigating ? "Successfully navigating to the dark altar area" : "Failed to start navigating to the dark altar area");
    }

    private boolean isAnimating() {
        return Players.local().animation() != -1;
    }
}
