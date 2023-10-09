package org.core.extensions;

import org.jetbrains.annotations.NotNull;
import org.powbot.api.rt4.Equipment;

import java.util.Arrays;

public class EquipmentExtensions {

    public static boolean contains(@NotNull int... ints) {
        return Equipment.stream().id(ints).isNotEmpty();
    }

    public static boolean doesNotContain(@NotNull int... ints) {
        return !contains(ints);
    }

    public static boolean containsAll(@NotNull int... ints) {
        return Arrays.stream(ints).allMatch(EquipmentExtensions::contains);
    }

}
