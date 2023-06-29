package org.aa.gnome.tasks.shop.heckelfunch;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.WebWalkingResult;

import java.util.logging.Logger;

public class NavigateToBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final Area bankArea = new Area(new Tile(2448, 3482, 1), new Tile(2450, 3483, 1));

    Tile bankTile;

    @Override
    public boolean activate() {
        return Inventory.isFull()
                && Players.local().distanceTo(bankTile = bankArea.getRandomTile()) > 5;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Attempting to navigate to the bank");

        WebWalkingResult navigationResult = Movement.builder(bankTile)
                .setRunMin(15)
                .setRunMax(80)
                .setUseTeleports(false)
                .setWalkUntil(() -> Players.local().distanceTo(bankTile) <= 5)
                .move();

        boolean navigating = navigationResult.getSuccess()
                && Condition.wait(() -> Players.local().inMotion() || bankArea.contains(Players.local()), 75, 15);

        logger.info(
                navigating
                        ? "We are navigating!"
                        : "We failed to begin navigating!"
        );
    }
}
