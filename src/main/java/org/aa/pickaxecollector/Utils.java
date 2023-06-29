package org.aa.pickaxecollector;

import org.powbot.api.Area;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Movement;
import org.powbot.mobile.drawing.Rendering;

public class Utils {

    public static void drawArea(Area area, int color, boolean showOnlyWalkableTiles) {
        for (Tile tile : area.getTiles()) {
            if (showOnlyWalkableTiles && tile.blocked(Movement.collisionMap(tile.getFloor()).flags(), 0)) {
                return;
            }
            drawTile(tile, color);
        }
    }

    public static void drawArea(Area area, int color) {
        for (Tile tile : area.getTiles()) {
            drawTile(tile, color);
        }
    }

    public static void drawTile(Tile tile, int color) {
        Rendering.setColor(color);
        Rendering.drawPolygon(tile.matrix().bounds());
    }

}
