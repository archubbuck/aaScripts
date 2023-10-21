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

public class GoToCave7 implements Task {

    @Override
    public boolean activate() {
        return all(
                Objects.stream().name("Altar").action("Craft-runes").isEmpty(),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE),
                Areas.CAVE_4_AREA.contains(Players.local())
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        System.out.println("Navigating to SHORTCUT_AREA");

        if (Tiles.CAVE_4_EXIT_TILE.distance() >= 5) {
            boolean navigated = Movement.builder(Tiles.CAVE_4_EXIT_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> Tiles.CAVE_4_EXIT_TILE.distance() < 5)
                    .move()
                    .getSuccess();
        }

        GameObject ruins = Objects.stream().name("Cave")
                .nearest(Tiles.CAVE_4_EXIT_TILE)
                .first();

        if (!ruins.valid()) {
            System.out.println("Unable to locate the ruins near Tiles.CAVE_4_EXIT_TILE");
            return false;
        }

        if (!ruins.inViewport(true)) {
            System.out.println("Turning the camera");
            Camera.turnTo(ruins);
        }

        boolean exiting = ruins.interact("Enter");

        if (!exiting) {
            System.out.println("Failed to interact with the ruins near Tiles.CAVE_4_EXIT_TILE");
            return false;
        }

        System.out.println("Waiting for the player to finish exiting");

        boolean exited = Condition.wait(() -> Areas.CAVE_7_AREA.contains(Players.local()), 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the time limit");
            return false;
        }

        return true;

//        GameObject exit = Objects.stream().name("Cave")
//                .nearest(SHORTCUT_EXIT_TILE)
//                .viewable()
//                .first();
//
//        if (!exit.valid()) {
//            System.out.println("Attempting navigation via Dax");
//            boolean navigated = Movement.builder(SHORTCUT_AREA.getCentralTile())
//                    .setRunMin(15)
//                    .setRunMax(75)
//                    .setUseTeleports(false)
//                    .setWalkUntil(() -> SHORTCUT_AREA.contains(Players.local()))
//                    .move()
//                    .getSuccess();
//        }
//
//        if (!exit.valid()) {
//            System.out.println("Unable to locate the exit near SHORTCUT_EXIT_TILE");
//            return false;
//        }
//
//        System.out.println("Successfully located the exit near SHORTCUT_EXIT_TILE");
//
//        if (!exit.inViewport()) {
//            System.out.println("Turning the camera");
//            Camera.turnTo(exit);
//        }
//
//        System.out.println("Interacting");
//
//        boolean exiting = exit.interact("Enter");
//
//        if (!exiting) {
//            System.out.println("Failed to interact with the exit near Tiles.SHORTCUT_EXIT_TILE");
//            return false;
//        }
//
//        System.out.println("Waiting for the player to finish exiting");
//
//        boolean exited = Condition.wait(() -> Areas.CAVE_7_AREA.contains(Players.local()), 150, 20);
//
//        if (!exited) {
//            System.out.println("Failed to exit the cave within the time limit");
//            return false;
//        }
//
//        return true;
    }
}
