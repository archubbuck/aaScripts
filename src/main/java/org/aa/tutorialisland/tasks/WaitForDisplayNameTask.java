package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Components;

public class WaitForDisplayNameTask extends AbstractTask {

    private final int SET_DISPLAY_NAME_WIDGET_ID = 558;

    @Override
    public boolean activate() {
        return displayNameTextBoxShown();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Waiting for user to enter display name");
        Condition.wait(() -> !displayNameTextBoxShown(), 150, 15);
    }

    private boolean displayNameTextBoxShown() {
        return Components.stream(SET_DISPLAY_NAME_WIDGET_ID).anyMatch(c -> c.actions().contains("Set name") && c.valid());
    }
}
