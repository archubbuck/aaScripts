package org.aa.barronite.mining;

import org.aa.barronite.Task;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Player;
import org.powbot.api.rt4.Players;

import java.util.logging.Logger;

public class WalkToBankTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final Tile bankTile = new Tile(2978, 5798, 0);

    @Override
    public boolean activate() {
        return Inventory.isFull()
                && !isPlayerOnBankTile(Players.local())
                && !Players.local().inMotion();
    }

    @Override
    public void execute() {
        logger.info("Attempting to walk to the bank");

        boolean walkingToTheBank = Movement.walkTo(bankTile)
                && Condition.wait(() -> Players.local().inMotion() || isPlayerOnBankTile(Players.local()), 150, 15);

        logger.info(walkingToTheBank ? "Walking to the bank" : "Failed to walk to the bank");
    }

    private boolean isPlayerOnBankTile(Player player) {
        return player.tile().equals(bankTile);
    }
}
