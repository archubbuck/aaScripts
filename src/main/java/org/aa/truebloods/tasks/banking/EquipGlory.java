package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.EquipmentExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class EquipGlory implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Equipment.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isEmpty()
//                && Inventory.stream().id(Items.CHARGED_AMULET_OF_GLORIES).isNotEmpty();
        return EquipmentExtensions.doesNotContain(Items.CHARGED_AMULET_OF_GLORIES)
                && InventoryExtensions.contains(Items.CHARGED_AMULET_OF_GLORIES);
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        Item glory = Inventory.stream().id(Items.CHARGED_AMULET_OF_GLORIES).first();

        logger.info("Attempting to equip the " + glory.name());

//        boolean equippedAmulet = glory.valid()
//                && glory.interact("Wear")
//                && Condition.wait(() -> Equipment.stream().id(glory.id()).isNotEmpty(), 75, 30);

        boolean equippedAmulet = glory.valid()
                && glory.interact("Wear")
                && Condition.wait(() -> EquipmentExtensions.contains(glory.id()), 75, 30);

        logger.info(
                (equippedAmulet ? "Successfully equipped " : "Failed to equip ") + glory.name()
        );

    }

}
