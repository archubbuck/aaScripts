package org.aa.gnome.tasks.shop.heckelfunch;

import org.aa.AbstractScript;
import org.aa.Task;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

public class OpenHeckelFunchShopTask implements Task {
    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private final int COINS_ID = 995;

    private Npc heckelFunch;

    @Override
    public boolean activate() {
        return !Store.opened()
                && !Inventory.isFull()
                && Inventory.stream().id(COINS_ID).isNotEmpty()
                && (heckelFunch = Npcs.stream().name("Heckel Funch").action("Trade").first()).valid();
    }

    @Override
    public void execute(AbstractScript abstractScript) {

        if (!heckelFunch.inViewport(true)) {
            logger.info("Heckel Funch is not in the viewport; turning the camera");
            Camera.turnTo(heckelFunch);
            boolean heckelInViewport = Condition.wait(() -> heckelFunch.valid() && heckelFunch.inViewport(true), 75, 30);
            logger.info(
                    heckelInViewport
                        ? "We turned the camera to Heckel Funch and he is now in the viewport!"
                        : "We failed to turn the camera for Heckel Funch to be in the viewport!"
            );
        }

        logger.info("Attempting to open Heckel Funch's shop");

        boolean openedTheShop = heckelFunch.interact("Trade")
                && Condition.wait(Store::opened, 75, 30);

        logger.info(
                openedTheShop
                        ? "We opened the shop!"
                        : "We failed to open the shop!"
        );

    }
}
