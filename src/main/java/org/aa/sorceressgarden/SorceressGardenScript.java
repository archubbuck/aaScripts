package org.aa.sorceressgarden;

import com.google.common.eventbus.Subscribe;
import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.powbot.api.*;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.event.TickEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.drawing.Rendering;

@ScriptManifest(
        name = "aaSorceressGarden",
        description = "Completes the Sorceress's Garden minigame",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.3",
        category = ScriptCategory.Thieving)
public class SorceressGardenScript extends AbstractScript {

    private CollisionDetector collisionDetector;

    private final Area LOBBY_AREA = new Area(
        new Tile(2903, 5480),
        new Tile(2920, 5463)
    );

    private final Tile STARTING_TILE = new Tile(2910, 5481, 0);
    private final Tile GATE_TILE = new Tile(2910, 5480, 0);

    public SorceressGardenScript() {
        super();
    }

    private int TICK;

    @Override
    public void onStart() {

        Paint paint = new PaintBuilder()
                .addString("Status:", super::getStatus)
                .addString("Tick:", () -> String.valueOf(TICK))
                .addString("Ticks to Start:", () -> String.valueOf(collisionDetector.ticksUntilStart))
                .build();

        addPaint(paint);

        collisionDetector = new CollisionDetector();
    }

//    GameObject gate;

    @Subscribe
    public void onTick(TickEvent tickEvent) {
        // handle whatever
        TICK++;
    }

    @Override
    public void poll() {

        //#######        #######//
        //####### MOVING #######//
        //#######        #######//

        if (Players.local().animation() != -1 || Players.local().inMotion()) {
            setStatus("Moving");
            return;
        }

        //#######       #######//
        //####### LOBBY #######//
        //#######       #######//

        if (LOBBY_AREA.contains(Players.local())) {
            setStatus("[LOBBY]-[IDLE]");

            GameObject gate = Objects.stream(GATE_TILE, 2, GameObject.Type.BOUNDARY).name("Gate").nearest().first();
            if (gate.click("Open", true)) {
                setStatus("Opening the gate");
                if (!Condition.wait(() -> Players.local().tile().equals(STARTING_TILE), 150, 15)) {
                    setStatus("Failed to confirm opening gate");
                }
            } else {
                setStatus("Failed to open the gate");
            }

            return;
        }

        //#######       #######//
        //####### START #######//
        //#######       #######//

        if (Players.local().tile().equals(STARTING_TILE)) {
            setStatus("[STARTING_TILE]-[IDLE]");

            Npcs.stream().id(1801, 1802, 1803, 1804, 1805, 1806)
                    .forEach(npc -> collisionDetector.updatePosition(npc, TICK));

            collisionDetector.updateCountdownTimer(TICK);

            if (collisionDetector.ticksUntilStart == 0) {

                var tree = Objects.stream().id(12943).nearest().first();
                if (tree.click() && Condition.wait(() -> !Movement.running(), 150, 60)) {
                    setStatus("clicked");
                    return;
                }

                return;
            }

            return;
        }

        //#######       #######//
        //####### ????? #######//
        //#######       #######//

        setStatus("[???]");
    }

    @Subscribe
    public void onRender(RenderEvent r){

        if (!Game.loggedIn()) {
            return;
        }

//        for (Polygon triangle : java.util.Objects.requireNonNull(gate.boundingModel()).triangles()) {
//            Rendering.drawPolygon(triangle);
//        }

//        gates.forEach(gameObject -> {
//            for (Polygon triangle : java.util.Objects.requireNonNull(gameObject.boundingModel()).triangles()) {
//                Rendering.drawPolygon(triangle);
//            }
//        });

    }

    public static void main(String[] args) {
        new SorceressGardenScript().startScript();
    }

    public static void highlightTile(Locatable locatable) {
        Rectangle rectangle = locatable.tile().matrix().bounds().getBounds();
        Rendering.setColor(Color.getGREEN());
        Rendering.drawRect(rectangle);
        Rendering.setColor(Color.argb(60, 255, 99, 71));
        Rendering.fillRect(rectangle);
    }
}
