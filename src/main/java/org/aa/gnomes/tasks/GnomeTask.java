package org.aa.gnomes.tasks;

public interface GnomeTask {
    String getName();
    boolean canExecute();
    void execute();
}
