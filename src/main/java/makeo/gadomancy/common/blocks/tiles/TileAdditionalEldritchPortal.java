package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import makeo.gadomancy.common.data.ModConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.tiles.TileEldritchPortal;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 06.11.2015 23:32
 */
public class TileAdditionalEldritchPortal extends TileEldritchPortal {
    private int count = 0;

    @Override
    public void updateEntity() {
        this.count += 1;
        if ((this.worldObj.isRemote) && ((this.count % 250 == 0) || (this.count == 0))) {
            this.worldObj.playSound(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, "thaumcraft:evilportal", 1.0F, 1.0F, false);
        }
        if ((this.worldObj.isRemote) && (this.opencount < 30)) {
            this.opencount += 1;
        }

        if ((!this.worldObj.isRemote) && (this.count % 5 == 0)) {
            List<EntityPlayerMP> players = this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(0.5D, 1.0D, 0.5D));
            for (EntityPlayerMP player : players) {
                if ((player.ridingEntity == null) && (player.riddenByEntity == null)) {
                    if (player.timeUntilPortal > 0) {
                        player.timeUntilPortal = 100;
                    } else if (player.dimension != ModConfig.dimOuterId) {
                        player.timeUntilPortal = 100;

                        //TODO: Teleport to dim
                        //player.mcServer.getConfigurationManager().transferPlayerToDimension(player, Config.dimensionOuterId, new TeleporterThaumcraft(mServer.worldServerForDimension(Config.dimensionOuterId)));

                        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ENTEROUTER")) {
                            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ENTEROUTER"), player);
                            Thaumcraft.proxy.getResearchManager().completeResearch(player, "ENTEROUTER");
                        }
                    } else {
                        player.timeUntilPortal = 100;

                        //TODO: Teleport out
                        //player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new TeleporterThaumcraft(mServer.worldServerForDimension(0)));
                    }
                }
            }
        }
    }
}
