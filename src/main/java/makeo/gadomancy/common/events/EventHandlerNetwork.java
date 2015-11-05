package makeo.gadomancy.common.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import makeo.gadomancy.common.network.packets.PacketUpdateGolemTypeOrder;
import makeo.gadomancy.common.utils.GolemEnumHelper;
import makeo.gadomancy.common.utils.world.TCMazeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 30.07.2015 18:26
 */
public class EventHandlerNetwork {
    @SubscribeEvent
    public void on(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayerMP p = (EntityPlayerMP) e.player;
        if(!p.playerNetServerHandler.netManager.isLocalChannel()) {
            PacketHandler.INSTANCE.sendTo(new PacketUpdateGolemTypeOrder(GolemEnumHelper.getCurrentMapping()), p);
            Gadomancy.proxy.familiarHandler.checkPlayerEquipment(p);
            PacketHandler.INSTANCE.sendTo(new PacketFamiliar.PacketFamiliarSyncCompletely(Gadomancy.proxy.familiarHandler.getCurrentActiveFamiliars()), p);
        }
    }

    @SubscribeEvent
    public void on(PlayerEvent.PlayerLoggedOutEvent e) {
        EntityPlayer player = e.player;
        Gadomancy.proxy.familiarHandler.notifyUnequip(player.worldObj, player);

        TCMazeHandler.closeSession(e.player);
    }
}
