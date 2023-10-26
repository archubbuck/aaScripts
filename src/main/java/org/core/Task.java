package org.core;

import org.powbot.api.script.AbstractScript;

public abstract class Task {
    public abstract boolean activate();
    public abstract void execute(AbstractScript abstractScript);
}

