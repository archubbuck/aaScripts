package org.aa.truebloods.tasks.banking;

import org.aa.truebloods.Task;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.BankExtensions;
import org.aa.truebloods.extensions.ConditionExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class WithdrawStamina implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
//        return Bank.opened()
//                && Bank.stream().id(Items.STAMINA_POTIONS).isNotEmpty()
//                && !Inventory.isFull()
//                && Inventory.stream().id(Items.STAMINA_POTIONS).isEmpty()
//                && Movement.energyLevel() <= Random.nextInt(20, 45);
//        return Bank.opened()
//                && InventoryExtensions.isNotFull()
//                && BankExtensions.contains(Items.STAMINA_POTIONS)
//                && InventoryExtensions.doesNotContain(Items.STAMINA_POTIONS)
//                && Movement.energyLevel() <= Random.nextInt(20, 45);
        return Utils.all(
                Bank.opened(),
                InventoryExtensions.isNotFull(),
                BankExtensions.contains(Items.STAMINA_POTIONS),
                InventoryExtensions.doesNotContain(Items.STAMINA_POTIONS),
                ConditionExtensions.shouldDrinkStamina()
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        Item staminaPotion = Bank.stream().id(Items.STAMINA_POTIONS).first();

        logger.info("Attempting to withdraw an " + staminaPotion.name());

//        boolean gloryWithdrawn = staminaPotion.valid()
//                && Bank.withdraw(staminaPotion.id(), 1)
//                && Condition.wait(() -> Inventory.stream().id(Items.STAMINA_POTIONS).isNotEmpty(), 75, 30);

        boolean gloryWithdrawn = staminaPotion.valid()
                && Bank.withdraw(staminaPotion.id(), 1)
                && Condition.wait(() -> InventoryExtensions.contains(staminaPotion.id()), 75, 30);

        logger.info(
                gloryWithdrawn
                        ? "Successfully withdrew " + staminaPotion.name()
                        : "Failed to withdraw " + staminaPotion.name()
        );

    }
}
