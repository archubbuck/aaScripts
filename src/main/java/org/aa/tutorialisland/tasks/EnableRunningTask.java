package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Movement;

public class EnableRunningTask extends AbstractTask {
    @Override
    public boolean activate() {
        return !Movement.running();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Enabling running");
        if (Movement.running(true)) {
            Condition.wait(Movement::running, 150, 15);
        }
    }
}
