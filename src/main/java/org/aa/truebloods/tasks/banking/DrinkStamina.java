package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class DrinkStamina implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Bank.opened()
//                && Inventory.stream().id(Items.STAMINA_POTIONS).isNotEmpty()
//                && (
//                    Movement.energyLevel() <= Random.nextInt(35, 65) || Varpbits.varpbit(277) <= 512
//                );
//        return Bank.opened()
//                && InventoryExtensions.contains(Items.STAMINA_POTIONS)
//                && (
//                    Movement.energyLevel() <= Random.nextInt(35, 65) || Varpbits.varpbit(277) <= 512
//                );
        return Utils.all(
                InventoryExtensions.contains(Items.STAMINA_POTIONS),
                ConditionExtensions.shouldDrinkStamina()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        Item staminaPotion = Inventory.stream().id(Items.STAMINA_POTIONS).first();

        logger.info("Attempting to drink the " + staminaPotion.name());

        int initialEnergyLevel = Movement.energyLevel();
        boolean equippedAmulet = staminaPotion.valid()
                && staminaPotion.interact("Drink")
                && Condition.wait(() -> Movement.energyLevel() > initialEnergyLevel, 75, 30);

        logger.info(
                (equippedAmulet ? "Successfully drank the " : "Failed to drink the ") + staminaPotion.name()
        );

    }

}
