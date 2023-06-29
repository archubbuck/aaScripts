package org.aa.gnome.tasks.shop.heckelfunch;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.walking.WebWalkingResult;

import java.util.logging.Logger;

public class NavigateToHeckelFunchShopTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final Area shopArea = new Area(new Tile(2489, 3489, 1), new Tile(2493, 3487, 1));

    @Override
    public boolean activate() {
        int COINS_ID = 995;
        return !shopArea.contains(Players.local())
                && !Inventory.isFull()
                && Inventory.stream().id(COINS_ID).isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to Heckel Funch's shop");

        WebWalkingResult navigationResult = Movement.builder(shopArea.getRandomTile())
                .setRunMin(15)
                .setRunMax(80)
                .setUseTeleports(false)
                .setWalkUntil(() -> shopArea.contains(Players.local()))
                .move();

        boolean navigating = navigationResult.getSuccess()
                && Condition.wait(() -> Players.local().inMotion() || shopArea.contains(Players.local()), 75, 15);

        logger.info(
                navigating
                        ? "We are navigating!"
                        : "We failed to begin navigating!"
        );
    }
}
