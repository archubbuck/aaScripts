package org.aa.gnome2

import com.google.common.eventbus.Subscribe
import org.aa.gnome2.models.Activity
import org.aa.gnome2.models.ActivityCategory
import org.powbot.api.event.InventoryChangeEvent
import org.powbot.api.script.*
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.mobile.service.ScriptUploader
import java.util.logging.Logger

@ScriptManifest(
        name = "KotGnomes",
        description = "Does gnome stuff.",
        version = "1.0.0",
        category = ScriptCategory.MoneyMaking,
        author = "Farmer"
)
@ScriptConfiguration.List(
        [
            ScriptConfiguration(
                    name = "Activity Category",
                    description = "The category of activities that you would like to automate.",
                    optionType = OptionType.STRING,
                    defaultValue = "Shop",
                    allowedValues = arrayOf("")
            ),
            ScriptConfiguration(
                    name = "Activity",
                    description = "The activity that you would like to automate.",
                    optionType = OptionType.STRING,
                    defaultValue = "Heckel Funch"
            )
        ]
)
class Script : AbstractScript() {
    private val logger = Logger.getLogger(this.javaClass.name);

    override fun onStart() {
        super.onStart()
        addPaint(
                PaintBuilder.newBuilder()
                        .build()
        )
    }

    @Subscribe
    fun inventoryChanged(inventoryChangeEvent: InventoryChangeEvent) {
        logger.info("DONE DONE DONE")
    }

    override fun showOptionsDialog(onCompletion: () -> Unit) {
        super.showOptionsDialog(onCompletion)
        updateAllowedOptions("Activity Category", ActivityCategory.values().map { it.displayName }.toTypedArray())
    }

    @ValueChanged(keyName = "Activity Category")
    fun activityCategoryChanged(newValue: String) {

        updateEnabled("Activity", true)

        val newAllowedOptions = Activity.values()
            .filter { it.category.displayName == newValue }
            .map { it.displayName }

        updateAllowedOptions("Activity", newAllowedOptions.toTypedArray())

//        val newCategory = ActivityCategory.values().first { it.displayName == newValue }


//        val activityCategory: ActivityCategory = ActivityCategory.valueOf(newValue)
//        val allowedOptions = Activity.values()
//                .filter { it.category == activityCategory }
//                .map { it.displayName }
//                .sortedByDescending { it }
//                .toTypedArray()
//        updateAllowedOptions("Activity", allowedOptions)
//        logger.info(Activity.values().joinToString { "," });

//        val newAllowedValues = Activity.values().filter { it.category.displayName == newValue }
//
//        logger.info(newAllowedValues.size.toString())
//
//        updateAllowedOptions("Activity", newAllowedValues.map { it.displayName }.toTypedArray())

//        for (item in ActivityCategory.values()) {
//            logger.info(item.displayName)
//            logger.info(item.description)
//        }

//        logger.info(ActivityCategory.values().map { it.displayName })
    }

    override fun poll() {
        TODO("Not yet implemented")
    }

}

fun main() {
    ScriptUploader().uploadAndStart("KotGnomes", "", "127.0.0.1:5845", portForward = true, useDefaultConfigs = false)
}