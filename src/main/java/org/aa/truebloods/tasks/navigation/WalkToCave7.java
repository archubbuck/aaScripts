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
import org.powbot.dax.api.DaxWalker;
import org.powbot.dax.teleports.Teleport;

import java.util.logging.Logger;

public class WalkToCave7 implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return Utils.all(
                Areas.CAVE_6_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to cave 7");

        GameObject caveEntrance = Objects.stream().name("Cave").within(Tiles.CAVE_6_EXIT, 1).first();

        if (!caveEntrance.inViewport(true)) {
            boolean navigated = Movement.builder(Tiles.CAVE_6_EXIT)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .move()
                    .getSuccess();
            if (!navigated) return;
        }

        boolean navigated = caveEntrance.valid()
                && caveEntrance.interact("Enter")
                && Condition.wait(() -> Areas.CAVE_7_AREA.contains(Players.local()), 125, 25);

        logger.info(
                navigated
                        ? "CAVE 7 REACHED"
                        : "FAILED TO REACH CAVE 7"
        );
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 7");
//
//        boolean navigated = Movement.builder(Tiles.CAVE_7_ENTER)
//                .setRunMin(15)
//                .setRunMax(75)
//                .setUseTeleports(false)
//                .move()
//                .getSuccess();
//
//        logger.info(
//                navigated
//                        ? "CAVE 7 REACHED"
//                        : "FAILED TO REACH CAVE 7"
//        );
//    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 7");
//
//        GameObject caveEntrance = Objects.stream().name("Cave").within(Tiles.CAVE_6_EXIT, 1).first();
//
//        if (!caveEntrance.inViewport(true)) {
//            boolean success = Movement.builder(Tiles.CAVE_6_EXIT)
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
//                && Condition.wait(() -> Areas.CAVE_7_AREA.contains(Players.local()), 125, 25);
//
//        logger.info(
//                success
//                        ? "We are now in cave 7"
//                        : "Failed to navigate to cave 7"
//        );
//    }
}
