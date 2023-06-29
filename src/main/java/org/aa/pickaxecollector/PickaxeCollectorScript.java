package org.aa.pickaxecollector;

import com.google.common.eventbus.Subscribe;
import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.aa.ScriptManifestDefaults;
import org.aa.pickaxecollector.tasks.*;
import org.powbot.api.Color;
import org.powbot.api.Rectangle;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.drawing.Rendering;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

@ScriptManifest(
        name = "aaPickaxeCollector",
        description = "Collects pickaxes from the Ruins of Camdozaal",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.3",
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

        this.setStatus("Checking quest requirement");
        if (Quests.INSTANCE.getCompletedQuests().stream().noneMatch(quest -> quest.equals("Below Ice Mountain"))) {
            this.setStatus("QUEST REQUIRED - Below Ice Mountain");
            ScriptManager.INSTANCE.pause();
        }

    }

    GameObject barrel;
    GameObject bankChest;

    @Override
    public void poll() {
        barrel = Objects.stream().name("Barrel").action("Take pickaxe").nearest().first();
        bankChest = Objects.stream().name("Bank chest").action("Use").nearest().first();

        for (AbstractTask task : super.tasks) {
            String taskName = task.getClass().getSimpleName();
            if (task.activate()) {
                getLog().info("[ACTIVATE] " + taskName);
                task.execute(this);
                break;
            }
        }
    }

    @Subscribe
    public void onRender(RenderEvent r){

        if (!Game.loggedIn() || Bank.opened()) {
            return;
        }

        for(GameObject gameObject : new GameObject[]{ barrel, bankChest }) {
            Rectangle rectangle = gameObject.tile().matrix().bounds().getBounds();
            Rendering.setColor(Color.getGREEN());
            Rendering.drawRect(rectangle);
            Rendering.setColor(Color.argb(60, 255, 99, 71));
            Rendering.fillRect(rectangle);
        }
    }

    public static void main(String[] args) {
//        new PickaxeCollectorScript().startScript();
        new ScriptUploader().uploadAndStart("aaPickaxeCollector", "", "127.0.0.1:5845", true, true);
    }
}
