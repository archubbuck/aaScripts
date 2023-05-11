package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Inventory;

public class EnableSingleTapDropTask extends AbstractTask {

    @Override
    public boolean activate() {
        return !Inventory.shiftDroppingEnabled();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        if (Chat.canContinue()) {
            abstractScript.setStatus("Continuing the chat");
            if (!Chat.continueChat() || !Condition.wait(() -> !Chat.canContinue(), 150, 15)) {
                abstractScript.setStatus("Unable to continue the chat");
                return;
            }
        }

        abstractScript.setStatus("Setting mouse toggle action to " + Game.MouseToggleAction.DROP.name());
        if (!Game.setMouseToggleAction(Game.MouseToggleAction.DROP)) {
            abstractScript.setStatus("Unable to set mouse toggle action to " + Game.MouseToggleAction.DROP.name());
            return;
        }

        abstractScript.setStatus("Enabling single tap dropping");
        if (!Game.setMouseActionToggled(true) || !Condition.wait(Inventory::shiftDroppingEnabled, 150, 15)) {
            abstractScript.setStatus("Unable to enable single tap dropping");
        }

    }
}
