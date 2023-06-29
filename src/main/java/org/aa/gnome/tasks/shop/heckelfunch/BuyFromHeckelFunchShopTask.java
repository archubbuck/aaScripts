package org.aa.gnome.tasks.shop.heckelfunch;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.gnome.Item;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Store;

import java.util.Arrays;
import java.util.logging.Logger;

public class BuyFromHeckelFunchShopTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final int COCKTAIL_GLASS_ID = 2026;
    private final int GIN_ID = 2019;
    private final int WHISKEY_ID = 2017;
    private final int BRANDY_ID = 2021;
    private final int LIME_ID = 2120;
    private final int LEMON_ID = 2102;
    private final int PINEAPPLE_ID = 2114;
    private final int ORANGE_ID = 2108;
    private final int VODKA_ID = 2015;

    private final int[] ITEMS_TO_BUY = {
            Item.COCKTAIL_SHAKER,
            COCKTAIL_GLASS_ID,
            GIN_ID,
            WHISKEY_ID,
            BRANDY_ID,
            LIME_ID,
            LEMON_ID,
            PINEAPPLE_ID,
            ORANGE_ID,
            VODKA_ID
    };

    private final int COINS_ID = 995;

    @Override
    public boolean activate() {
        return Store.opened()
                && !Inventory.isFull()
                && Inventory.stream().id(COINS_ID).isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to buy items from Heckel Funch's shop");

        var itemIdToBuy = Arrays.stream(ITEMS_TO_BUY).filter(itemId -> {
            logger.info("Checking the stock for " + itemId);
            int stock = Store.getItem(itemId).itemStackSize();
            return stock > 0;
        }).toArray();

        if (itemIdToBuy.length == 0) {
            logger.info("There are no items in stock!");
            return;
        }

        int startingCoinStack = Inventory.stream().id(COINS_ID).first().stackSize();

        boolean boughtItem = Store.buy(itemIdToBuy[0], 50)
                && Condition.wait(() -> startingCoinStack > Inventory.stream().id(COINS_ID).first().stackSize(), 75, 30);

        logger.info(
                boughtItem
                        ? "We bought the item from the shop!"
                        : "We failed to buy the item from the shop!"
        );
    }
}
