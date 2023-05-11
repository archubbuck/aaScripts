package org.aa;

public abstract class AbstractScript extends org.powbot.api.script.AbstractScript {

    private String status;

    public final AbstractTask[] tasks;

    public AbstractScript(AbstractTask... tasks) {
        this.status = "Initializing";
        this.tasks = tasks;
    }

    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        return this.status = status;
    }

    @Override
    public void onStop() {
        getLog().info("Script stopped");
    }
}
