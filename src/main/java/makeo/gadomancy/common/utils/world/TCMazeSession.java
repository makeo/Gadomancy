package makeo.gadomancy.common.utils.world;

import makeo.gadomancy.common.data.config.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.lib.world.dim.CellLoc;

import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 05.11.2015 14:12
 */
public class TCMazeSession {

    public final EntityPlayerMP player;
    public final Map<CellLoc, Short> chunksAffected;
    public final CellLoc portalCell;

    public final int originDimId;
    public final Vec3 originLocation;

    TCMazeSession(EntityPlayer owner, Map<CellLoc, Short> locations, int dim, Vec3 origin) {
        this.player = (EntityPlayerMP) owner;
        this.chunksAffected = locations;
        this.originDimId = dim;
        this.originLocation = origin;
        this.portalCell = findPortal();
    }

    TCMazeSession() {
        player = null;
        chunksAffected = null;
        portalCell = null;
        originDimId = 0;
        originLocation = null;
    }

    private CellLoc findPortal() {
        for(CellLoc loc : chunksAffected.keySet()) {
            Short s = chunksAffected.get(loc);
            Cell c = new Cell(s);
            if(c.feature == 1) {
                return loc;
            }
        }
        return null;
    }

    final void closeSession(boolean teleport) {
        TCMazeHandler.free(chunksAffected);
        if(teleport) {
            WorldUtil.tryTeleportBack(player, originDimId);
            player.setPositionAndUpdate(originLocation.xCoord, originLocation.yCoord, originLocation.zCoord);
        }
    }

    final void startSession() {
        WorldServer ws = MinecraftServer.getServer().worldServerForDimension(ModConfig.dimOuterId);

        for(CellLoc loc : chunksAffected.keySet()) {
            long k = ChunkCoordIntPair.chunkXZ2Int(loc.x, loc.z);
            if(ws.theChunkProviderServer.loadedChunkHashMap.containsItem(k)) {
                Chunk c = (Chunk) ws.theChunkProviderServer.loadedChunkHashMap.getValueByKey(k);
                ws.theChunkProviderServer.loadedChunks.remove(c);
                ws.theChunkProviderServer.loadedChunkHashMap.remove(k);
            }
        }

        if(portalCell == null) {
            LogManager.getLogger().error("Thaumcraft didn't generate a portal! Stopping instance! PLEASE REPORT THIS ERROR!", new IllegalStateException());
            closeSession(false);
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Thaumcraft didn't generate a portal in the Eldritch dimension. Sorry, we can't teleport you.."));
        } else {
            WorldUtil.teleportToFakeOuter(player);
            int x = portalCell.x * 16 + 8;
            int z = portalCell.z * 16 + 8;
            player.setPositionAndUpdate(x + 0.5, TCMazeHandler.TELEPORT_LAYER_Y, z + 0.5);
            Thaumcraft.proxy.getResearchManager().completeResearch(player, "ENTEROUTER"); //badumm tss
        }
    }

    public static TCMazeSession placeholder() {
        return new TCMazeSession();
    }
}
