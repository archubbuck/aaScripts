
package org.aa.bloods;

import com.google.common.eventbus.Subscribe;
import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.aa.Task;
import org.aa.bloods.tasks.*;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

@ScriptManifest(
        name = "aaBloods",
        description = "Crafts blood runes in Arceuus",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Runecrafting)
public class BloodsScript extends AbstractScript {

    private final Task[] tasks = {
            new ActivateBloodEssenceTask(),
            new ChipDenseRunestoneTask(),
            new NavigateToDenseEssenceTask(),
            new NavigateToDarkAltarTask(),
            new NavigateToBloodAltarTask()
    };

    @Override
    public void onStart() {
        Paint paint = new PaintBuilder()
                .trackInventoryItem(Constants.BLOOD_ESSENCE)
                .trackInventoryItem(Constants.DENSE_ESSENCE_BLOCK, null, TrackInventoryOption.QuantityChangeIncOny)
                .trackInventoryItem(Constants.BLOOD_RUNE)
                .build();
        addPaint(paint);
    }

    @Override
    public void poll() {

        if (ScriptManager.INSTANCE.isStopping()) {
            getLog().info("Shutting down...");
            return;
        }

        for (Task task : tasks) {
            if (task.activate()) {
                task.execute(this);
                break;
            }
        }

    }

    @Subscribe
    public void onRender(RenderEvent r){

        if (!Game.loggedIn() || ScriptManager.INSTANCE.isStopping()) {
            return;
        }

    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaBloods", "", "127.0.0.1:5855", true, false);
    }
}
