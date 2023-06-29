package org.aa.truebloods;

import org.powbot.api.script.AbstractScript;

public interface Task {
    boolean activate();
    void execute(AbstractScript abstractScript);
}
