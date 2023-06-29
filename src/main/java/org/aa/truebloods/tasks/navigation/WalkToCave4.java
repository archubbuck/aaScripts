package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WalkToCave4 implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return Utils.all(
                Areas.CAVE_3_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to cave 4");

        boolean navigated = Movement.builder(Tiles.CAVE_4_ENTER)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .move()
                .getSuccess();

        logger.info(
                navigated
                        ? "CAVE 4 REACHED"
                        : "FAILED TO REACH CAVE 4"
        );
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 4");
//
//        boolean navigateToExitTile = Movement.builder(Tiles.CAVE_3_EXIT)
//                .setRunMin(15)
//                .setRunMax(75)
//                .setUseTeleports(false)
//                .move()
//                .getSuccess();
//
//        logger.info(
//                navigateToExitTile
//                        ? "WEB WALKER navigateToExitTile SUCCESS"
//                        : "WEB WALKER navigateToExitTile FAIL"
//        );
//
//        if (!navigateToExitTile) return;
//
//        boolean navigateToCave = Movement.builder(new Tile(3491, 9876, 0))
//                .setRunMin(15)
//                .setRunMax(75)
//                .move()
//                .getSuccess();
//
//        if (navigateToCave) {
//            logger.info("WEB WALKER navigateToCave SUCCESS");
//            return;
//        }
//
//        logger.info("WEB WALKER navigateToCave FAIL");
//
//        GameObject caveEntrance = Objects.stream().name("Cave entrance").within(Tiles.CAVE_3_EXIT, 1).first();
//
//        boolean success = caveEntrance.valid()
//                && caveEntrance.inViewport(true)
//                && caveEntrance.interact("Enter")
//                && Condition.wait(() -> Players.local().tile().equals(new Tile(3491, 9876, 0)), 75, 30);
//
//        logger.info(
//                success
//                        ? "MANUAL NAVIGATION SUCCESS"
//                        : "MANUAL NAVIGATION FAIL"
//        );
//    }
}
