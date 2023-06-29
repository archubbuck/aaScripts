package org.aa.gnome.tasks.cook;

import org.aa.Task;
import org.aa.gnome.tasks.SetCameraTask;
import org.aa.gnome.tasks.cook.cocktails.FruitBlast;
import org.powbot.api.script.paint.PaintBuilder;

public class Cook {

    public final static Task[] TASKS = {
            new SetCameraTask(),
            new FruitBlast()
    };

    public final static PaintBuilder PAINT = new PaintBuilder()
            .withTotalLoot(true);

}
