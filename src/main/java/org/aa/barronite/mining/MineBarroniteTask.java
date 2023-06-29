package org.aa.barronite.mining;

import org.aa.barronite.BarroniteScript;
import org.aa.barronite.Task;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.stream.locatable.interactive.GameObjectStream;
import org.powbot.api.rt4.walking.model.Skill;

import java.util.logging.Logger;

public class MineBarroniteTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final Tile mineTile = new Tile(2936, 5811, 0);

    private static final int[] BARRONITE_ROCKS_IDS = {
            41547,
            41548
    };

    @Override
    public boolean activate() {
        return !Inventory.isFull()
                && mineTile.matrix().onMap()
                && getBarroniteRocks().isNotEmpty()
                && (!BarroniteScript.ore.refresh().valid() || BarroniteScript.getTimeSinceLastAnimation() >= 12500 && Skills.timeSinceExpGained(Skill.Mining) >= 12500);
    }

    @Override
    public void execute() {
        logger.info("Mining");

        BarroniteScript.ore = getBarroniteRocks().nearest().first();

        boolean miningBarroniteRock = BarroniteScript.ore.interact("Mine")
                && Condition.wait(this::isMining, 150, 15);

        logger.info(miningBarroniteRock ? "Mining barronite rocks" : "Failed to mine barronite rocks");
    }

    private GameObjectStream getBarroniteRocks() {
        return Objects.stream().id(BARRONITE_ROCKS_IDS).viewable();
    }

    private boolean isMining() {
        return Players.local().distanceTo(BarroniteScript.ore) <= 1 && BarroniteScript.getTimeSinceLastAnimation() < 150 * 15;
    }
}
