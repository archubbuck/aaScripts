package org.aa.truebloods.extensions;

import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.Varpbits;

public class ConditionExtensions {

    public static boolean waitForExpGained(int skill, int time) {
        int originalXp = Skills.experience(skill);
        return Condition.wait(() -> {
            int newExp = Skills.experience(skill);
            return newExp > originalXp;
        }, 10, time/10);
    }

    public static boolean waitForExpGained(int skill) {
        int time = 4500;
        return waitForExpGained(skill, time);
    }

    public static boolean readyToCraftBloodRunes() {
        return Utils.all(
                EquipmentExtensions.contains(Items.CHARGED_AMULET_OF_GLORIES),
                InventoryExtensions.containsAll(Items.PURE_ESSENCE, Items.BLOOD_TALISMAN),
                !PouchTracker.INSTANCE.hasPouchToFill()
        );
    }

    public static boolean shouldDrinkStamina() {
        return Utils.all(
                Movement.energyLevel() <= 35,
                Varpbits.varpbit(277) <= 512
        );
    }

}
