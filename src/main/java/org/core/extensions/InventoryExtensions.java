package org.core.extensions;

import org.jetbrains.annotations.NotNull;
import org.powbot.api.rt4.Inventory;

import java.util.Arrays;

public class InventoryExtensions {

    public static boolean contains(@NotNull int... ints) {
        boolean result = Inventory.stream().id(ints).isNotEmpty();
//        System.out.println("Inventory " + (result ? "contains" : "does not contain") + " " + Arrays.toString(ints));
        return result;
    }

    public static boolean doesNotContain(@NotNull int... ints) {
//        return !contains(ints);
        boolean result = Inventory.stream().id(ints).isNotEmpty();
        return !result;
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
