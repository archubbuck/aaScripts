package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WalkToCave3 implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return Utils.all(
                Areas.CAVE_2_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to cave 3");

        boolean navigated = Movement.builder(Tiles.CAVE_3_ENTER)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .move()
                .getSuccess();

        logger.info(
                navigated
                        ? "CAVE 3 REACHED"
                        : "FAILED TO REACH CAVE 3"
        );
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 3");
//
//        boolean navigateToExitTile = Movement.builder(Tiles.CAVE_2_EXIT)
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
//        boolean navigateToCave = Movement.builder(new Tile(3481, 9824))
//                .setRunMin(15)
//                .setRunMax(75)
//                .move()
//                .getSuccess();
//
//        logger.info(
//                navigateToCave
//                        ? "WEB WALKER navigateToCave SUCCESS"
//                        : "WEB WALKER navigateToCave FAIL"
//        );
//    }
}
