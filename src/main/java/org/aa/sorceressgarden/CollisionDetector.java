package org.aa.sorceressgarden;

import org.powbot.api.Tile;
import org.powbot.api.rt4.Npc;

import java.util.Arrays;

public class CollisionDetector {

    private static final Tile[] HOMES = {
            new Tile(2907, 5488, 0),
            new Tile(2907, 5490, 0),
            new Tile(2910, 5487, 0),
            new Tile(2912, 5485, 0),
            new Tile(2921, 5486, 0),
            new Tile(2921, 5495, 0),
    };

    private static final int[] CYCLE_LENGTHS = {10, 10, 12, 20, 12, 12};

    private final int[] tickBasis = {-1, -1, -1, -1, -1, -1};

    public int ticksUntilStart = -1;

    static final boolean[][] VALID_TICKS_GATE_START = {
            {false, false, false, false, false, false, true, false, false, false},
            {true, true, true, false, false, false, false, true, false, false},
            {false, false, true, true, true, true, false, false, false, false, true, true},
            {true, true, true, false, false, false, true, true, true, true, true, true, false, false, false, false, false, true, true, true},
            {true, false, true, false, false, false, true, false, false, false, false, true},
            {false, false, false, true, false, false, true, true, true, true, true, false}
    };

    /**
     * The second index is the number of ticks since it was last at its home spot.
     * The value in the array is whether you can run past that elemental without getting caught.
     */
    private boolean[] getValidTicksForElemental(int elementalIndex)
    {
        return VALID_TICKS_GATE_START[elementalIndex];
    }

    public void updatePosition(Npc npc, int tc)
    {
        int eId = npc.getId() - 1801;
        if (npc.tile().equals(HOMES[eId]))
        {
            tickBasis[eId] = tc;
        }
    }

    private int getBestStartPointForLowestTotalParityScore()
    {
        int smallestParity = Integer.MAX_VALUE;
        int smallestParityIndex = -1;
        for (int i = 0; i < 60; i++)
        {
            int paritySum = getParitySum(i);
            if (paritySum < smallestParity)
            {
                smallestParity = paritySum;
                smallestParityIndex = i;
            }
        }
        if (smallestParityIndex == -1)
        {
            throw new IllegalStateException("Every elemental should be passable on at least one tick.");
        }
        return smallestParityIndex;
    }

    private int getParitySum(int startCycle)
    {
        int paritySum = 0;
        for (int elementalIndex = 0; elementalIndex < 6; elementalIndex++)
        {
            paritySum += getParityForStartCycle(startCycle, elementalIndex);
        }
        return paritySum;
    }

    private int getParityForStartCycle(int startCycle, int elementalIndex)
    {
        if (tickBasis[elementalIndex] == -1)
        {
            return -1;
        }

        int basis = tickBasis[elementalIndex];

        boolean[] validTicksForElemental = getValidTicksForElemental(elementalIndex);
        for (int parity = 0; parity < validTicksForElemental.length; parity++)
        {
            int cycleLength = CYCLE_LENGTHS[elementalIndex];
            int indexWithParity = moduloPositive(startCycle - basis - parity, cycleLength);
            if (validTicksForElemental[indexWithParity])
            {
                return parity;
            }
        }
        throw new IllegalStateException("Every elemental should be passable on at least one tick.");
    }

    public int getParity(int elementalId)
    {
        boolean seenAllElementalBasis = !Arrays.stream(tickBasis).filter(i -> i == -1).findAny().isPresent();
        return !seenAllElementalBasis ? -1 : getParityForStartCycle(getBestStartPointForLowestTotalParityScore(), elementalId - 1801);
    }

    public boolean isLaunchCycle()
    {
        return ticksUntilStart < 8;
    }

    private static int moduloPositive(int base, int mod)
    {
        return ((base % mod) + mod) % mod;
    }

    public void updateCountdownTimer(int tc)
    {
        // This is less than 60 * 6 * 20 = 7200 operations, shouldn't lag anyone to run it every game tick.
        ticksUntilStart = moduloPositive(getBestStartPointForLowestTotalParityScore() - tc, 60);
    }

}
