package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.constants.Tiles;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;

public class GoToCave7 extends Task {

    @Override
    public boolean activate() {
        return all(
                Areas.CAVE_4_AREA.contains(Players.local()),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        System.out.println("Navigating to SHORTCUT_AREA");

        GameObject exit = Objects.stream().name("Cave")
                .within(Tiles.CAVE_4_EXIT_TILE, 1)
                .first();

        if (!exit.valid()) {
            System.out.println("Unable to locate the cave near Tiles.CAVE_4_EXIT_TILE");
            return;
        }

        boolean entered = exit.interact("Enter")
                && Condition.wait(() -> Areas.CAVE_7_AREA.contains(Players.local()), 150, 20);

        System.out.println(entered ? "Successfully entered the cave!" : "Failed to enter the cave!");
    }

//    @Override
//    public void execute(AbstractScript abstractScript) {
//        System.out.println("Navigating to SHORTCUT_AREA");
//
//        if (Tiles.CAVE_4_EXIT_TILE.distance() >= 5) {
//            boolean navigated = Movement.builder(Tiles.CAVE_4_EXIT_TILE)
//                    .setRunMin(15)
//                    .setRunMax(75)
//                    .setUseTeleports(false)
//                    .setWalkUntil(() -> Tiles.CAVE_4_EXIT_TILE.distance() < 5)
//                    .move()
//                    .getSuccess();
//        }
//
//        GameObject ruins = Objects.stream().name("Cave")
//                .nearest(Tiles.CAVE_4_EXIT_TILE)
//                .first();
//
//        if (!ruins.valid()) {
//            System.out.println("Unable to locate the ruins near Tiles.CAVE_4_EXIT_TILE");
//            return;
//        }
//
//        if (!ruins.inViewport(true)) {
//            System.out.println("Turning the camera");
//            Camera.turnTo(ruins);
//        }
//
//        boolean exiting = ruins.interact("Enter");
//
//        if (!exiting) {
//            System.out.println("Failed to interact with the ruins near Tiles.CAVE_4_EXIT_TILE");
//            return;
//        }
//
//        System.out.println("Waiting for the player to finish exiting");
//
//        boolean exited = Condition.wait(() -> Areas.CAVE_7_AREA.contains(Players.local()), 150, 20);
//
//        if (!exited) {
//            System.out.println("Failed to exit the cave within the time limit");
//        }
//    }
}
