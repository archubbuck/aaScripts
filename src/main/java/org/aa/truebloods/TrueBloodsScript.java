
package org.aa.truebloods;

import com.google.common.eventbus.Subscribe;
import org.aa.ScriptManifestDefaults;
import org.aa.bloods.Constants;
import org.aa.truebloods.constants.Areas;
import org.aa.truebloods.constants.Items;
import org.aa.truebloods.constants.Tiles;
import org.aa.truebloods.extensions.InventoryExtensions;
import org.aa.truebloods.helpers.Pouch;
import org.aa.truebloods.helpers.PouchTracker;
import org.aa.truebloods.helpers.SystemMessageManager;
import org.aa.truebloods.tasks.ActivateBloodEssence;
import org.aa.truebloods.tasks.EmptyColossalPouch;
import org.aa.truebloods.tasks.MakeBloodRunes;
import org.aa.truebloods.tasks.SetCameraTask;
import org.aa.truebloods.tasks.banking.*;
import org.aa.truebloods.tasks.navigation.*;
import org.powbot.api.event.*;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.Varpbits;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.service.ScriptUploader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.aa.truebloods.helpers.Constants.*;

@ScriptManifest(
        name = "aaTrueBloods",
        description = "Crafts blood runes",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Runecrafting)
public class TrueBloodsScript extends AbstractScript {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

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
                .addString("ptt", () -> PouchTracker.INSTANCE.getPouchesToTrack().toString())
                .addString("ptf", () -> String.valueOf(PouchTracker.INSTANCE.hasPouchToFill()))
                .build();
        addPaint(paint);

//        PouchTracker.INSTANCE.setSupportedPouches(new ArrayList<>() {{
//            new Pouch(ITEM_SMALL_POUCH, 0, false);
//            new Pouch(ITEM_MEDIUM_POUCH, 1, false);
//            new Pouch(ITEM_LARGE_POUCH, 2, false);
//            new Pouch(ITEM_GIANT_POUCH, 3, false);
//            new Pouch(ITEM_COLOSSAL_POUCH, 4, true);
//        }});


        PouchTracker.INSTANCE.updatePouchesToTrack();
    }

    @Override
    public void poll() {
        for (Task task : tasks) {
            if (task.activate()) {
                logger.info(task.getClass().getName());
                task.execute(this);
                break;
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

//        if (messageEvent.getMessageType() != MessageType.Game) {
//            return;
//        }
//
//        String messageText = messageEvent.getMessage();
//
//        logger.info("[" + messageEvent.getMessageType() + "] " + messageText);
//
//        if (messageText.equals("You cannot add any more essence to the pouch.")) {
////            State.pouchIsFull = true;
//            State.essenceInPouch = 35;
//        }
//
//        if (messageText.equals("There are thirty-five pure essences in this pouch.")) {
////            State.pouchIsFull = true;
//            State.essenceInPouch = 35;
//        }
//
////        if (messageText.equals("The pouch contains pure essence, so you can fill it only with more pure essence.")) {
////            State.pouchIsFull = false;
////        }
//
//        if (messageText.equals("There is no essence in this pouch.")) {
////            State.pouchIsFull = false;
//            State.essenceInPouch = 0;
//        }
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
        new ScriptUploader().uploadAndStart("aaTrueBloods", "", "127.0.0.1:5865", true, false);
    }
}
