package org.aa.bloodyhell.tasks;

import org.core.Task;
import org.aa.bloodyhell.constants.Items;
import org.core.extensions.InventoryExtensions;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Objects;
import org.powbot.api.script.AbstractScript;

import static org.core.helpers.Conditions.all;

public class CraftRunes extends Task {
    @Override
    public boolean activate() {
        return all(
                getAltar().valid(),
                InventoryExtensions.contains(Items.PURE_ESSENCE)
        );
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        GameObject altar = getAltar();

        if (!altar.valid()) return;

        boolean crafted = altar.interact("Craft-rune")
                && Condition.wait(() -> InventoryExtensions.doesNotContain(Items.PURE_ESSENCE), 125, 15);
    }

    private GameObject getAltar() {
        return Objects.stream().name("Altar").action("Craft-rune").first();
    }
}
