package org.aa;

public abstract class AbstractTask {
    public boolean activate() {
        return false;
    }
    public void execute(AbstractScript abstractScript) {}
}
