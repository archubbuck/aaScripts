package org.aa.bloodyhell.tasks.navigation;

import org.aa.bloodyhell.Task;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.extensions.EquipmentExtensions;
import org.aa.bloodyhell.extensions.InventoryExtensions;
import org.aa.truebloods.Utils;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.helpers.PouchTracker;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.AbstractScript;

public class GoToAltar implements Task {
    @Override
    public boolean activate() {
        return Utils.all(
                Objects.stream().name("Altar").action("Craft-runes").isEmpty(),
                EquipmentExtensions.contains(Items.RUNECRAFTING_CAPES),
                EquipmentExtensions.contains(Items.STAVES),
                EquipmentExtensions.contains(Items.AMULETS_OF_GLORY),
                InventoryExtensions.contains(Items.POUCHES),
                Utils.all(
                        InventoryExtensions.contains(Items.PURE_ESSENCE),
                        !PouchTracker.INSTANCE.hasPouchToFill()
                )
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {

        if (atArea(Areas.EDGEVILLE_AREA)) {
            return navigateTo(Tiles.CAVE_1_EXIT);
        }

        if (atArea(Areas.CAVE_1_AREA)) {
            Tile tile = new Tile(3447, 9824);
            GameObject caveEntrance = Objects.stream().name("Cave entrance").within(tile, 3).first();
            return caveEntrance.valid() && caveEntrance.interact("Enter") && Condition.wait(() -> atArea(Areas.CAVE_2_AREA), 150, 10);
        }

        if (atArea(Areas.CAVE_2_AREA)) {
            Tile tile = new Tile(3466, 9820);
            GameObject caveEntrance = Objects.stream().name("Cave entrance").within(tile, 1).first();
            return caveEntrance.valid() && caveEntrance.interact("Enter") && Condition.wait(() -> atArea(Areas.CAVE_3_AREA), 150, 10);
        }

        Area shortcutArea = new Area(new Tile(3534, 9775, 0), new Tile(3539, 9765, 0));

        if (atArea(Areas.CAVE_3_AREA)) {
            Tile tile = Skills.level(Skill.Agility) >= 93 ? new Tile(3500, 9804) : new Tile(3483, 9832);
            String caveObjName = Skills.level(Skill.Agility) >= 93 ? "Cave" : "Cave entrance";
            Area targetArea = Skills.level(Skill.Agility) >= 93 ? shortcutArea : Areas.CAVE_4_AREA;
            if (tile.distance() > 5) {
                boolean navigated = Movement.builder(tile)
                        .setRunMin(15)
                        .setRunMax(75)
                        .setUseTeleports(false)
                        .move()
                        .getSuccess();
                if (!navigated) return false;
            }
            GameObject caveEntrance = Objects.stream().name(caveObjName).within(tile, 1).first();
            return caveEntrance.valid() && caveEntrance.interact("Enter") && Condition.wait(() -> atArea(targetArea), 150, 10);
        }

        if (atArea(shortcutArea)) {
            Tile tile = new Tile(3539, 9772);
            GameObject caveEntrance = Objects.stream().name("Cave").within(tile, 1).first();
            return caveEntrance.valid() && caveEntrance.interact("Enter") && Condition.wait(() -> atArea(Areas.CAVE_7_AREA), 150, 10);
        }

        if (atArea(Areas.CAVE_4_AREA)) {
            Tile tile = new Tile(3492, 9862);
            if (tile.distance() > 5) {
                boolean navigated = Movement.builder(tile)
                        .setRunMin(15)
                        .setRunMax(75)
                        .setUseTeleports(false)
                        .move()
                        .getSuccess();
                if (!navigated) return false;
            }
            GameObject caveEntrance = Objects.stream().name("Cave entrance").within(tile, 1).first();
            return caveEntrance.valid()
                    && caveEntrance.interact("Enter")
                    && Condition.wait(() -> atArea(Areas.CAVE_5_AREA), 125, 25);
        }

        if (atArea(Areas.CAVE_5_AREA)) {
            GameObject caveEntrance = Objects.stream().name("Cave").within(Tiles.CAVE_5_EXIT, 1).first();

            if (!caveEntrance.inViewport(true)) {
                boolean navigated = Movement.builder(Tiles.CAVE_5_EXIT)
                        .setRunMin(15)
                        .setRunMax(75)
                        .setUseTeleports(false)
                        .move()
                        .getSuccess();
                if (!navigated) return false;
            }

            return caveEntrance.valid()
                    && caveEntrance.interact("Enter")
                    && Condition.wait(() -> atArea(Areas.CAVE_6_AREA), 125, 25);
        }

        if (atArea(Areas.CAVE_6_AREA)) {
            GameObject caveEntrance = Objects.stream().name("Cave").within(Tiles.CAVE_6_EXIT, 1).first();

            if (!caveEntrance.inViewport(true)) {
                boolean navigated = Movement.builder(Tiles.CAVE_6_EXIT)
                        .setRunMin(15)
                        .setRunMax(75)
                        .setUseTeleports(false)
                        .move()
                        .getSuccess();
                if (!navigated) return false;
            }

            return caveEntrance.valid()
                    && caveEntrance.interact("Enter")
                    && Condition.wait(() -> atArea(Areas.CAVE_7_AREA), 125, 25);
        }

        if (atArea(Areas.CAVE_7_AREA)) {
            Area ruinsArea = new Area(new Tile(3557, 9786, 0), new Tile(3565, 9780, 0));

            if (ruinsArea.getCentralTile().distance() > 5) {
                boolean navigated = Movement.builder(ruinsArea.getRandomTile())
                        .setRunMin(15)
                        .setRunMax(75)
                        .setUseTeleports(false)
                        .move()
                        .getSuccess();
                if (!navigated) return false;
            }

            GameObject mysteriousRuins = Objects.stream().name("Mysterious ruins").action("Enter").first();

            return mysteriousRuins.valid()
                    && mysteriousRuins.interact("Enter")
                    && Condition.wait(() -> atArea(Areas.BLOOD_ALTAR_AREA), 150, 10);
        }

        return false;
    }

    private boolean navigateTo(Locatable locatable) {
        return Movement.builder(locatable)
                .setRunMin(15)
                .setRunMax(75)
                .setUseTeleports(false)
                .move()
                .getSuccess();
    }

    private boolean atArea(Area area) {
        return area.contains(Players.local());
    }
}
