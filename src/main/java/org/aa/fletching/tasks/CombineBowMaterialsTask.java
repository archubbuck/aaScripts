package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.fletching.FletchingScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;

import java.util.logging.Logger;

public class CombineBowMaterialsTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return !Bank.opened()
                && Skills.timeSinceExpGained(Skill.Fletching) > 2250
                && Components.stream().action("String").viewable().isEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        abstractScript.setStatus("Combining bow materials");

        Item bowString = Inventory.stream().filter(item -> item.id() == FletchingScript.BOW_STRING_ID).any();
        Item yewLongU = Inventory.stream().filter(item -> item.id() == FletchingScript.bow.getUnstrungId()).any();

//        boolean combinedItems = (Random.nextBoolean() ? bowString.useOn(yewLongU) : yewLongU.useOn(bowString))
//                && Condition.wait(() -> Components.stream().action("String").viewable().isNotEmpty(), 150, 15);

        boolean combinedItems = (Random.nextBoolean() ? bowString.useOn(yewLongU) : yewLongU.useOn(bowString))
                && Condition.wait(() -> Components.stream().action("String").viewable().isNotEmpty(), 75, 30);

        logger.info(combinedItems ? "Combined items" : "Failed to combine items");
    }
}
