package org.aa.gnome.tasks.cook.cocktails;

import org.aa.AbstractScript;
import org.aa.Task;
import org.aa.gnome.Item;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Component;
import org.powbot.api.rt4.Components;
import org.powbot.api.rt4.Inventory;

import java.util.Arrays;
import java.util.logging.Logger;

public class FruitBlast implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final int[] PRIMARY_INGREDIENTS = {
            Item.COCKTAIL_SHAKER,
            Item.PINEAPPLE_ID,
            Item.LEMON_ID,
            Item.ORANGE_ID
    };

    private Component gnomeCocktailsMenu;

    @Override
    public boolean activate() {
        return Arrays.stream(PRIMARY_INGREDIENTS).allMatch(itemId -> Inventory.stream().id(itemId).isNotEmpty());
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("We have all of the ingredients!");

        // if mixer widget not open, click mixer

        gnomeCocktailsMenu = getGnomeCocktailsMenu();

        if (!gnomeCocktailsMenu.valid()) {
            logger.info("Attempting to open the gnome cocktails menu");

            org.powbot.api.rt4.Item cocktailShaker = Inventory.stream().id(Item.COCKTAIL_SHAKER).any();

            boolean openGnomeCocktailMenu = cocktailShaker.click() && Condition.wait(() -> getGnomeCocktailsMenu().valid(), 150, 30);

            logger.info(
                    openGnomeCocktailMenu
                        ? "We opened the gnome cocktails menu!"
                        : "We failed to open the gnome cocktails menu!"
            );

            if (!openGnomeCocktailMenu) return;
        }

        Component createButton = Components.stream(gnomeCocktailsMenu.widgetId()).action("Create").viewable().first();

        if (!createButton.valid()) {
            logger.info("Unable to locate the create button");
            return;
        }

        long initialMixedBlastCount = getMixedBlastCount();

        boolean created = createButton.click() && Condition.wait(() -> getMixedBlastCount() > initialMixedBlastCount, 75, 30);

        logger.info(
                created
                    ? "We created a mixed blast!"
                    : "We failed to create a mixed blast!"
        );
    }

    private long getMixedBlastCount() {
        return Inventory.stream().id(Item.MIXED_BLAST).count();
    }

    private Component getGnomeCocktailsMenu() {
        gnomeCocktailsMenu = Components.stream().text("Gnome Cocktails").viewable().first();
        logger.info(gnomeCocktailsMenu.getIds().toString());
        return gnomeCocktailsMenu;
    }
}
