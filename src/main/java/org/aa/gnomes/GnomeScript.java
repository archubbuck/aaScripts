package org.aa.gnomes;

import org.aa.gnomes.tasks.GnomeTask;
import org.aa.gnomes.tasks.production.cocktails.MakeBlurberrySpecial;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.dax.api.DaxWalker;
import org.powbot.dax.teleports.Teleport;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

/*
 * Script Manifest
 */

@ScriptManifest(
        name = "Gnomes",
        description = "Completes the gnome restaurant minigame",
        author = "Farmer",
        version = "0.0.1",
        category = ScriptCategory.Minigame)

/*
 * Script Configuration
 */

public class GnomeScript extends AbstractScript {
    Logger logger = Logger.getLogger(this.getClass().getName());

    ArrayList<GnomeTask> tasks = new ArrayList<>();

    @Override
    public void onStart() {
        DaxWalker.blacklistTeleports(Teleport.values());
        tasks.add(new MakeBlurberrySpecial());
    }

    @Override
    public void poll() {
        for (GnomeTask task : tasks) {
            String taskName = task.getName();
            logger.info("Checking task: " + taskName);
            if (task.canExecute()) {
                logger.info("Executing task: " + taskName);
                task.execute();
            }
            if (ScriptManager.INSTANCE.isPaused() || ScriptManager.INSTANCE.isStopping()) {
                logger.info("The script is paused or stopping; skipping tasks...");
                break;
            }
        }
    }

    public static void main(String[] args) {
        String port = System.getProperty("port");

        if (port == null) {
            throw new IllegalArgumentException("The port number is undefined.");
        }

        try (ScriptUploader scriptUploader = new ScriptUploader()) {
            scriptUploader.uploadAndStart("Gnomes", "", "127.0.0.1:" + port, true, true);
        } catch (Exception ignored) { }
    }
}
