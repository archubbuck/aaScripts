package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WalkToCave6 implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return Utils.all(
                Areas.CAVE_5_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to cave 6");

        GameObject caveEntrance = Objects.stream().name("Cave").within(Tiles.CAVE_5_EXIT, 1).first();

        if (!caveEntrance.inViewport(true)) {
            boolean navigated = Movement.builder(Tiles.CAVE_5_EXIT)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .move()
                    .getSuccess();
            if (!navigated) return;
        }

        boolean navigated = caveEntrance.valid()
                && caveEntrance.interact("Enter")
                && Condition.wait(() -> Areas.CAVE_6_AREA.contains(Players.local()), 125, 25);

        logger.info(
                navigated
                        ? "CAVE 6 REACHED"
                        : "FAILED TO REACH CAVE 6"
        );
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 6");
//
//        boolean navigated = Movement.builder(Tiles.CAVE_6_ENTER)
//                .setRunMin(15)
//                .setRunMax(75)
//                .setUseTeleports(false)
//                .move()
//                .getSuccess();
//
//        logger.info(
//                navigated
//                        ? "CAVE 6 REACHED"
//                        : "FAILED TO REACH CAVE 6"
//        );
//    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 6");
//
//        GameObject caveEntrance = Objects.stream().name("Cave").within(Tiles.CAVE_5_EXIT, 1).first();
//
//        if (!caveEntrance.inViewport(true)) {
//            boolean success = Movement.builder(Tiles.CAVE_5_EXIT)
//                    .setRunMin(15)
//                    .setRunMax(75)
//                    .setUseTeleports(false)
//                    .move()
//                    .getSuccess();
//            if (!success) return;
//        }
//
//        boolean interactionSuccess = caveEntrance.valid()
//                && caveEntrance.inViewport(true)
//                && caveEntrance.interact("Enter")
//                && Condition.wait(() -> !Players.local().inMotion(), 75, 30);
//
//        boolean success = interactionSuccess
//                && Condition.wait(() -> Areas.CAVE_6_AREA.contains(Players.local()), 125, 25);
//
//        logger.info(
//                success
//                        ? "We are now in cave 6"
//                        : "Failed to navigate to cave 6"
//        );
//    }
}
