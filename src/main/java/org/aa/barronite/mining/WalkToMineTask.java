package org.aa.barronite.mining;

import org.aa.barronite.Task;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

public class WalkToMineTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final Tile mineTile = new Tile(2936, 5811, 0);

    @Override
    public boolean activate() {
        return !Inventory.isFull()
                && !mineTile.matrix().onMap()
                && !isPlayerOnMineTile(Players.local())
                && !Players.local().inMotion();
    }

    @Override
    public void execute() {
        logger.info("Attempting to walk to the mine");

        boolean walkingToTheMine = Movement.walkTo(mineTile)
                && Condition.wait(() -> Players.local().inMotion() || isPlayerOnMineTile(Players.local()), 150, 15);

        logger.info(walkingToTheMine ? "Walking to the mine" : "Failed to walk to the mine");
    }

    private boolean isPlayerOnMineTile(Player player) {
        return player.tile().equals(mineTile);
    }
}
