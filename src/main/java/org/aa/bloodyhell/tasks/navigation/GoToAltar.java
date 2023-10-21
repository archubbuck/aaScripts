package org.aa.bloodyhell.tasks.navigation;

import org.core.Task;
import org.aa.bloodyhell.constants.Areas;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.constants.Tiles;
import org.core.extensions.EquipmentExtensions;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Area;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;

public class GoToAltar implements Task {
    @Override
    public boolean activate() {
        return all(
                Objects.stream().name("Altar").action("Craft-runes").isEmpty(),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                all(
                        InventoryExtensions.contains(Items.PURE_ESSENCE)
//                        !PouchTracker.INSTANCE.hasPouchToFill()
                )
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {

        Area shortcutArea = new Area(new Tile(3534, 9775, 0), new Tile(3539, 9765, 0));

        boolean canTakeShortcut = Skills.level(Skill.Agility) >= 93;

        CaveNavigator caveNavigator = new CaveNavigator()
                .navigate(Areas.EDGEVILLE_AREA, Areas.CAVE_1_AREA, Tiles.CAVE_1_EXIT_TILE, null)
                .navigate(Areas.CAVE_1_AREA, Areas.CAVE_2_AREA, new Tile(3447, 9822), "Cave entrance")
                .navigate(Areas.CAVE_2_AREA, Areas.CAVE_3_AREA, new Tile(3466, 9820), "Cave entrance")
                .navigate(
                        Areas.CAVE_3_AREA,
                        canTakeShortcut ? shortcutArea : Areas.CAVE_4_AREA,
                        canTakeShortcut ? new Tile(3500, 9804) : new Tile(3483, 9832),
                        canTakeShortcut ? "Cave" : "Cave entrance"
                )
                .navigate(shortcutArea, Areas.CAVE_7_AREA, new Tile(3539, 9772), "Cave")
                .navigate(Areas.CAVE_4_AREA, Areas.CAVE_5_AREA, new Tile(3492, 9862), "Cave entrance")
                .navigate(Areas.CAVE_5_AREA, Areas.CAVE_6_AREA, Tiles.CAVE_5_EXIT, "Cave")
                .navigate(Areas.CAVE_6_AREA, Areas.CAVE_7_AREA, Tiles.CAVE_6_EXIT, "Cave")
                .navigate(Areas.CAVE_7_AREA, Areas.BLOOD_ALTAR_AREA, new Tile(3559, 9780), "Mysterious ruins");

        return caveNavigator.getResult();
    }
}
