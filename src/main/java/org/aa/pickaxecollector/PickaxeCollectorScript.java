package org.aa.pickaxecollector;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.ScriptManifestDefaults;
import org.aa.pickaxecollector.tasks.*;
import org.powbot.api.rt4.*;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.script.ScriptManager;

@ScriptManifest(
        name = "aaPickaxeCollector",
        description = "Collects pickaxes from the Ruins of Camdozaal",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.MoneyMaking)
public class PickaxeCollectorScript extends AbstractScript {

    public PickaxeCollectorScript() {
        super(
                new DepositToBankTask(),
                new OpenBankTask(),
                new CloseBankTask(),
                new EnableSingleTapDropTask(),
                new DropTask(),
                new PickupTask(),
                new TakeTask()
        );
    }

    @Override
    public void onStart() {
        Paint paint = new PaintBuilder()
                .addString("Status:", super::getStatus)
                .addString("Mouse Mode: ", () -> Game.loggedIn() ? Game.getMouseToggle().name() : "UNKNOWN")
                .addString("Single Tap Dropping: ", () -> Inventory.shiftDroppingEnabled() ? "Enabled" : "Disabled")
                .addString("Camera Pitch", () -> String.valueOf(Camera.pitch()))
                .addString("Camera Zoom", () -> String.valueOf(Camera.getZoom()))
                .trackInventoryItem(Constants.BRONZE_PICKAXE_ID, "Bronze Pickaxe")
                .build();
        addPaint(paint);
    }

    @Override
    public void poll() {

        if (Quests.INSTANCE.getCompletedQuests().stream().noneMatch(quest -> quest.equals("Below Ice Mountain"))) {
            this.setStatus("QUEST REQUIRED - Below Ice Mountain");
            ScriptManager.INSTANCE.pause();
            return;
        }

        for (AbstractTask task : super.tasks) {
            String taskName = task.getClass().getSimpleName();
            if (task.activate()) {
                getLog().info("[ACTIVATE] " + taskName);
                task.execute(this);
                break;
            }
        }

    }
}
