package makeo.gadomancy.client.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import makeo.gadomancy.client.ClientProxy;
import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.renderers.tile.RenderTileEssentiaCompressor;
import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.Gadomancy;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Queue;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 01.11.2015 10:41
 */
public class ClientHandler {

    public static int ticks;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase.equals(TickEvent.Phase.END) && !Minecraft.getMinecraft().isGamePaused()) {
            FamiliarHandlerClient.playerTickEvent();
            ticks++;

            EffectHandler.getInstance().tick();
        }

        Queue<Runnable> actions = ((ClientProxy)Gadomancy.proxy).clientActions;
        while(actions.peek() != null) {
            actions.poll().run();
        }
    }

    @SubscribeEvent
    public void onDc(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        EffectHandler.getInstance().clear();
        RenderTileEssentiaCompressor.ownedVortex.clear();
    }

}
