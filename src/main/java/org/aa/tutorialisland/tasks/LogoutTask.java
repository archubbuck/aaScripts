package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.Preferences;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Varpbits;
import org.powbot.mobile.script.ScriptManager;

public class LogoutTask extends AbstractTask {

    @Override
    public boolean activate() {
        return Varpbits.varpbit(281) == 1000 && Game.loggedIn();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Logging out");
        Preferences.setAutoLoginEnabled(false);
        if (Game.logout() && Condition.wait(() -> !Game.loggedIn(), 150, 30)) {
            ScriptManager.INSTANCE.stop();
            Preferences.setAutoLoginEnabled(true);
        }
    }

}
