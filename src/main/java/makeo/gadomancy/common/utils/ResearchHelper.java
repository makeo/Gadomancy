package makeo.gadomancy.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 30.10.2015 21:23
 */
public class ResearchHelper {

    public static void distributeResearch(String research, World world, double x, double y, double z, double radius) {
        List players = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(radius, radius, radius));
        for(Object pl : players) {
            EntityPlayer player = (EntityPlayer) pl;
            if(!ResearchManager.isResearchComplete(player.getCommandSenderName(), research) && ResearchManager.doesPlayerHaveRequisites(player.getCommandSenderName(), research)) {
                thaumcraft.common.lib.network.PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(research), (EntityPlayerMP)player);
                Thaumcraft.proxy.getResearchManager().completeResearch(player, research);
            }
        }
    }

}
