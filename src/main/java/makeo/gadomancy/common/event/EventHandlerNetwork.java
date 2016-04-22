package makeo.gadomancy.common.event;

import makeo.gadomancy.common.data.DataAchromatic;
import makeo.gadomancy.common.data.DataFamiliar;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketSyncConfigs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 30.07.2015 18:26
 */
public class EventHandlerNetwork {

    //TODO Restore old client configs on server leave again.
    @SubscribeEvent
    public void on(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayerMP p = (EntityPlayerMP) e.player;
        //PacketHandler.INSTANCE.sendTo(new PacketUpdateGolemTypeOrder(GolemEnumHelper.getCurrentMapping()), p);
        PacketHandler.INSTANCE.sendTo(new PacketSyncConfigs(), p);
        ((DataFamiliar) SyncDataHolder.getDataServer("FamiliarData")).checkPlayerEquipment(p);
        ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).checkPotionEffect(p);
        SyncDataHolder.syncAllDataTo(p);
    }

    @SubscribeEvent
    public void on(PlayerEvent.PlayerLoggedOutEvent e) {
        EntityPlayer player = e.player;
        DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
        if(familiarData.hasFamiliar(player)) {
            familiarData.handleUnsafeUnequip(player);
        }

        //TCMazeHandler.closeSession(e.player, true);
    }
}
