package org.aa.spamclicker;

import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.stream.item.InventoryItemStream;
import org.powbot.api.rt4.stream.locatable.interactive.GameObjectStream;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.service.ScriptUploader;

import java.util.Arrays;

@ScriptManifest(
        name = "aaSpamClicker",
        description = "Spam clicks an object",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Other)
public class SpamClickerScript extends AbstractScript {

    private final int DOOR_KEY_ID = 2409;
    private final int NEEDLE_ID = 1733;
    private final int THREAD_ID = 1734;

    private final int[] itemsToKeep = {
            DOOR_KEY_ID,
            NEEDLE_ID,
            THREAD_ID
    };

    @Override
    public void onStart() {
        Paint paint = new PaintBuilder()
                .trackInventoryItems(itemsToKeep)
                .build();

        addPaint(paint);
    }

    @Override
    public void poll() {
        InventoryItemStream itemsToDrop = getItemsToDrop();

        if (itemsToDrop.isNotEmpty()) {
            getLog().info("Dropping items");
            Inventory.drop(itemsToDrop.list());
//            boolean droppedItems = Condition.wait(() -> getItemsToDrop().isEmpty(), 150, 15);
            Condition.sleep(Random.nextInt(167, 411));
//            getLog().info(droppedItems ? "Dropped items" : "Failed to drop items in time");
            getLog().info("Dropped items");
            return;
        }

        GameObjectStream targetObjects = Objects.stream().name("Boxes").action("Search").within(5);

        if (targetObjects.isNotEmpty()) {
            getLog().info("Identified possible targets");
            GameObject targetObject = targetObjects.nearest().first();
            getLog().info("Identified target");
            boolean clicked = targetObject.click();
            getLog().info(clicked ? "Successfully clicked" : "Failed to click");
            Condition.sleep(Random.nextInt(167, 411));
            return;
        }
    }

    private InventoryItemStream getItemsToDrop() {
        return Inventory.stream().filter(item -> Arrays.stream(itemsToKeep).noneMatch(itemId -> itemId == item.id()));
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaSpamClicker", "", "127.0.0.1:5835", true, true);
    }
}
