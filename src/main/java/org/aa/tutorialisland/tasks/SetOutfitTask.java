package org.aa.tutorialisland.tasks;

import org.aa.AbstractScript;
import org.aa.AbstractTask;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.Component;
import org.powbot.api.rt4.Components;

import java.util.List;

public class SetOutfitTask extends AbstractTask {

    @Override
    public boolean activate() {
        return Components.stream().anyMatch(c -> c.valid() && c.text().equals("Character Creator"));
    }

    @Override
    public void execute(AbstractScript abstractScript) {
        Component outfitSelectionTitleComponent = Components.stream().filter(c -> c.valid() && c.text().equals("Character Creator")).first();
        Component outfitSelectionComponent = outfitSelectionTitleComponent.parent();

        double roll = Random.nextDouble(0, 100);
        abstractScript.setStatus("Roll: " + roll);

        boolean shouldSaveOutfit = roll <= 6.872;

        if (shouldSaveOutfit) {
            Component confirmButton = Components.stream(outfitSelectionComponent.widgetId()).filter(c -> c.actions().contains("Confirm")).first();
            if (confirmButton.click()) {
                Condition.wait(() -> !outfitSelectionComponent.refresh().visible(), 150, 15);
                return;
            }
            abstractScript.setStatus("Failed to save outfit");
            return;
        }

        List<Component> potentialTargets = Components.stream(outfitSelectionComponent.widgetId()).filter(c -> c.valid() && (c.actions().contains("Select") || c.text().equals("Male") || c.text().equals("Female"))).list();
        Component target = potentialTargets.get(Random.nextInt(0, potentialTargets.size() - 1));

        if (target.click()) {
            int sleep = Random.nextInt(487, 1633);
            abstractScript.setStatus("Sleeping for " + sleep + "ms");
            Condition.sleep(sleep);
        }
    }
}