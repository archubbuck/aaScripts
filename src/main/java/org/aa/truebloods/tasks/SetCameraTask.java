package org.aa.truebloods.tasks;

import org.aa.truebloods.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Store;
import org.powbot.api.script.AbstractScript;

import java.util.logging.Logger;

public class SetCameraTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean activate() {
        return !zoomValid() || !pitchValid();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        logger.info("Setting the camera");

        if (Bank.opened()) {
            logger.info("Attempting to close the bank");
            boolean closedTheBank = Bank.close() && Condition.wait(() -> !Bank.opened(), 75, 30);
            logger.info(closedTheBank ? "We closed the bank!" : "We failed to close the bank!");
        }

        if (Store.opened()) {
            logger.info("Attempting to close the store");
            boolean closedTheStore = Store.close() && Condition.wait(() -> !Store.opened(), 75, 30);
            logger.info(closedTheStore ? "We closed the store!" : "We failed to close the store!");
        }

        if (!zoomValid()) {
            logger.info("Attempting to set the camera zoom");
            Camera.moveZoomSlider(Camera.ZOOM_MIN);
            logger.info(
                    Condition.wait(this::zoomValid, 75, 30)
                            ? "We zoomed the camera out!"
                            : "We failed to zoom the camera out!"
            );
        }

        if (!pitchValid()) {
            logger.info("Attempting to set the camera pitch");
            logger.info(
                    Camera.pitch(true) && Condition.wait(this::pitchValid, 75, 30)
                            ? "We zoomed the camera out!"
                            : "We failed to zoom the camera out!");
        }

    }

    private boolean zoomValid() {
        double TARGET_ZOOM = 4;
        return Camera.getZoom() <= TARGET_ZOOM;
    }

    private boolean pitchValid() {
        int TARGET_PITCH = 85;
        return Camera.pitch() >= TARGET_PITCH;
    }
}
