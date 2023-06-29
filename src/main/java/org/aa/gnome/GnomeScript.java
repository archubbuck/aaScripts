package org.aa.gnome;

import com.google.common.eventbus.Subscribe;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.aa.Task;
import org.aa.gnome.tasks.cook.Cook;
import org.aa.gnome.tasks.shop.heckelfunch.Shop;
import org.jetbrains.annotations.NotNull;
import org.powbot.api.*;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.script.OptionType;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptConfiguration;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.drawing.Rendering;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

import java.util.Arrays;

@ScriptManifest(
        name = "aaGnomes",
        description = "Gnomes",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.MoneyMaking)
@ScriptConfiguration.List({
        @ScriptConfiguration(
                name = "Activity",
                description = "The activity that you would like to do",
                optionType = OptionType.STRING,
                defaultValue = "Buy Shit"
        )
})
public class GnomeScript extends AbstractScript {

    private final Area shopArea = new Area(
            new Tile(2489, 3490, 1),
            new Tile(2494, 3487, 1)
    );

    private Task[] tasks;

    @Override
    public void showOptionsDialog(@NotNull Function0<Unit> function0) {
        super.showOptionsDialog(function0);

        updateAllowedOptions("Activity", Arrays.stream(Activity.values())
                .map(Activity::getName)
                .toArray(String[]::new));
    }

    @Override
    public void onStart() {

        String activityName = getOption("Activity");

        PaintBuilder paintBuilder = new PaintBuilder();

        if (activityName.equals(Activity.SHOP.getName())) {
            tasks = Shop.TASKS;
            paintBuilder = Shop.PAINT;
        }

        if (activityName.equals(Activity.COOK.getName())) {
            tasks = Cook.TASKS;
            paintBuilder = Cook.PAINT;
        }

        if (tasks.length < 1) {
            ScriptManager.INSTANCE.stop();
        }

        addPaint(paintBuilder.build());
    }

    @Override
    public void poll() {
        for (Task task : tasks) {
            if (task.activate()) {
                task.execute(this);
                break;
            }
        }
    }

    @Subscribe
    public void onRender(RenderEvent r){
        if (!Game.loggedIn()) {
            return;
        }
        for (Tile tile : shopArea.getTiles()) {
            Rendering.setColor(Color.getGREEN());
            Rendering.drawString("Y", tile.matrix().centerPoint().getX(), tile.matrix().centerPoint().getY());
        }
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaGnomes", "", "127.0.0.1:" + System.getProperty("port"), true, true);
    }
}
