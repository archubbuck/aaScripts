package org.aa.bloodyhell.tasks.navigation;

import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.stream.locatable.interactive.GameObjectStream;

import javax.annotation.Nullable;

public class CaveNavigator {
    private boolean shouldContinue = true;

    public CaveNavigator navigate(Area startingArea, Area destinationArea, Tile exitTile, @Nullable String caveName) {
        if (!this.shouldContinue || !startingArea.contains(Players.local())) {
            log("Skipping execution...");
            return this;
        }

        if (exitTile.distance() > 5) {
            log("Attempting to navigate to the exit tile...");
            boolean navigated = Movement.builder(exitTile)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> exitTile.distance() <= 5)
                    .move()
                    .getSuccess();
            if (!navigated) {
                log("Navigating to the exit tile failed");
                this.shouldContinue = false;
                return this;
            };
        }

        if (caveName != null) {
            GameObject caveObject = Objects.stream().name(caveName).nearest(exitTile).first();
            log("Attempting to interact with " + caveName);
            boolean interacting = caveObject.valid() && caveObject.interact("Enter");
            if (!interacting) {
                log("Interaction failed for " + caveName);
                this.shouldContinue = false;
                return this;
            };
        }

        this.shouldContinue = Condition.wait(() -> destinationArea.contains(Players.local()), 150, 10);

        log(this.shouldContinue ? "Continuing..." : "Stopping...");

        return this;
    }

    public boolean getResult() {
        return this.shouldContinue;
    }

    private void log(String message) {
        String LOG_PREFIX = "[CAVE_NAVIGATOR] ";
        System.out.println(LOG_PREFIX + message);
    }
}
