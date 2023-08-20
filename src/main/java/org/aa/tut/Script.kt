package org.aa.tut

import org.aa.tut.tasks.Task
import org.aa.tut.tasks.camera.SetCameraZoom
import org.powbot.api.Notifications
import org.powbot.api.script.*
import org.powbot.api.script.paint.Paint
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.mobile.script.ScriptManager
import org.powbot.mobile.service.ScriptUploader
import java.util.logging.Logger

@ScriptManifest(
    name = "aaTut",
    description = "Completes Tutorial Island",
    author = "Farmer",
    version = "0.0.1",
    category = ScriptCategory.Other
)
@ScriptConfiguration.List(
    [
        ScriptConfiguration(
            name = "Username",
            description = "Type the username you wish to use.",
            optionType = OptionType.STRING
        ),
    ]
)
class Script : AbstractScript() {
    private val logger = Logger.getLogger(this.javaClass.name);

    private val tasks: List<Task> = listOf(
        SetCameraZoom()
    )

    lateinit var configuration: Configuration;

    override fun onStart() {
        super.onStart()
        loadConfiguration()
        loadPaint()
    }

    private fun loadConfiguration() {
        val username = getOption<String>("Username");
        if (username.isEmpty()) {
            Notifications.showNotification("Please enter a username.")
            ScriptManager.stop()
            return
        }
        configuration = Configuration(username)
    }

    private fun loadPaint() {
        val paint: Paint = PaintBuilder.newBuilder()
            .addString("Username") { configuration.username }
            .build()
        addPaint(paint)
    }

    override fun poll() {
        tasks.forEach {
            if (it.activate()) {
                logger.info(it.activationMessage)
                val success = it.execute()
                logger.info(if (success) it.successMessage else it.failureMessage )
            }
        }
    }

}

fun main() {
    ScriptUploader().uploadAndStart("aaTut", "", "127.0.0.1:5845", portForward = true, useDefaultConfigs = false)
}