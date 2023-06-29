package org.aa.gnome.tasks.shop.heckelfunch;

import org.aa.Task;
import org.aa.gnome.Item;
import org.aa.gnome.tasks.SetCameraTask;
import org.powbot.api.script.paint.PaintBuilder;

public class Shop {

    public final static Task[] TASKS = {
            new SetCameraTask(),
            new NavigateToBankTask(),
            new OpenBankTask(),
            new DepositItemsToBankTask(),
            new NavigateToHeckelFunchShopTask(),
            new OpenHeckelFunchShopTask(),
            new BuyFromHeckelFunchShopTask()
    };

    private static final int[] ITEMS_TO_BUY = {
            Item.COCKTAIL_SHAKER,
            Item.COCKTAIL_GLASS_ID,
            Item.GIN_ID,
            Item.WHISKEY_ID,
            Item.BRANDY_ID,
            Item.LIME_ID,
            Item.LEMON_ID,
            Item.PINEAPPLE_ID,
            Item.ORANGE_ID,
            Item.VODKA_ID
    };

    public final static PaintBuilder PAINT = new PaintBuilder()
            .trackInventoryItems(ITEMS_TO_BUY)
            .withTotalLoot(true);

}
