package org.aa.tutorialisland;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.ScriptManifestDefaults;
import org.aa.tutorialisland.tasks.*;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;

@ScriptManifest(
        name = "aaTutorialIsland",
        description = "Completes Tutorial Island",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Other)
public class TutorialIslandScript extends AbstractScript {

    public TutorialIslandScript() {
        super(
            new LogoutTask(),
            new DisableHideChatBoxTask(),
            new ContinueChatTask(),
            new ZoomOutTask(),
            new EnableRunningTask(),
            new WaitForDisplayNameTask(),
            new SetOutfitTask()
        );
    }

    @Override
    public void onStart() {
        Paint paint = new PaintBuilder()
                .addString("Status:", super::getStatus)
                .build();

        addPaint(paint);
    }

    @Override
    public void poll() {
        for (AbstractTask task : super.tasks) {
            String taskName = task.getClass().getSimpleName();
            if (task.activate()) {
                getLog().info("[ACTIVATE] " + taskName);
                task.execute(this);
                break;
            }
        }
    }

    @Override
    public void onStop() {
        getLog().info("Script stopped");
    }
}
