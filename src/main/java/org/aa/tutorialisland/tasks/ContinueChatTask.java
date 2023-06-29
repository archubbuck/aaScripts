package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Chat;

public class ContinueChatTask extends AbstractTask {
    @Override
    public boolean activate() {
        return Chat.canContinue();
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        abstractScript.setStatus("Continuing the chat");
        if (Chat.continueChat()) {
            Condition.wait(() -> !Chat.canContinue(), 150, 10);
        }
    }
}
