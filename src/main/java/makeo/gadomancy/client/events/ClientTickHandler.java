package makeo.gadomancy.client.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import makeo.gadomancy.client.util.FamiliarHandlerClient;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 01.11.2015 10:41
 */
public class ClientTickHandler {

    public static int ticks;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.END)) {
            FamiliarHandlerClient.playerTickEvent();
            ticks++;
        }
    }

}
