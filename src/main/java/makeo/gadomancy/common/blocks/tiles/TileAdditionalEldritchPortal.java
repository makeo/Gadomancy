package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.utils.world.TCMazeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileEldritchPortal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 06.11.2015 23:32
 */
public class TileAdditionalEldritchPortal extends TileEldritchPortal {

    private static Map<EntityPlayer, ExtendedChunkCoordinates> trackedPortalActivity = new HashMap<EntityPlayer, ExtendedChunkCoordinates>();
    private EntityPlayer toTeleport;
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

        if ((!this.worldObj.isRemote) && (this.count % 5 == 0) && toTeleport == null) {
            List<EntityPlayerMP> players = this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(0.5D, 1.0D, 0.5D));
            if(players != null && !players.isEmpty()) {
                EntityPlayerMP toTeleport = players.get(0);

                if(toTeleport.timeUntilPortal > 0) return;

                if(!ResearchManager.isResearchComplete(toTeleport.getCommandSenderName(), TileNodeManipulator.MultiblockType.E_PORTAL_CREATOR.getResearchNeeded())) {
                    return;
                }

                this.toTeleport = toTeleport;
                toTeleport.timeUntilPortal = 200;

                if(toTeleport.dimension != ModConfig.dimOuterId) {
                    //Teleporting there.

                    if(TCMazeHandler.createSessionWaitForTeleport(toTeleport)) {
                        startTracking(toTeleport, new ExtendedChunkCoordinates(new ChunkCoordinates(xCoord, yCoord, zCoord), toTeleport.dimension));
                    }
                } else {
                    //Teleporting back.

                    TCMazeHandler.closeSession(toTeleport, true);
                }
            }
        }
    }

    public static void startTracking(EntityPlayer player, ExtendedChunkCoordinates tileCoords) {
        if(!trackedPortalActivity.containsKey(player)) {
            trackedPortalActivity.put(player, tileCoords);
        }
    }

    public static void informSessionStart(EntityPlayer player) {
        if(trackedPortalActivity.containsKey(player)) {
            ExtendedChunkCoordinates tileCoords = trackedPortalActivity.get(player);
            trackedPortalActivity.remove(player);
            if(tileCoords != null) {
                ChunkCoordinates cc = tileCoords.coordinates;
                WorldServer ws = MinecraftServer.getServer().worldServerForDimension(tileCoords.dimId);
                ws.removeTileEntity(cc.posX, cc.posY, cc.posZ);
                ws.setBlockToAir(cc.posX, cc.posY, cc.posZ);
                ws.markBlockForUpdate(cc.posX, cc.posY, cc.posZ);
            }
        }
    }

    public static class ExtendedChunkCoordinates {

        private ChunkCoordinates coordinates;
        private int dimId;

        public ExtendedChunkCoordinates(ChunkCoordinates coordinates, int dimId) {
            this.coordinates = coordinates;
            this.dimId = dimId;
        }
    }

}
