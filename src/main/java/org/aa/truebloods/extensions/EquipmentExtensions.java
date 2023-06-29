package org.aa.truebloods.extensions;

import org.jetbrains.annotations.NotNull;
import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Inventory;

public class EquipmentExtensions {

    public static boolean contains(@NotNull int... ints) {
        return Equipment.stream().id(ints).isNotEmpty();
    }

    public static boolean doesNotContain(@NotNull int... ints) {
        return !contains(ints);
    }

}
