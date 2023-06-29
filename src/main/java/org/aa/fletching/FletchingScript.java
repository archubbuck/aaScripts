package org.aa.fletching;

import com.google.common.eventbus.Subscribe;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.aa.Task;
import org.aa.fletching.items.Bow;
import org.aa.fletching.tasks.*;
import org.jetbrains.annotations.NotNull;
import org.powbot.api.event.InventoryChangeEvent;
import org.powbot.api.rt4.GrandExchange;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.OptionType;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptConfiguration;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.api.script.paint.TrackSkillOption;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

import java.text.NumberFormat;
import java.util.Arrays;

@ScriptManifest(
        name = "aaFletching",
        description = "Fletches",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.3",
        category = ScriptCategory.Fletching)
@ScriptConfiguration.List({
        @ScriptConfiguration(
                name = "Bow",
                description = "The bow to fletch",
                optionType = OptionType.STRING,
                defaultValue = "Magic longbow"
        )
})
public class FletchingScript extends AbstractScript {

    private int unstrongBowsConsumed = 0;
    private int bowStringsConsumed = 0;
    private int bowsCreated = 0;

//    public static long lastXpDrop;
    private long profit;

    public static final int BOW_STRING_ID = 1777;
    public static Bow bow;

    private int BOW_STRING_PRICE;
    private int UNSTRUNG_BOW_PRICE;
    private int STRUNG_BOW_PRICE;

    private final Task[] tasks = {
        new OpenBankTask(),
        new CloseBankTask(),
        new WithdrawUnstrungBowTask(),
        new WithdrawBowStringTask(),
        new CombineBowMaterialsTask(),
        new FletchBowTask(),
        new DepositItemsAtBankTask()
    };

    @Override
    public void onStart() {

        bow = Arrays.stream(Bow.values())
                .filter(b -> b.getStrungName().equals(getOption("Bow")))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to identify the chosen bow."));

        PaintBuilder paintBuilder = new PaintBuilder()
                .addString("Status:", super::getStatus)
                .addString("Profit:", this::getProfit)
                .trackInventoryItem(bow.getStrungId(), null, TrackInventoryOption.QuantityChangeIncOny);

        paintBuilder = Skills.level(Skill.Fletching) >= 99
                ? paintBuilder.trackSkill(Skill.Fletching, TrackSkillOption.Exp)
                : paintBuilder.trackSkill(Skill.Fletching);

        addPaint(paintBuilder.build());

        BOW_STRING_PRICE = GrandExchange.getItemPrice(BOW_STRING_ID);
        UNSTRUNG_BOW_PRICE = GrandExchange.getItemPrice(bow.getUnstrungId());
        STRUNG_BOW_PRICE = GrandExchange.getItemPrice(bow.getStrungId());
    }

    @Override
    public void showOptionsDialog(@NotNull Function0<Unit> function0) {
        super.showOptionsDialog(function0);

        updateAllowedOptions("Bow", Arrays.stream(Bow.values())
                .map(Bow::getStrungName)
                .toArray(String[]::new));
    }

    @Subscribe
    public void onInventoryChange(InventoryChangeEvent event) {
        if (event.getQuantityChange() > 0) {
            if (event.getItemId() == bow.getStrungId()) {
                bowsCreated += event.getQuantityChange();
            }
        } else {
            if (event.getItemId() == bow.getUnstrungId()) {
                unstrongBowsConsumed += Math.abs(event.getQuantityChange());
            }
            if (event.getItemId() == BOW_STRING_ID) {
                bowStringsConsumed += Math.abs(event.getQuantityChange());
            }
        }
        profit = (long) (STRUNG_BOW_PRICE - BOW_STRING_PRICE - UNSTRUNG_BOW_PRICE) * bowsCreated;
    }

    @Override
    public void poll() {
        long pollExecutionTime = System.currentTimeMillis();
        for (Task task : tasks) {
            if (task.activate()) {
                long taskExecutionTime = System.currentTimeMillis();
                task.execute(this);
                taskExecutionTime = System.currentTimeMillis() - taskExecutionTime;
                getLog().info("[" + taskExecutionTime + "] " + task.getClass().getName());
                break;
            }
        }
        pollExecutionTime = System.currentTimeMillis() - pollExecutionTime;
        getLog().info("[" + pollExecutionTime + "] " + this.getClass().getName());
    }

    private String getProfit() {
        String totalProfit = NumberFormat.getInstance().format(profit);
        String profitPerHour = NumberFormat.getInstance().format((long) calculateProfitPerHour(profit, ScriptManager.INSTANCE.getRuntime(true)));
        return totalProfit + " (" + profitPerHour + ")";
    }

    private double calculateProfitPerHour(double totalProfit, long executionTimeMillis) {
        double executionTimeHours = executionTimeMillis / (1000.0 * 60 * 60);
        return executionTimeHours >= 1
                ? totalProfit / executionTimeHours
                : totalProfit * (1 / executionTimeHours);
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaFletching", "", "127.0.0.1:5785", true, true);
//        new ScriptUploader().uploadAndStart("aaFletching", "", "127.0.0.1:5855", true, true);
//        new ScriptUploader().uploadAndStart("aaFletching", "Stormance1635@gmail.com", "127.0.0.1:5835", true, true);
    }

}
