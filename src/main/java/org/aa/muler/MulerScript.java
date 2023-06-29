package org.aa.muler;

import com.google.common.eventbus.Subscribe;
import org.aa.AbstractScript;
import org.aa.ScriptManifestDefaults;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.event.MessageEvent;
import org.powbot.api.event.MessageType;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.stream.item.InventoryItemStream;
import org.powbot.api.rt4.stream.locatable.interactive.PlayerStream;
import org.powbot.api.script.ScriptCategory;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.SettingsManager;
import org.powbot.mobile.ToggleId;
import org.powbot.mobile.service.ScriptUploader;

@ScriptManifest(
        name = "aaMuler",
        description = "Mules",
        author = ScriptManifestDefaults.AUTHOR,
        version = "0.0.1",
        category = ScriptCategory.Other)
public class MulerScript extends AbstractScript {

    private final String MULE = "Ries";
    private final int WORLD = 506;
    private final Tile TILE = new Tile(1627, 3948);

    private boolean shouldMule;
    private long tradeRequestLastSent = -1;

    @Override
    public void onStart() {
        SettingsManager.set(ToggleId.DismissTradeWidget, false);

        Paint paint = new PaintBuilder()
                .addString("OK", () -> "OK")
                .build();

        addPaint(paint);
    }

    @Subscribe
    public void onMessage(MessageEvent event) {

        if (!event.getMessageType().equals(MessageType.FriendOnlineStatus)) {
            return;
        }

        if (!event.getMessage().equals(MULE + " has logged in.")) {
            return;
        }

        shouldMule = true;
    }

    @Override
    public void poll() {
        getLog().info("itr1");

        if (!shouldMule) {
            return;
        }

        getLog().info("itr2");

        if (Players.local().tile().distanceTo(TILE) > 5 && Movement.walkTo(TILE)) {
            getLog().info("WALKING TO TILE");
            boolean reachedTheTile = Condition.wait(() -> Players.local().tile().equals(TILE), 150, 15);
            getLog().info(reachedTheTile ? "WE REACHED THE TILE" : "HAVE NOT YET REACHED THE TILE");
            return;
        }

        getLog().info("itr3");

        PlayerStream mules = Players.stream().filter(player -> player.name().equals(MULE));

        if (mules.isEmpty()) {
            getLog().info("UNABLE TO LOCATE MULE");
            return;
        }

        getLog().info("itr4");

        Player mule = mules.first();
        getLog().info("MULE: " + mule.name());

        if (!Trade.isOpen() && getMsSinceLastTradeRequest() > Random.nextInt(6500, 37000)) {
            getLog().info("SENDING TRADE REQUEST");
            if (mule.interact("Trade with")) {
                tradeRequestLastSent = System.currentTimeMillis();
                return;
            }
        }

        getLog().info("itr5");

        if (Trade.isOpen(Trade.Screen.First)) {
            getLog().info("FIRST SCRN");

            InventoryItemStream tradeableItems = Inventory.stream().filter(Item::tradeable);

            if (tradeableItems.isEmpty() && !Trade.hasAccepted(Trade.Party.Me) && Trade.accept()) {
                getLog().info("ACCEPTING THE TRADE");
                boolean weHaveAccepted = Condition.wait(() -> Trade.hasAccepted(Trade.Party.Me), 150, 15);
                getLog().info(weHaveAccepted ? "WE HAVE ACCEPTED" : "FAILED TO ACCEPT");
                return;
            }

            Item itemToTrade = tradeableItems.any();

            getLog().info("OFFERING " + itemToTrade.stackSize() + " " + itemToTrade.name());
            if (Trade.offerAll(itemToTrade.id())) {
                boolean itemAddedToTradeScreen = Condition.wait(() -> Trade.stream().id(itemToTrade.id()).isNotEmpty(), 150, 15);
                getLog().info(itemAddedToTradeScreen ? "ADDED ITEMS TO THE SCREEN" : "FAILED TO ADD ITEMS TO SCREEN");
                return;
            }
        }

        getLog().info("itr6");

        if (Trade.isOpen(Trade.Screen.Second) && !Trade.hasAccepted(Trade.Party.Me)) {
            getLog().info("ACCEPTING SECOND SCREEN");
            boolean acceptedSecondTradeScreen = Trade.accept() && Condition.wait(() -> !Trade.isOpen(Trade.Screen.Second) || Trade.hasAccepted(Trade.Party.Me), 150, 15);
            getLog().info(acceptedSecondTradeScreen ? "ACCEPTED SECOND TRADE SCREEN" : "FAILED TO ACCEPT SECOND TRADE SCREEN");
            shouldMule = !acceptedSecondTradeScreen;
            return;
        }

        getLog().info("itr7");

    }

    private long getMsSinceLastTradeRequest() {
        return System.currentTimeMillis() - tradeRequestLastSent;
    }

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("aaMuler", "Stormance1635@gmail.com", "127.0.0.1:5835", true, true);
    }
}
