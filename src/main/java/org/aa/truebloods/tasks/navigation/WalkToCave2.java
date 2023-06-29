package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WalkToCave2 implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Areas.CAVE_1_AREA.contains(Players.local())
//                && Equipment.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isNotEmpty()
//                && Inventory.stream().id(Items.PURE_ESSENCE).isNotEmpty();
        return Utils.all(
                Areas.CAVE_1_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to cave 2");

        boolean navigated = Movement.builder(Tiles.CAVE_2_ENTER)
                .setRunMin(15)
                .setRunMax(75)
                .move()
                .getSuccess();

        logger.info(
                navigated
                        ? "CAVE 2 REACHED"
                        : "FAILED TO REACH CAVE 2"
        );
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        logger.info("Attempting to navigate to cave 2");
//
//        GameObject caveEntrance = Objects.stream().name("Cave entrance").within(Tiles.CAVE_1_EXIT, 1).first();
//
//        if (!caveEntrance.inViewport(true)) {
//            boolean success = Movement.builder(Tiles.CAVE_1_EXIT)
//                    .setRunMin(15)
//                    .setRunMax(75)
//                    .setUseTeleports(false)
//                    .move()
//                    .getSuccess();
//            if (!success) return;
//        }
//
//        boolean interactionSuccess = caveEntrance.valid()
//                && caveEntrance.interact("Enter")
//                && Condition.wait(() -> !Players.local().inMotion(), 75, 30);
//
//        boolean success = interactionSuccess
//                && Condition.wait(() -> Areas.CAVE_2_AREA.contains(Players.local()), 125, 25);
//
//        logger.info(
//                success
//                        ? "We are now in cave 2"
//                        : "Failed to navigate to cave 2"
//        );
//    }
}
