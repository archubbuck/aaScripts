package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.constants.Tiles;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;
import static org.core.helpers.Conditions.any;

public class GoToCave1 implements Task {
    @Override
    public boolean activate() {
        return all(
                any(
                        Areas.EDGEVILLE_AREA.contains(Players.local()),
                        Areas.FAIRY_RING_AREA.contains(Players.local())
                ),
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
        System.out.println("Navigating to Areas.CAVE_1_AREA");

//        boolean navigated = Movement.builder(Areas.CAVE_1_AREA.getCentralTile())
//                .setRunMin(15)
//                .setRunMax(75)
//                .setUseTeleports(false)
//                .setWalkUntil(() -> Areas.CAVE_1_AREA.contains(Players.local()))
//                .move()
//                .getSuccess();

        boolean navigated = Movement.builder(Tiles.CAVE_1_EXIT_TILE)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .move()
                .getSuccess();

        if (!navigated) {
            System.out.println("Failed to navigate to Areas.CAVE_1_AREA");
            return false;
        }

        System.out.println("Successfully navigated to Areas.CAVE_1_AREA");

        return true;
    }
}
