package org.aa.truebloods;

import org.jetbrains.annotations.NotNull;

public class Utils {

    public static boolean all(@NotNull boolean... conditions) {
        for(boolean condition : conditions) if(!condition) return false;
        return true;
    }

    public static boolean any(@NotNull boolean... conditions) {
        for(boolean condition : conditions) if(!condition) return true;
        return false;
    }

}
