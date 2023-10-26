package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.constants.Tiles;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;
import static org.core.helpers.Conditions.any;

public class GoToCave1 extends Task {
    @Override
    public boolean activate() {
        return all(
                any(
                        Areas.EDGEVILLE_AREA.contains(Players.local()),
                        Areas.FAIRY_RING_AREA.contains(Players.local())
                ),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        System.out.println("Navigating to Areas.CAVE_1_AREA");

//        Tile tile = Areas.FAIRY_RING_AREA.getRandomTile();
        Tile tile = new Tile(3129, 3497, 0)
                .derive(Random.nextInt(-2,3), Random.nextInt(-2,3));

        if (!tile.loaded()) {
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

//        boolean navigated = Movement.builder(Tiles.CAVE_1_EXIT_TILE)
//                .setRunMin(15)
//                .setRunMax(75)
//                .setUseTeleports(false)
//                .move()
//                .getSuccess();
//
//        if (!navigated) {
//            System.out.println("Failed to navigate to Areas.CAVE_1_AREA");
//            return;
//        }

        System.out.println("Successfully navigated to Areas.CAVE_1_AREA");
    }
}
