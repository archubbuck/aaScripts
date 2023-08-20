package org.aa.bloodyhell;

import org.powbot.api.script.AbstractScript;

public interface Task {
    boolean activate();
    boolean execute(AbstractScript abstractScript);
}
