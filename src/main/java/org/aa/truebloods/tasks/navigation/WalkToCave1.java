package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.EquipmentExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WalkToCave1 implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Areas.EDGEVILLE_AREA.contains(Players.local())
//                && Equipment.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isNotEmpty()
//                && Inventory.stream().id(Items.PURE_ESSENCE).isNotEmpty();
//        return Areas.EDGEVILLE_AREA.contains(Players.local())
//                && EquipmentExtensions.contains(Items.CHARGED_AMULET_OF_GLORIES)
//                && InventoryExtensions.containsAll(Items.PURE_ESSENCE, Items.BLOOD_TALISMAN)
//                && !PouchTracker.INSTANCE.hasPouchToFill();
        return Utils.all(
                Areas.EDGEVILLE_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to cave 1");

//        boolean navigated = Movement.builder(new Tile(3447, 9822, 0))
//                .setRunMin(15)
//                .setRunMax(75)
//                .move()
//                .getSuccess();

        boolean navigated = Movement.builder(Tiles.CAVE_2_ENTER)
                .setRunMin(15)
                .setRunMax(75)
                .move()
                .getSuccess();

        logger.info(
                navigated
                    ? "CAVE 1 REACHED"
                    : "FAILED TO REACH CAVE 1"
        );
    }
}
