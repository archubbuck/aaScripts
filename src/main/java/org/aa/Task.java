package org.aa;

import org.aa.AbstractScript;

public interface Task {
    boolean activate();
    void execute(AbstractScript abstractScript);
}
