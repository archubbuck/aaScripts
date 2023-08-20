package org.aa.bloodyhell.extensions;

import org.aa.truebloods.extensions.InventoryExtensions;
import org.jetbrains.annotations.NotNull;
import org.powbot.api.rt4.Equipment;

import java.util.Arrays;

public class EquipmentExtensions {

    public static boolean contains(@NotNull int... ints) {
        boolean result = Equipment.stream().id(ints).isNotEmpty();
//        System.out.println("Equipment " + (result ? "contains" : "does not contain") + " " + Arrays.toString(ints));
        return result;
    }

    public static boolean doesNotContain(@NotNull int... ints) {
        return !contains(ints);
    }

    public static boolean containsAll(@NotNull int... ints) {
        return Arrays.stream(ints).allMatch(EquipmentExtensions::contains);
    }

}
