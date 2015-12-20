package makeo.gadomancy.common.integration.mystcraft;

import com.xcompwiz.mystcraft.api.event.LinkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.common.data.config.ModConfig;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 20.12.2015 15:53
 */
public class EventHandlerMystcraft {

    @SubscribeEvent
    public void onLink(LinkEvent.LinkEventAllow event) {
        if(event.origin != null && event.origin.provider.dimensionId == ModConfig.dimOuterId) {
            event.setCanceled(true);
        }
        if(event.destination != null && event.destination.provider.dimensionId == ModConfig.dimOuterId) {
            event.setCanceled(true);
        }
    }

}
