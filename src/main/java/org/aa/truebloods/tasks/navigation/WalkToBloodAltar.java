package org.aa.truebloods.tasks.navigation;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WalkToBloodAltar implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Areas.CAVE_7_AREA.contains(Players.local())
//                && Inventory.stream().id(Items.PURE_ESSENCE).isNotEmpty()
//                && Inventory.isFull()
//                && !Players.local().inMotion();
        return Utils.all(
                Areas.CAVE_7_AREA.contains(Players.local()),
                ConditionExtensions.readyToCraftBloodRunes()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to enter the blood altar");

        GameObject mysteriousRuins = Objects.stream().name("Mysterious ruins").first();

        if (!mysteriousRuins.valid()) {
            logger.info("Mysterious ruins invalid");
            return;
        }

        Item bloodTalisman = Inventory.stream().id(Items.BLOOD_TALISMAN).first();

        if (!bloodTalisman.valid()) {
            logger.info("Blood talisman invalid");
            return;
        }

        boolean success = bloodTalisman.useOn(mysteriousRuins)
                && Condition.wait(() -> Areas.BLOOD_ALTAR_AREA.contains(Players.local()) && !Players.local().inMotion(), 125, 25);

        logger.info(
                success
                    ? "We are now in the blood altar"
                    : "Failed to navigate the blood altar"
        );
    }
}
