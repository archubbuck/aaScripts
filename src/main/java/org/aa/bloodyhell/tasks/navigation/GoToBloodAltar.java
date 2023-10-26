package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;

public class GoToBloodAltar extends Task {

    private final Tile RUINS_TILE = new Tile(3560, 9780, 0);

    @Override
    public boolean activate() {
        return all(
                Areas.CAVE_7_AREA.contains(Players.local()),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        if (RUINS_TILE.distance() > 5) {
            boolean navigated = Movement.builder(RUINS_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> RUINS_TILE.distance() <= 5)
                    .move()
                    .getSuccess();
        }

        GameObject ruins = Objects.stream().name("Mysterious ruins")
                .within(20)
                .nearest(RUINS_TILE)
                .first();

        if (!ruins.valid()) {
            System.out.println("Unable to locate the ruins near RUINS_TILE");
            return;
        }

        if (!ruins.inViewport(true)) {
            System.out.println("Turning the camera");
            Camera.turnTo(ruins);
        }

        boolean exiting = ruins.interact("Enter");

        if (!exiting) {
            System.out.println("Failed to interact with the ruins near RUINS_TILE");
            return;
        }

        System.out.println("Waiting for the player to finish exiting");

        boolean exited = Condition.wait(() -> Areas.BLOOD_ALTAR_AREA.contains(Players.local()), 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the time limit");
        }
    }
}
