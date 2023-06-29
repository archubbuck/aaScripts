package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Varpbits;

import java.util.Arrays;

public class ZoomOutTask extends AbstractTask {

    private static final int[] varpsToSkip = {1, 2, 3, 7, 10, 510, 520, 525, 530, 531, 532, 540};

    @Override
    public boolean activate() {
        return Camera.getZoom() > 0 && Arrays.stream(varpsToSkip).noneMatch(v -> Varpbits.varpbit(281) == v);
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Zooming the camera out");
        Camera.moveZoomSlider(-2);
        Condition.wait(() -> Camera.pitch(100), 150, 10);
    }
}
