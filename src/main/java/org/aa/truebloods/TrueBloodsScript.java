
package org.aa.truebloods;

import com.google.common.eventbus.Subscribe;
import org.aa.ScriptManifestDefaults;
import org.aa.bloods.Constants;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.extensions.EquipmentExtensions;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.PouchTracker;
import org.aa.truebloods.helpers.SystemMessageManager;
import org.aa.truebloods.tasks.ActivateBloodEssence;
import org.aa.truebloods.tasks.EmptyColossalPouch;
import org.aa.truebloods.tasks.MakeBloodRunes;
import org.aa.truebloods.tasks.SetCameraTask;
import org.aa.truebloods.tasks.banking.*;
import org.aa.truebloods.tasks.navigation.*;
import org.powbot.api.Notifications;
import org.powbot.api.event.GameActionEvent;
import org.powbot.api.event.InventoryChangeEvent;
import org.powbot.api.event.MessageEvent;
import org.powbot.api.event.VarpbitChangedEvent;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

import java.util.logging.Logger;

@ScriptManifest(
        name = "aaTrueBloods",
        description = "Crafts blood runes",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Runecrafting)
public class TrueBloodsScript extends AbstractScript {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    Validator validator;

    private static final Task[] tasks = {
            new SetCameraTask(),
            new ActivateBloodEssence(),
            new EmptyColossalPouch(),
            new MakeBloodRunes(),
            new WalkToBank(),
            new OpenBank(),
            new DepositItems(),
            new WithdrawGlory(),
            new EquipGlory(),
            new WithdrawStamina(),
            new DrinkStamina(),
            new WithdrawPureEssence(),
            new FillColossalPouch(),
            new WalkToCave1(),
            new WalkToCave2(),
            new WalkToCave3(),
            new WalkToCave4(),
            new WalkToCave5(),
            new WalkToCave6(),
            new WalkToCave7(),
            new WalkToBloodAltar(),
    };

    @Override
    public void onStart() {

        Paint paint = new PaintBuilder()
                .trackInventoryItem(Constants.BLOOD_RUNE)
                .addString("Can Empty", () -> String.valueOf(PouchTracker.INSTANCE.hasPouchToEmpty()))
                .addString("Can Fill", () -> String.valueOf(PouchTracker.INSTANCE.hasPouchToFill()))
                .build();

        addPaint(paint);

        this.validator = new Validator()
                .require(
                        "You must wear a charged Amulet of Glory",
                        () -> EquipmentExtensions.contains(Items.CHARGED_AMULET_OF_GLORIES)
                )
                .require(
                        "Your inventory must contain a Blood Talisman",
                        () -> InventoryExtensions.contains(Items.BLOOD_TALISMAN)
                );

        if (!this.validator.valid()) {
            ScriptManager.INSTANCE.stop();
            Notifications.showNotification(this.validator.getMessage());
            return;
        }

        PouchTracker.INSTANCE.updatePouchesToTrack();
    }

    @Override
    public void poll() {
        if (this.validator.valid()) {
            for (Task task : tasks) {
                if (task.activate()) {
                    logger.info(task.getClass().getName());
                    task.execute(this);
                    break;
                }
            }
        }
    }

    /**
     *  Subscribes to the messages received from the game and updates the status accordingly
     *
     *  @param messageEvent The message received form the game.
     */
    @Subscribe
    public void onMessage(MessageEvent messageEvent) {
        if (!messageEvent.getSender().isEmpty()) {
            return;
        }
        PouchTracker.INSTANCE.messageEvent(messageEvent);
        SystemMessageManager.INSTANCE.messageRecieved(messageEvent);
    }

    /**
     *  Subscribes to changes in the inventory to update the current status of our pouches
     *
     *  @param inventoryChangeEvent The event received from the game
     */
    @Subscribe
    public void onInventoryChanged(InventoryChangeEvent inventoryChangeEvent) {
        PouchTracker.INSTANCE.inventoryChangedEvent(inventoryChangeEvent);
    }

    /**
     *  Subscribes to game actions which will update the current status of our pouches
     *
     *  @param gameActionEvent The event received in the game
     */
    @Subscribe
    public void onGameActionEvent(GameActionEvent gameActionEvent) {
        PouchTracker.INSTANCE.gameActionEvent(gameActionEvent);
    }

    /**
     *  Subscribes to game actions which will update the current status of our pouches
     *
     *  @param varpbitChangedEvent The event received in the game
     */
    @Subscribe
    public void onVarpbitChanged(VarpbitChangedEvent varpbitChangedEvent) {
        if (varpbitChangedEvent.getIndex() != 261) {
            return;
        }
        PouchTracker.INSTANCE.varpbitChanged(varpbitChangedEvent.getNewValue());
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaTrueBloods", "", "127.0.0.1:" + System.getProperty("port"), true, true);
    }
}
