package org.aa.pickaxecollector.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Camera;

public class SetCameraZoomTask extends AbstractTask {

    @Override
    public boolean activate() {
        return shouldAdjustZoom();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        abstractScript.setStatus("Adjusting the zoom");
        Camera.moveZoomSlider(0);

        if (!Condition.wait(() -> !shouldAdjustZoom(), 150, 15)) {
            abstractScript.setStatus("Unable to adjust the zoom");
        }

    }

    private boolean shouldAdjustZoom() {
        return Camera.getZoom() > 0;
    }

}
