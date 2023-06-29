package org.aa.fletching.tasks;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Component;
import org.powbot.api.rt4.Components;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;

import java.util.logging.Logger;

public class FletchBowTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean activate() {
        return Components.stream().widget(270).action("String").viewable().isNotEmpty();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Confirming that we want to fletch");

        Component stringComponent = Components.stream().widget(270).action("String").viewable().first();

        boolean confirmedWeWantToFletch = stringComponent.click()
                && Condition.wait(() -> Skills.timeSinceExpGained(Skill.Fletching) < 2250, 75, 30);

        logger.info(confirmedWeWantToFletch ? "Fletching bows" : "Failed to confirm that we want to fletch");
    }
}

//public class FletchBowTask implements Task {
//    Logger logger = Logger.getLogger(this.getClass().getSimpleName());
//
//    Component stringComponent;
//
//    @Override
//    public boolean activate() {
//        return getStringComponent().visible();
//    }
//
//    @Override
//    public void execute(AbstractScript abstractScript) {
//        abstractScript.setStatus("Confirming that we want to fletch");
//
//        boolean confirmedWeWantToFletch = stringComponent.valid() && stringComponent.click()
//                && Condition.wait(() -> {
//                    boolean c1 = Skills.timeSinceExpGained(Skill.Fletching) < 2250;
//                    if (!c1) {
//                        logger.info("WAITING ON XP");
//                    } else {
//                        logger.info(stringComponent.getIds().toString());
//                    }
//                    return c1;
//        }, 75, 30);
//
//        logger.info(confirmedWeWantToFletch ? "Fletching bows" : "Failed to confirm that we want to fletch");
//    }
//
//    private Component getStringComponent() {
//        stringComponent = Components.stream(270, 14).action("String").first();
//        return stringComponent;
//    }
//}
