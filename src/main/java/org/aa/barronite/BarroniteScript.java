package org.aa.barronite;

import com.google.common.eventbus.Subscribe;
import org.aa.ScriptManifestDefaults;
import org.aa.barronite.Task;
import org.aa.barronite.mining.*;
import org.powbot.api.event.PlayerAnimationChangedEvent;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.api.script.paint.TrackSkillOption;

@ScriptManifest(
        name = "aaBarronite",
        description = "Trains mining and smithing with barronite",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Mining)
public class BarroniteScript extends AbstractScript {

    public static final int BARRONITE_SHARDS_ID = 25676;
    public static final int BARRONITE_DEPOSIT_ID = 25684;

    private static long lastAnimationTs;
    public static long lastIdleAnimationTs;

    public static GameObject ore = GameObject.getNil();

    private String status = "Initializing";

    private final Task[] tasks = {
        new WalkToBankTask(),
        new CloseBankTask(),
        new OpenBankTask(),
        new DepositItemsAtBankTask(),
        new MineBarroniteTask(),
        new WalkToMineTask(),
    };

    @Override
    public void onStart() {
        Paint paint = new PaintBuilder()
//                .addString("Status:", this::getStatus)
//                .addString("TSA:", () -> String.valueOf(getTimeSinceLastAnimation()))
//                .addString("LIA:", () -> String.valueOf(System.currentTimeMillis() - lastIdleAnimationTs))
                .trackSkill(Skill.Mining, TrackSkillOption.Level)
                .trackSkill(Skill.Mining, TrackSkillOption.Exp)
                .trackSkill(Skill.Mining, TrackSkillOption.TimeToNextLevel)
                .trackInventoryItem(BARRONITE_SHARDS_ID,null, TrackInventoryOption.QuantityChange)
                .trackInventoryItem(BARRONITE_DEPOSIT_ID, null, TrackInventoryOption.QuantityChange)
                .build();
        addPaint(paint);
    }

    @Override
    public void poll() {
//        getLog().info(String.valueOf(Players.local().distanceTo(BarroniteScript.ore)));
        for (Task task : tasks) {
            if (task.activate()) {
                task.execute();
                return;
            }
        }
    }

    @Subscribe
    public void onPlayerAnimationChanged(PlayerAnimationChangedEvent event) {
        boolean isLocalPlayer = event.getPlayer().name().equals(Players.local().name());
        if (!isLocalPlayer) {
            return;
        }
        lastAnimationTs = System.currentTimeMillis();
    }

    public String setStatus(String status) {
        return this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static long getTimeSinceLastAnimation() {
        return System.currentTimeMillis() - lastAnimationTs;
    }

    public static void main(String[] args) {
        new BarroniteScript().startScript();
    }
}
