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

public class GoToBloodAltar implements Task {
//    private final Tile RUINS_TILE = new Tile(3559, 9780);
//    private final Tile RUINS_TILE = new Tile(3562, 9782);

    private final Tile RUINS_TILE = new Tile(3560, 9780, 0);

    @Override
    public boolean activate() {
        return all(
                Areas.CAVE_7_AREA.contains(Players.local()),
//                Objects.stream().name("Altar").action("Craft-runes").isEmpty(),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
//        System.out.println("Navigating to RUINS_TILE");

//        System.out.println("Distance: " + RUINS_TILE.distance());

        if (RUINS_TILE.distance() > 5) {
            boolean navigated = Movement.builder(RUINS_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> RUINS_TILE.distance() <= 5)
                    .move()
                    .getSuccess();
        }

//        GameObject ruins2 = Objects.stream().name("Mysterious ruins").first();

//        System.out.println(ruins2.tile());

        GameObject ruins = Objects.stream().name("Mysterious ruins")
//                .at(RUINS_TILE)
                .within(20)
                .nearest(RUINS_TILE)
                .first();

        if (!ruins.valid()) {
            System.out.println("Unable to locate the ruins near RUINS_TILE");
            return false;
        }

        if (!ruins.inViewport(true)) {
            System.out.println("Turning the camera");
            Camera.turnTo(ruins);
        }

        boolean exiting = ruins.interact("Enter");

        if (!exiting) {
            System.out.println("Failed to interact with the ruins near RUINS_TILE");
            return false;
        }

        System.out.println("Waiting for the player to finish exiting");

        boolean exited = Condition.wait(() -> Areas.BLOOD_ALTAR_AREA.contains(Players.local()), 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the time limit");
            return false;
        }

        return true;
    }
}
