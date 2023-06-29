package org.aa.gnome;

public enum Activity {

    SHOP("Buy Shit"),
    COOK("Cook Shit");

    private final String name;

    Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
