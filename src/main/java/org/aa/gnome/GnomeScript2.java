package org.aa.gnome;

import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.aa.Task;
import org.aa.gnome.tasks.DepositItemsTask;
import org.aa.gnome.tasks.OpenBankTask;
import org.aa.gnome.tasks.SetCameraTask;
import org.aa.gnome.tasks.WithdrawItemsTask;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.mobile.service.ScriptUploader;

@ScriptManifest(
        name = "aaGnomes",
        description = "Gnomes",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.MoneyMaking)
public class GnomeScript2 extends AbstractScript {

    private final Task[] tasks = {
            new SetCameraTask(),
            new OpenBankTask(),
            new DepositItemsTask(),
            new WithdrawItemsTask("Lemon", 2102, 20)
    };

    @Override
    public void poll() {
        for (Task task : tasks) {
            if (task.activate()) {
                task.execute(this);
                break;
            }
        }
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaGnomes", "", "127.0.0.1:" + System.getProperty("port"), true, true);
    }
}
