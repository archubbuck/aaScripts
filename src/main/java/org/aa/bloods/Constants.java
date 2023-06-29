package org.aa.bloods;

import org.powbot.api.Area;
import org.powbot.api.Tile;

public class Constants {
    public static final int BLOOD_RUNE = 565;
    public static final int BLOOD_ESSENCE = 26390;
    public static final int BLOOD_ESSENCE_ACTIVE = 26392;
    public static final int DARK_ESSENCE_FRAGMENTS = 7938;
    public static final int DARK_ESSENCE_BLOCK = 13446;
    public static final int DENSE_ESSENCE_BLOCK = 13445;
    public static final int CHISEL = 1755;
    public static final int BLOOD_ALTAR_ID = 27978;
    public static final int DARK_ALTAR_ID = 27979;
    public static final int ROCK_ID = 34741;
    public static final Area DENSE_ESSENCE_AREA = new Area(
            new Tile(1767, 3875, 0),
            new Tile(1751, 3869, 0),
            new Tile(1747, 3859, 0),
            new Tile(1747, 3849, 0),
            new Tile(1761, 3838, 0),
            new Tile(1774, 3838, 0),
            new Tile(1773, 3863, 0)
    );
    public static final Area BLOOD_RUNE_ALTAR_AREA = new Area(
            new Tile(1709, 3832, 0),
            new Tile(1712, 3838, 0),
            new Tile(1729, 3835, 0),
            new Tile(1740, 3834, 0),
            new Tile(1740, 3825, 0),
            new Tile(1719, 3821, 0),
            new Tile(1710, 3824, 0));
    public static final Tile BLOOD_RUNE_ALTAR_TILE = new Tile(1720, 3829, 0);
    public static final Area DARK_ALTAR_AREA = new Area(new Tile(1744, 3862, 0), new Tile(1702, 3897, 0));
    public static final Tile DARK_ALTAR_TILE = new Tile(1719, 3883, 0);
    public static final Tile SHORTCUT_ROCK_TILE_S = new Tile(1761, 3872, 0);
    public static final Tile SHORTCUT_ROCK_TILE_N = new Tile(1761, 3874, 0);
    public static final Tile SHORTCUT_ROCK_TILE_W = new Tile(1742, 3854, 0);
    public static final int SHORTCUT_ROCK_W_ID = 27984;
}
