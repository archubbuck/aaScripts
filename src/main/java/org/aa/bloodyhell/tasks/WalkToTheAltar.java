package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.tasks.navigation.*;
import org.core.Task;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.rt4.Players;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;
import static org.core.helpers.Conditions.any;

public class WalkToTheAltar implements Task {
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
    public boolean execute(AbstractScript abstractScript) {
        if (Areas.EDGEVILLE_AREA.contains(Players.local()) || Areas.FAIRY_RING_AREA.contains(Players.local())) {
            return new GoToCave1().execute(abstractScript);
        }

        if (Areas.CAVE_1_AREA.contains(Players.local())) {
            return new GoToCave2().execute(abstractScript);
        }

        if (Areas.CAVE_2_AREA.contains(Players.local())) {
            return new GoToCave3().execute(abstractScript);
        }

        if (Areas.CAVE_3_AREA.contains(Players.local())) {
            return new GoToCave4().execute(abstractScript);
        }

        if (Areas.CAVE_4_AREA.contains(Players.local())) {
            return new GoToCave7().execute(abstractScript);
        }

        if (Areas.CAVE_7_AREA.contains(Players.local())) {
            return new GoToBloodAltar().execute(abstractScript);
        }

        System.out.println("We are in an unexpected area!");
        System.out.println(Players.local().tile());

        return false;
    }

    // Player is inside of the Area
    // Interactable object is on the screen
    // ---- interact and wait
    // **** Starting Area, Target Area, Anchor Object

}
