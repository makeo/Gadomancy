package makeo.gadomancy.client.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.client.util.GrowingDisplayManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 23.10.2015 23:09
 */
public class RenderWorldTickListener {

    @SubscribeEvent
    public void worldRenderEvent(RenderWorldLastEvent event) {
        GrowingDisplayManager.notifyRenderTick();
    }

}
