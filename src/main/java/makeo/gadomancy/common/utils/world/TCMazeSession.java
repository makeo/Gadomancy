package makeo.gadomancy.common.utils.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.apache.logging.log4j.LogManager;
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
class TCMazeSession {

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

    final void closeSession() {
        TCMazeHandler.free(chunksAffected);
        WorldUtil.tryTeleportBack(player, originDimId);
        player.setPosition(originLocation.xCoord, originLocation.yCoord, originLocation.zCoord);
    }

    final void startSession() {
        if(portalCell == null) {
            LogManager.getLogger().error("Thaumcraft didn't generate a portal! Stopping instance!", new IllegalStateException());
            closeSession();
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Thaumcraft didn't generate a portal in the Eldritch dimension. Sorry, we can't teleport you.."));
        } else {
            WorldUtil.teleportToFakeOuter(player);
            int x = portalCell.x * 16 + 7;
            int z = portalCell.z * 16 + 7;
            player.setPosition(x, TCMazeHandler.TELEPORT_LAYER_Y, z);
        }
    }

}
