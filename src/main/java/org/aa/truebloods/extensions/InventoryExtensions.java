package org.aa.truebloods.extensions;

import org.aa.truebloods.constants.Items;
import org.jetbrains.annotations.NotNull;
import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Inventory;

import java.util.Arrays;

public class InventoryExtensions {

    public static boolean contains(@NotNull int... ints) {
        return Inventory.stream().id(ints).isNotEmpty();
    }

    public static boolean doesNotContain(@NotNull int... ints) {
        return !contains(ints);
    }

    public static boolean containsOnly(@NotNull int... ints) {
        int emptySlots = Inventory.emptySlotCount();
        long itemSlots = Inventory.stream().id(ints).count();
        return itemSlots + emptySlots == 28;
    }

    public static boolean containsAll(@NotNull int... ints) {
        return Arrays.stream(ints).allMatch(InventoryExtensions::contains);
    }

    public static boolean isNotFull() {
        return !Inventory.isFull();
    }

    public static long count(@NotNull int... ints) {
        return Inventory.stream().id(ints).count();
    }

}
