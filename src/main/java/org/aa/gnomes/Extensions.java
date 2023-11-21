package org.aa.gnomes;

import org.powbot.api.rt4.Inventory;

public class Extensions {

    /*
     * Inventory
     */

    public static class Inventory {

        /**
         * @param itemName The name of the item
         * @param countRequired The number of the item to appear in the inventory
         * @param strategy The strategy to use when evaluating the result
         */
        public static boolean containsCount(String itemName, int countRequired, CountReqStrategy strategy) {
            long countOfItem = org.powbot.api.rt4.Inventory.stream().name(itemName).count(true);

            switch (strategy) {
                case GREATER_THAN:
                    return countOfItem > countRequired;
                case GREATER_THAN_OR_EQUAL:
                    return countOfItem >= countRequired;
                case LESS_THAN:
                    return countOfItem < countRequired;
                case LESS_THAN_OR_EQUAL:
                    return countOfItem <= countRequired;
                case EQUALS:
                    return countOfItem == countRequired;
            }

            return containsCount(itemName, countRequired);
        }

        /**
         * @param itemName The name of the item
         * @param countRequired The number of the item to appear in the inventory
         */
        public static boolean containsCount(String itemName, int countRequired) {
            return containsCount(itemName, countRequired, CountReqStrategy.EQUALS);
        }
    }
}
