package org.aa.tut.tasks.camera

import org.aa.tut.tasks.Task
import org.powbot.api.Condition
import org.powbot.api.rt4.Camera

class SetCameraZoom : Task(
    "Setting the camera zoom",
    "Successfully set the camera zoom",
    "Failed to set the camera zoom"
) {
    private val targetZoom = 4.0;

    override fun activate(): Boolean {
        return !zoomValid();
    }

    override fun execute(): Boolean {
        Camera.moveZoomSlider(targetZoom)
        return Condition.wait(this::zoomValid, 75, 30);
    }

    private fun zoomValid(): Boolean {
        return Camera.zoom <= 4;
    }
}