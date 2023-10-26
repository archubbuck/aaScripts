package org.aa.bloodyhell.tasks;

import org.aa.bloodyhell.constants.Items;
import org.core.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Equipment;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.stream.item.BankItemStream;
import org.powbot.api.script.AbstractScript;

import java.util.Arrays;
import java.util.List;

public class WithdrawEquipment extends Task {
    private final String amuletOfGlorySubStr = "Amulet of glory(";

    private final String[] equipmentNames = {
            "Dramen staff",
            "Hat of the eye",
            "Robe top of the eye",
            "Robe bottoms of the eye",
            "Boots of the eye",
            "Runecraft cape",
            amuletOfGlorySubStr
    };

    @Override
    public boolean activate() {
        return Equipment.stream().nameContains(equipmentNames).count() < equipmentNames.length;
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        if (!Bank.opened()) {
            boolean opened = Bank.open() && Condition.wait(Bank::opened, 10, 150);
            System.out.println(opened ? "Opened the bank" : "Failed to open the bank");
            if (!opened) {
                return;
            }
        }

        List<Item> itemsEquipped = Equipment.stream().name(equipmentNames).list();

        for (String equipmentName : equipmentNames) {
            String itemName = equipmentName.equals(amuletOfGlorySubStr)
                    ? Bank.stream().id(Items.AMULETS_OF_GLORY).first().name()
                    : equipmentName;

            System.out.println("Checking: " + itemName);

            if (itemsEquipped.stream().anyMatch(item -> item.name().equalsIgnoreCase(itemName))) {
                System.out.println("Skipping: " + itemName);
                continue;
            }

            System.out.println("Withdrawing: " + itemName);
            Bank.withdraw(itemName, 1);
        }
    }
}
