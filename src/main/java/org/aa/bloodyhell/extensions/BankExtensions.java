package org.aa.bloodyhell.extensions;

import org.jetbrains.annotations.NotNull;
import org.powbot.api.rt4.Bank;

public class BankExtensions {

    public static boolean contains(@NotNull int... ints) {
        return Bank.stream().id(ints).isNotEmpty();
    }

    public static boolean doesNotContain(@NotNull int... ints) {
        return !contains(ints);
    }

}
