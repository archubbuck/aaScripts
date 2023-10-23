package org.core;

import org.powbot.api.Tile;

public enum Banks {
    AL_KHARID(new Tile(3269, 3166, 0)),
    ARDOUGNE_NORTH(new Tile(2616, 3332, 0)),
    ARDOUGNE_SOUTH(new Tile(2653, 3283, 0)),
    CAMELOT(new Tile(2725, 2491, 0)),
    DRAYNOR(new Tile(3093, 3243, 0)),
    EDGEVILLE(new Tile(3094, 3493, 0)),
    GRAND_EXCHANGE(new Tile(3165, 3487, 0)),
    VARROCK_EAST(new Tile(3254, 3420, 0)),
    VARROCK_WEST(new Tile(3184, 3440, 0));
    public final Tile tile;
    public Tile getTile() {
        return tile;
    }
    Banks(Tile tile) {
        this.tile = tile;
    }
}
