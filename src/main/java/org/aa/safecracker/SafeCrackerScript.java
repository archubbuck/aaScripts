package org.aa.safecracker;

import com.google.common.eventbus.Subscribe;
import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.event.PlayerAnimationChangedEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.stream.item.InventoryItemStream;
import org.powbot.api.rt4.stream.locatable.interactive.GameObjectStream;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.SettingsManager;
import org.powbot.mobile.ToggleId;
import org.powbot.mobile.service.ScriptUploader;

@ScriptManifest(
        name = "aaSafeCracker",
        description = "Cracks safes",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Thieving)
public class SafeCrackerScript extends AbstractScript {

    private static long lastAnimationTs;

    @Override
    public void onStart() {
        SettingsManager.set(ToggleId.DismissTradeWidget, false);

        Paint paint = new PaintBuilder()
                .trackSkill(Skill.Thieving)
                .trackInventoryItems(1623, 1621, 1619, 1617)
                .build();

        addPaint(paint);
    }

    @Override
    public void poll() {
        int startingHealthPercentage = Players.local().healthPercent();

        if (startingHealthPercentage < Random.nextInt(25, 85)) {
            getLog().info("We need to eat");

            InventoryItemStream jugsOfWine = Inventory.stream().id(1993);
            if (jugsOfWine.isEmpty()) {
                getLog().info("We need more jugs of wine!");
                return;
            }

            Item jugOfWine = jugsOfWine.any();
            getLog().info("Attempting to eat");

            boolean ateFood = jugOfWine.click() && Condition.wait(() -> Players.local().healthPercent() > startingHealthPercentage, 150, 15);
            getLog().info(ateFood ? "We ate food" : "Failed to eat food");

            return;
        }

        if (Inventory.isFull()) {
            getLog().info("Inventory is full!");
            return;
        }

        boolean isIdle = Players.local().animation() == -1;
        boolean isEating = Players.local().animation() == 829;
        boolean isThieving = Players.local().animation() == 2247;

        if (isIdle) {
            getLog().info("Idle");
        }

        if (isEating) {
            getLog().info("Eating");
        }

        if (isThieving) {
            getLog().info("Thieving");
            return;
        }

//        if (getTimeSinceLastAnimation() < 1350) {
//            getLog().info("Waiting");
//            return;
//        }

//        if (Skills.timeSinceExpGained(Skill.Thieving) > getTimeSinceLastAnimation() && getTimeSinceLastAnimation() < 3250) {
//            getLog().info("Waiting");
//            return;
//        }

//        if (Skills.timeSinceExpGained(Skill.Thieving) < 1750 || getTimeSinceLastAnimation() < 3250) {
//            getLog().info("Waiting");
//            return;
//        }

        GameObjectStream wallSafes = Objects.stream().name("Wall safe").action("Crack").within(1);

        if (wallSafes.isEmpty()) {
            getLog().info("Unable to locate wall safe");
            return;
        }

        GameObject wallSafe = wallSafes.nearest().first();
        getLog().info("Attempting to interact with the safe");

        boolean interacted1 = wallSafe.interact("Crack");
        getLog().info("interacted1: " + interacted1);

        boolean interacted2 = Condition.wait(() -> startingHealthPercentage != Players.local().healthPercent() || Skills.timeSinceExpGained(Skill.Thieving) < 1350, 150, 150);
        getLog().info("interacted2: " + interacted2);

        boolean interacted = interacted1 && interacted2;
        getLog().info(interacted ? "Successfully interacted" : "Failed to interact");

    }

    @Subscribe
    public void onPlayerAnimationChanged(PlayerAnimationChangedEvent event) {
        boolean isLocalPlayer = event.getPlayer().name().equals(Players.local().name());
        if (!isLocalPlayer) {
            return;
        }
        getLog().info("Animation changed: " + event.getAnimation());
        lastAnimationTs = System.currentTimeMillis();
    }

    public static long getTimeSinceLastAnimation() {
        return System.currentTimeMillis() - lastAnimationTs;
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaSafeCracker", "", "127.0.0.1:5835", true, true);
    }
}
