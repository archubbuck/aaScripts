package org.aa.bloodyhell;

import com.google.common.eventbus.Subscribe;
import org.aa.ScriptManifestDefaults;
import org.aa.bloods.Constants;
import org.aa.bloodyhell.tasks.*;
import org.aa.bloodyhell.tasks.banking.*;
import org.aa.bloodyhell.tasks.equipment.EquipGlory;
import org.aa.bloodyhell.tasks.equipment.EquipSkillCape;
import org.aa.bloodyhell.tasks.equipment.EquipStaff;
import org.aa.bloodyhell.tasks.navigation.GoToAltar;
import org.aa.bloodyhell.tasks.navigation.GoToBank;
import org.aa.truebloods.helpers.PouchTracker;
import org.aa.truebloods.helpers.SystemMessageManager;
import org.powbot.api.event.GameActionEvent;
import org.powbot.api.event.InventoryChangeEvent;
import org.powbot.api.event.MessageEvent;
import org.powbot.api.event.VarpbitChangedEvent;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.dax.api.DaxConfigs;
import org.powbot.dax.api.DaxWalker;
import org.powbot.mobile.service.ScriptUploader;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@ScriptManifest(
        name = "aaBloodyHell",
        description = "Crafts blood runes",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Runecrafting)
public class BloodyHellScript extends AbstractScript {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private static final Task[] tasks = {
            new SetCamera(),
            new EquipSkillCape(),
            new EquipStaff(),
            new EquipGlory(),
            new DrinkStamina(),
            new GoToBank(),
            new DepositJunk(),
            new WithdrawSkillCape(),
            new WithdrawStaff(),
            new WithdrawGlory(),
            new WithdrawPouch(),
            new ActivateBloodEssence(),
            new WithdrawBloodEssenceActive(),
            new WithdrawBloodEssence(),
            new WithdrawStamina(),
            new WithdrawPureEssence(),
            new FillPouch(),
            new EmptyPouch(),
            new CraftRunes(),
            new GoToAltar()
    };

    @Override
    public void onStart() {
        Paint paint = new PaintBuilder()
                .trackInventoryItem(Constants.BLOOD_RUNE)
                .build();
        addPaint(paint);
        PouchTracker.INSTANCE.setPouchesToTrack(
                PouchTracker.INSTANCE.getSupportedPouches().stream()
                        .filter(pouch -> pouch.getItemName().equalsIgnoreCase("Colossal Pouch"))
                        .collect(Collectors.toList())
        );
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
        new ScriptUploader().uploadAndStart("aaBloodyHell", "", "127.0.0.1:" + System.getProperty("port"), true, true);
    }
}
