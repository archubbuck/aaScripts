package org.aa.sorc;

import org.powbot.api.Random;

public class Configuration {
    private static final int SORC_GARDEN_WORLD = 523;
    private static final int STAMINAS_REQUIRED = 2;
    private static final int RUN_ENERGY_MIN = 35;
    private static final int RUN_ENERGY_MAX = 55;

    public static int getSorcGardenWorld() {
        return SORC_GARDEN_WORLD;
    }

    public static int getStaminasRequired() {
        return STAMINAS_REQUIRED;
    }

    public static int getRunEnergyMin() {
        return RUN_ENERGY_MIN;
    }

    public static int getRunEnergyMax() {
        return RUN_ENERGY_MAX;
    }

    public static int getRandomRunEnergy() {
        return Random.nextInt(RUN_ENERGY_MIN, RUN_ENERGY_MAX);
    }
}
