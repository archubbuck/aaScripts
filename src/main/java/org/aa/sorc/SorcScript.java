package org.aa.sorc;

import org.aa.sorc.collections.Areas;
import org.aa.sorc.collections.GameObjects;
import org.aa.sorc.collections.Items;
import org.aa.sorc.collections.Tiles;
import org.core.ScriptManifestDefaults;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.rlib.generated.RItemDefinition;
import org.powbot.mobile.rscache.loader.ItemLoader;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@ScriptManifest(
        name = "aaSorc",
        description = "Completes the sorc minigame",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Minigame)
public class SorcScript extends AbstractScript {
    private final boolean redeem = true;

    protected State state = new State();
    protected Paint paint;

    GameObject tree;
    GameObject gate;

    @Override
    public void poll() {
        if (redeem) {
            exchange();
        } else {
            squirk();
        }
    }

    private int getCountOfStaminas() {
        return (int) Inventory.stream().name(Items.STAMINA_POTION_4).count();
    }

    private void loadCache() {
        tree = getTree();
        gate = getGate();
    }

    private GameObject getTree() {
        return Objects.stream().name(GameObjects.TREE_NAME).action(GameObjects.TREE_ACTION).within(Tiles.TREE_TILE, 5).first();
    }

    private GameObject getGate() {
        return Objects.stream().name(GameObjects.GATE_NAME).action(GameObjects.GATE_ACTION).at(Tiles.GATE_TILE).first();
    }

    @Override
    public void onStart() {

        PaintBuilder paintBuilder = new PaintBuilder();

        /*
         * Track the number of laps by tracking the fruit
         */

        paintBuilder.trackInventoryItem(10845, Items.SUMMER_SQUIRK, TrackInventoryOption.QuantityChangeIncOny);

        /*
         * Track the juice so that we can calculate the potential XP
         */

        paintBuilder.trackInventoryItem(10849, Items.SUMMER_SQUIRK_JUICE, TrackInventoryOption.QuantityChangeIncOny);

        paint = paintBuilder.build();
        addPaint(paint);
    }

    public void exchange() {
        boolean hasJuice = Inventory.stream().name(Items.SUMMER_SQUIRK_JUICE).isNotEmpty();

        /*
         * If the player has juice in their inventory, exchange it for XP
         */

        if (hasJuice) {
            var osmanTile = new Tile(3287, 3182, 0);
            var osman = Npcs.stream().name("Osman").first();

            if (!osman.valid()) {
                boolean navigated = Movement.builder(osmanTile)
                        .setRunMin(Configuration.getRunEnergyMin())
                        .setRunMax(Configuration.getRunEnergyMax())
                        .setWalkUntil(osman::valid)
                        .setUseTeleports(false)
                        .move()
                        .getSuccess();
                if (!navigated) {
                    System.out.println("Failed to locate Osman in time!");
                    return;
                }
            }

            boolean chatting = osman.interact("Talk-to")
                    && Condition.wait(Chat::chatting, 125, 30);

            if (chatting) {
                boolean chatComplete = Chat.completeChat(
                        "I'd like to talk about sq'irks."
                );
                if (!chatComplete) {
                    System.out.println("Failed to complete the chat!");
                }
            }

            return;
        }

        /*
         * If the player doesn't have any juice in their inventory, then we must bank
         */

        if (!Areas.AL_KHARID_BANK.contains(Players.local())) {
            boolean navigated = Movement.builder(Areas.AL_KHARID_BANK.getRandomTile())
                    .setRunMin(Configuration.getRunEnergyMin())
                    .setRunMax(Configuration.getRunEnergyMax())
                    .setWalkUntil(() -> Areas.AL_KHARID_BANK.contains(Players.local()))
                    .setUseTeleports(false)
                    .move()
                    .getSuccess();
            if (!navigated) {
                System.out.println("Failed to navigate to the bank within the threshold");
                return;
            }
        }

        /*
         * Open the bank
         */

        boolean bankOpened = Bank.open() && Condition.wait(Bank::opened, 125, 20);
        if (!bankOpened) {
            getLog().warning("Failed to open the bank");
            return;
        }

        /*
         * Withdraw the juice from the bank
         */

        boolean withdrewJuice = Bank.withdraw(Items.SUMMER_SQUIRK_JUICE, Bank.Amount.ALL)
                && Condition.wait(() -> Inventory.stream().name(Items.SUMMER_SQUIRK_JUICE).isNotEmpty(), 125, 20);
        if (!withdrewJuice) {
            getLog().warning("Failed to withdraw the juice");
            return;
        }

    }

    public void squirk() {
        /*
         * Idle if the player's animation matches
         */

        int[] animationsToIdle = { 2280 };

        Condition.wait(() -> {
            boolean satisified = Arrays.stream(animationsToIdle).noneMatch(animationId -> animationId == Players.local().animation());
            if (!satisified) {
                getLog().info("[GLOBAL] Waiting for animation to end");
            }
            return satisified;
        }, 50,  12_000);

        /*
         * Switch to world 523 - the Sorc Garden world
         */

        if (Worlds.current().getNumber() != Configuration.getSorcGardenWorld()) {
            boolean hopped = Worlds.stream().id(Configuration.getSorcGardenWorld()).first().hop()
                    && Condition.wait(() -> Worlds.current().getNumber() == Configuration.getSorcGardenWorld(), 125, 50);
            if (!hopped) {
                getLog().warning("Failed to hop worlds");
                return;
            }
        }

        /*
         * Drink a stamina potion when run energy is below the threshold
         */

        int startingEnergyLevel = Movement.energyLevel();

        if (startingEnergyLevel <= state.runEnergyMin) {
            Item stamina = Inventory.stream().nameContains("Stamina").action("Drink").first();
            if (stamina.valid()) {
                boolean drank = stamina.interact("Drink") && Condition.wait(() -> Movement.energyLevel() > startingEnergyLevel, 150, 10);
                if (!drank) {
                    getLog().warning("Failed to drink the stamina");
                    return;
                }
                state.runEnergyMin = Configuration.getRandomRunEnergy();
            } else {
                getLog().info("Stamina is invalid");
            }
        }

        /*
         * Create juice if ingredients are available
         */

        if (Inventory.stream().name(Items.PESTLE_AND_MORTAR).isNotEmpty()
                && Inventory.stream().name(Items.BEER_GLASS).isNotEmpty()
                && Inventory.stream().name(Items.SUMMER_SQUIRK).count() >= 2
        ) {
            Item pestle = Inventory.stream().name(Items.PESTLE_AND_MORTAR).first();
            Item squirk = Inventory.stream().name(Items.SUMMER_SQUIRK).first();
            boolean success = pestle.useOn(squirk) && Condition.wait(() -> !squirk.valid(), 125, 15);
            if (!success) {
                getLog().warning("Failed to create the juice");
                return;
            }
        }

        /*
         * Handle banking if we are out of beer glasses
         */

        if (Inventory.stream().name(Items.BEER_GLASS).isEmpty()) {
            if (!Areas.AL_KHARID_BANK.contains(Players.local())) {
                GameObject fountain = Objects.stream().name(GameObjects.FOUNTAIN_NAME).action(GameObjects.FOUNTAIN_ACTION).first();
                if (fountain.valid()) {
                    boolean teleported = fountain.interact(GameObjects.FOUNTAIN_ACTION)
                            && Condition.wait(() -> Areas.SORC_GARDEN_HOUSE.contains(Players.local()), 125, 50);
                    if (!teleported) {
                        getLog().warning("Failed to teleport through the fountain");
                        return;
                    }
                }
                getLog().info("Using dax to walk to the bank");
                Movement.builder(Areas.AL_KHARID_BANK.getRandomTile())
                        .setRunMin(Configuration.getRunEnergyMin())
                        .setRunMax(Configuration.getRunEnergyMax())
                        .setUseTeleports(false)
                        .move();
                return;
            } else {

                /*
                 * Open the bank
                 */

                boolean bankOpened = Bank.open() && Condition.wait(Bank::opened, 125, 20);
                if (!bankOpened) {
                    getLog().warning("Failed to open the bank");
                    return;
                }

                /*
                 * Deposit all items with exclusions
                 */

                boolean depositedWithExclusions = Bank.depositAllExcept(Items.PESTLE_AND_MORTAR, Items.STAMINA_POTION_4, Items.BEER_GLASS);
                if (!depositedWithExclusions) {
                    getLog().warning("Failed to deposit with exclusions");
                    return;
                }

                /*
                 * Withdraw the pestle
                 */

                if (Inventory.stream().name(Items.PESTLE_AND_MORTAR).isEmpty()) {
                    boolean withdrewPestle = Bank.withdraw(Items.PESTLE_AND_MORTAR, 1)
                            && Condition.wait(() -> Inventory.stream().name(Items.PESTLE_AND_MORTAR).isNotEmpty(), 125, 20);
                    if (!withdrewPestle) {
                        getLog().warning("Failed to withdraw pestle");
                        return;
                    }
                }

                /*
                 * Withdraw the staminas
                 */

                int staminasRequired = Configuration.getStaminasRequired();
                int countOfStaminas = getCountOfStaminas();

                if (countOfStaminas < staminasRequired) {
                    boolean withdrewStaminas = Bank.withdraw(Items.STAMINA_POTION_4, staminasRequired - countOfStaminas)
                            && getCountOfStaminas() == staminasRequired;
                    if (!withdrewStaminas) {
                        getLog().warning("Failed to withdraw staminas");
                        return;
                    }
                }

                /*
                 * Withdraw the beer glasses
                 */

                if (Inventory.emptySlotCount() > 2) {
                    boolean withdrewBeerGlasses = Bank.withdraw(Items.BEER_GLASS, Inventory.emptySlotCount() - 2)
                            && Condition.wait(Inventory::isFull, 125, 20);
                    if (!withdrewBeerGlasses) {
                        getLog().warning("Failed to withdraw beer glasses");
                        return;
                    }
                }
            }
        }

        if (Areas.SORC_GARDEN_HOUSE.contains(Players.local())) {
            Npc apprentice = Npcs.stream().name("Apprentice").action("Teleport").first();
            if (apprentice.valid()) {
                // TODO: Refactor, this is a bad query
                boolean teleported = apprentice.interact("Teleport")
                        && Condition.wait(Tiles.GATE_TILE::reachable, 125, 50);
                if (!teleported) {
                    getLog().warning("Failed to teleport through the apprentice");
                    return;
                }
            }
        }

        loadCache();

        boolean gateReachable = gate.reachable();
        boolean treeReachable = tree.reachable();

        /*
         * Navigate the gate
         */

        if (gateReachable) {
            getLog().info("Navigating the gate");
            if (!gate.inViewport(true)) {
                getLog().info("Angling the camera to the gate");
                Camera.angleToLocatable(gate);
            }
            boolean success = gate.interact(GameObjects.GATE_ACTION)
                    && Condition.wait(() -> !gate.valid() || !gate.reachable(), 175, 20);
            if (success) getLog().info("Successfully navigated the gate");
            else getLog().warning("Failed to navigate the gate within the threshold");
            return;
        }

        /*
         * Navigate the tree
         */

        if (treeReachable) {
            getLog().info("Navigating the tree");
            if (!tree.inViewport(true)) {
                getLog().info("Angling the camera to the tree");
                Camera.angleToLocatable(tree);
            }
            boolean success = tree.interact(GameObjects.TREE_ACTION)
                    && Condition.wait(() -> {
                boolean animating = Players.local().animation() == 2280;
                if (animating) {
                    getLog().info("[TREE] Waiting for animation to end");
                }
                boolean satisified = !animating && (!tree.valid() || !tree.reachable());
                if (satisified) {
                    getLog().info("Successfully navigated the tree");
                }
                return satisified;
            }, 175, 100);
            return;
        }

        /*
         * Panic
         */

        getLog().info("Using dax to walk to the SORC_GARDEN_HOUSE");
        Movement.builder(Areas.SORC_GARDEN_HOUSE.getRandomTile())
                .setRunMin(Configuration.getRunEnergyMin())
                .setRunMax(Configuration.getRunEnergyMax())
                .setUseTeleports(false)
                .move();
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaSorc", "", "127.0.0.1:" + System.getProperty("port"), true, true);
    }
}
