package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.constants.Tiles;
import org.aa.bloodyhell.tasks.navigation.*;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.*;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.local.LocalPathFinder;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;

public class WalkToTheAltar extends Task {
    @Override
    public boolean activate() {
        return all(
                !Areas.BLOOD_ALTAR_AREA.contains(Players.local()),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        boolean takeShortcut = Skills.level(Skill.Agility) >= 93;

        if (Areas.EDGEVILLE_AREA.contains(Players.local()) || Areas.FAIRY_RING_AREA.contains(Players.local())) {
            goToCave1();
            return;
        }

        if (Areas.CAVE_1_AREA.contains(Players.local())) {
            goToCave2();
            return;
        }

        if (Areas.CAVE_2_AREA.contains(Players.local())) {
            goToCave3();
            return;
        }

        if (Areas.CAVE_3_AREA.contains(Players.local())) {
            if (takeShortcut) goToCave4();
            else goToCave5();
            return;
        }

        if (Areas.CAVE_4_AREA.contains(Players.local())) {
            goToCave10();
            return;
        }

        if (Areas.CAVE_5_AREA.contains(Players.local())) {
            goToCave6();
            return;
        }

        if (Areas.CAVE_6_AREA.contains(Players.local())) {
            goToCave7();
            return;
        }

        if (Areas.CAVE_7_AREA.contains(Players.local())) {
            goToCave8();
            return;
        }

        if (Areas.CAVE_10_AREA.contains(Players.local())) {
            goToBloodAltar();
            return;
        }

        System.out.println("We are in an unexpected area!");
        System.out.println(Players.local().tile());
    }

    private void goToCave1() {
        System.out.println("Navigating to Areas.CAVE_1_AREA");

        Tile tile = new Tile(3129, 3497, 0)
                .derive(Random.nextInt(-2,3), Random.nextInt(-2,3));

        if (!tile.loaded() || tile.distance() > 5) {
            boolean navigated = Movement.builder(tile)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> tile.distance() <= 5)
                    .move()
                    .getSuccess();

            if(!navigated) {
                System.out.println("Failed to navigate to " + tile);
                return;
            }
        }

        GameObject fairyRing = Objects.stream()
                .nameContains("Fairy ring")
                .within(Areas.FAIRY_RING_AREA)
                .first();

        if (!fairyRing.valid()) {
            System.out.println("Unable to locate the fair ring!");
            return;
        }

        boolean fairyRingConfigured = fairyRing.actions().stream().anyMatch(action -> action.equals("Last-destination (DLS)"));

        if (!fairyRingConfigured) {
            System.out.println("Configuring the fairy ring!");
            fairyRingConfigured = FairyRing.open()
                    && FairyRing.teleport("DLS")
                    && Condition.wait(() -> Areas.CAVE_1_AREA.contains(Players.local()), 10, 200);
            if (!fairyRingConfigured) {
                System.out.println("Failed to configure the fairy ring!");
                return;
            }
        }

        boolean teleported = fairyRing.interact("Last-destination (DLS)")
                && Condition.wait(() -> Areas.CAVE_1_AREA.contains(Players.local()), 20, 150);

        if (!teleported) {
            System.out.println("Failed to teleport via the fairy ring");
            return;
        }

        Condition.sleep(Random.nextInt(450, 650));

        System.out.println("Successfully navigated to Areas.CAVE_1_AREA");
    }

    private void goToCave2() {
        System.out.println("Navigating to Areas.CAVE_2_AREA");

        GameObject exit = Objects.stream().name("Cave entrance")
                .within(Tiles.CAVE_1_EXIT_TILE, 1)
                .first();

        if (!exit.valid()) {
            System.out.println("Unable to locate the entrance to Areas.CAVE_2_AREA");
            return;
        }

        boolean entered = exit.interact("Enter")
                && Condition.wait(() -> Areas.CAVE_2_AREA.contains(Players.local()), 150, 20);

        System.out.println(entered ? "Successfully entered the cave!" : "Failed to enter the cave!");
    }

    private void goToCave3() {
        System.out.println("Navigating to Areas.CAVE_3_AREA");

        if (Tiles.CAVE_2_EXIT_TILE.distance() >= 5) {
            boolean navigated = Movement.builder(Tiles.CAVE_3_EXIT_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> Tiles.CAVE_2_EXIT_TILE.distance() < 5)
                    .move()
                    .getSuccess();
        }

        GameObject exit = Objects.stream().name("Cave entrance")
                .nearest(Tiles.CAVE_2_EXIT_TILE)
                .first();

        if (!exit.valid()) {
            System.out.println("Unable to locate the exit near Tiles.CAVE_2_EXIT_TILE");
            return;
        }

        System.out.println("Successfully located the exit near Tiles.CAVE_2_EXIT_TILE");

        if (!exit.inViewport(true)) {
            System.out.println("Turning the camera");
            Camera.turnTo(exit);
        }

        System.out.println("Interacting");

        boolean exiting = exit.interact("Enter");

        if (!exiting) {
            System.out.println("Failed to interact with the exit near Tiles.CAVE_2_EXIT_TILE");
            return;
        }

        System.out.println("Waiting for the player to finish exiting");

        boolean exited = Condition.wait(() -> Areas.CAVE_3_AREA.contains(Players.local()), 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the time limit");
        }
    }

    private void goToCave4() {
        System.out.println("Navigating to Areas.CAVE_4_AREA");

        if (Tiles.CAVE_3_EXIT_TILE.distance() >= 5) {
            boolean navigated = Movement.builder(Tiles.CAVE_3_EXIT_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> Tiles.CAVE_3_EXIT_TILE.distance() < 5)
                    .move()
                    .getSuccess();
        }

        GameObject exit = Objects.stream().name("Cave")
                .nearest(Tiles.CAVE_3_EXIT_TILE)
                .first();

        if (!exit.valid()) {
            System.out.println("Unable to locate the exit near CAVE_3_EXIT_TILE");
            return;
        }

        System.out.println("Successfully located the exit near CAVE_3_EXIT_TILE");

        if (!exit.inViewport(true)) {
            System.out.println("Turning the camera");
            Camera.turnTo(exit);
        }

        System.out.println("Interacting");

        boolean exiting = exit.interact("Enter");

        if (!exiting) {
            System.out.println("Failed to interact with the exit near CAVE_3_EXIT_TILE");
            return;
        }

        System.out.println("Waiting for the player to finish exiting");

        boolean exited = Condition.wait(() -> Areas.CAVE_4_AREA.contains(Players.local()), 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the time limit");
            return;
        }
    }

    private void goToCave5() {
        System.out.println("Navigating to Areas.CAVE_5_AREA");

        Tile tile = new Tile(3484, 9832, 0);

        GameObject exit = Objects.stream().name("Cave entrance")
                .at(tile)
                .first();

        if (!exit.valid() || exit.distance() >= 5) {
            boolean navigated = Movement.builder(tile)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> exit.valid() && exit.distance() < 5)
                    .move()
                    .getSuccess();
            if (!navigated) {
                System.out.println("Failed to navigate to the exit tile");
                return;
            }
        }

        if (!exit.inViewport(true)) {
            System.out.println("Angling the camera to the exit");
            Camera.angleToLocatable(exit);
        }

        System.out.println("Attempting to exit the cave");

        boolean exited = exit.interact("Enter")
                && Condition.wait(() -> {
                    boolean satisfied = Areas.CAVE_5_AREA.contains(Players.local());
                    if (satisfied) {
                        System.out.println("Successfully exited the cave");
                    }
                    return satisfied;
                }, 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the threshold");
        }
    }

    private void goToCave6() {
        System.out.println("Navigating to cave 6");

        Tile ENTRANCE_TILE = new Tile(3492, 9861, 0);

        GameObject exit = Objects.stream().name("Cave entrance")
                .at(ENTRANCE_TILE)
                .first();

        if (!exit.valid() || exit.distance() >= 5) {
            boolean navigated = Movement.builder(ENTRANCE_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> exit.valid() && exit.distance() < 5)
                    .move()
                    .getSuccess();
            if (!navigated) {
                System.out.println("Failed to navigate to the exit tile");
                return;
            }
        }

        System.out.println("Attempting to enter the cave");
        boolean exited = exit.interact("Enter")
                && Condition.wait(() -> {
            boolean satisfied = Areas.CAVE_6_AREA.contains(Players.local());
            if (satisfied) {
                System.out.println("Successfully exited the cave");
            }
            return satisfied;
        }, 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the threshold");
            return;
        }
    }

    private void goToCave7() {
        System.out.println("Navigating to cave 7");

//        Tile ENTRANCE_TILE = new Tile(3560, 9814, 0);
        Tile ENTRANCE_TILE = new Tile(3560, 9813, 0);

        GameObject exit = Objects.stream().name("Cave")
                .at(ENTRANCE_TILE)
                .first();

        if (!exit.valid() || exit.distance() >= 5) {
            boolean navigated = Movement.builder(ENTRANCE_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> exit.valid() && exit.distance() < 5)
                    .move()
                    .getSuccess();
            if (!navigated) {
                System.out.println("Failed to navigate to the exit tile");
                return;
            }
        }

        System.out.println("Attempting to enter the cave");
        boolean exited = exit.interact("Enter")
                && Condition.wait(() -> {
            boolean satisfied = Areas.CAVE_7_AREA.contains(Players.local());
            if (satisfied) {
                System.out.println("Successfully exited the cave");
            }
            return satisfied;
        }, 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the threshold");
            return;
        }

//        Tile[] path = {
//                new Tile(3513, 9811, 0),
//                new Tile(3522, 9815, 0),
//                new Tile(3527, 9819, 0),
//                new Tile(3535, 9821, 0),
//                new Tile(3542, 9826, 0),
//                new Tile(3552, 9826, 0),
//                new Tile(3558, 9822, 0),
//                new Tile(3559, 9815, 0)
//        };
//
//        TilePath tilePath = Movement.newTilePath(path);
//
//        boolean traversed = Condition.wait(() -> {
//            boolean distanceBreached = tilePath.end().distance() > 3;
//            if (distanceBreached) {
//                Tile nextTile = tilePath.next();
//                boolean traversedInternally = tilePath.traverse()
//                        && Condition.wait(() -> nextTile.distance() <= 3, )
//            }
//            return !distanceBreached && !Players.local().inMotion()
//        }, 175, 30);
//        System.out.println(traversed ? "traversed" : "failed traversed");

    }

    private void goToCave8() {
        System.out.println("Navigating to cave 8");

        Tile ENTRANCE_TILE = new Tile(3555, 9787, 0);

        GameObject exit = Objects.stream().name("Cave")
                .at(ENTRANCE_TILE)
                .first();

        boolean exitValid = exit.valid();
        System.out.println("Exit valid" + String.valueOf(exitValid));

        if (!exitValid || exit.distance() >= 5) {
            System.out.println("Navigating with dax because why tf not");
            boolean navigated = Movement.builder(ENTRANCE_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> exit.valid() && exit.distance() < 5)
                    .move()
                    .getSuccess();
            if (!navigated) {
                System.out.println("Failed to navigate to the exit tile");
                return;
            }
        }

        System.out.println("Attempting to enter the cave");
        boolean exited = exit.interact("Enter")
                && Condition.wait(() -> {
            boolean satisfied = Areas.CAVE_10_AREA.contains(Players.local());
            if (satisfied) {
                System.out.println("Successfully exited the cave");
            }
            return satisfied;
        }, 150, 20);

        if (!exited) {
            System.out.println("Failed to exit the cave within the threshold");
            return;
        }
    }

    private void goToCave10() {
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

    private void goToBloodAltar() {
        final Tile RUINS_TILE = new Tile(3560, 9780, 0);

        if (RUINS_TILE.distance() > 3) {
            boolean navigated = Movement.builder(RUINS_TILE)
                    .setRunMin(15)
                    .setRunMax(75)
                    .setUseTeleports(false)
                    .setWalkUntil(() -> RUINS_TILE.distance() <= 3)
                    .move()
                    .getSuccess();
        }

        GameObject ruins = Objects.stream().name("Mysterious ruins")
//                .within(20)
//                .nearest(RUINS_TILE)
                .first();

        if (!ruins.valid()) {
            System.out.println("Unable to locate the ruins near RUINS_TILE");
            return;
        }

        if (!ruins.inViewport(true)) {
            System.out.println("Turning the camera");
            Camera.turnTo(ruins);
        }

        boolean exiting = ruins.interact("Enter", true);

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
