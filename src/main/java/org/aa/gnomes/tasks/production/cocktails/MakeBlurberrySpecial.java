package org.aa.gnomes.tasks.production.cocktails;

import org.aa.gnomes.CountReqStrategy;
import org.aa.gnomes.Extensions;
import org.aa.gnomes.collections.Items;
import org.aa.gnomes.tasks.GnomeTask;
import org.powbot.api.rt4.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class MakeBlurberrySpecial implements GnomeTask {
    private final Logger logger = Logger.getLogger(getName());



    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean canExecute() {
        return canMakeMixedBlast();
    }

    @Override
    public void execute() {

    }

    private boolean canMakeMixedBlast() {
        return Stream.of(
                Items.Tools.COCKTAIL_SHAKER,
                Items.Ingredients.PINEAPPLE,
                Items.Ingredients.LEMON,
                Items.Ingredients.ORANGE
        ).allMatch(itemName ->
                Extensions.Inventory.containsCount(itemName, 1, CountReqStrategy.GREATER_THAN_OR_EQUAL)
        );
    }

//    private boolean canMakeMixedBlast() {
//        return Stream.of(
//                Items.Tools.COCKTAIL_SHAKER,
//                Items.Ingredients.PINEAPPLE,
//                Items.Ingredients.LEMON,
//                Items.Ingredients.ORANGE
//        ).allMatch(itemName ->
//                Inventory.stream().name(itemName).isNotEmpty());
//    }
}
