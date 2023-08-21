package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.Task;
import org.aa.bloodyhell.constants.Items;
import org.aa.bloodyhell.extensions.InventoryExtensions;
import org.aa.truebloods.Utils;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Objects;
import org.powbot.api.script.AbstractScript;

public class CraftRunes implements Task {
    @Override
    public boolean activate() {
        return Utils.all(
                getAltar().valid(),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public boolean execute(AbstractScript abstractScript) {
        GameObject altar = getAltar();

        if (!altar.valid()) return false;

        return altar.interact("Craft-rune")
                && Condition.wait(() -> InventoryExtensions.doesNotContain(Items.PURE_ESSENCE), 75, 15);
//        return altar.interact("Craft-rune")
//                && Condition.wait(() -> Skills.timeSinceExpGained(Skill.Runecrafting) <= 1000, 50, 20);
    }

    private GameObject getAltar() {
        return Objects.stream().name("Altar").action("Craft-rune").first();
    }
}
