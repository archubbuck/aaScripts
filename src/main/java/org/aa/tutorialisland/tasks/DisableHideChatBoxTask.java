package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.mobile.SettingsManager;
import org.powbot.mobile.ToggleId;

public class DisableHideChatBoxTask extends AbstractTask {
    @Override
    public boolean activate() {
        return SettingsManager.enabled(ToggleId.HideChatBox);
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Disabling " + ToggleId.HideChatBox);
        SettingsManager.set(ToggleId.HideChatBox, false);
    }
}
