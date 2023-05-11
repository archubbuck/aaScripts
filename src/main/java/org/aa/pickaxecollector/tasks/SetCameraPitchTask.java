package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Camera;

public class SetCameraPitchTask extends AbstractTask {

    @Override
    public boolean activate() {
        return shouldAdjustPitch();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        abstractScript.setStatus("Adjusting the pitch");
        if (!Camera.pitch(100) || Condition.wait(() -> !shouldAdjustPitch(), 150, 15)) {
            abstractScript.setStatus("Unable to adjust the pitch");
        }

    }

    private boolean shouldAdjustPitch() {
        return Camera.pitch() < 100;
    }

}
