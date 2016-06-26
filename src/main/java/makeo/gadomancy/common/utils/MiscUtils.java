package makeo.gadomancy.common.utils;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 17.11.2015 18:39
 */
public final class MiscUtils {

    private MiscUtils() {}

    private static final int[] R_NUMBERS = {1000, 900,  500, 400,  100,  90,  50,   40,  10,   9,    5,   4,    1};
    private static final String[] R_CHARS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static String toRomanNumeral(int number) {
        if(number < 0 || number > 3999) {
            return String.valueOf(number);
        }
        String roman = "";
        for (int i = 0; i < R_NUMBERS.length; i++) {
            while (number >= R_NUMBERS[i]) {
                roman += R_CHARS[i];
                number -= R_NUMBERS[i];
            }
        }
        return roman;
    }

    public static Vector3 interpolateEntityPosition(Entity entity, float partialTicks) {
        double iPx = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double iPy = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        double iPz = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        return new Vector3(iPx, iPy, iPz);
    }
    
    public static int ticksForDays(int days) {
        return ticksForHours(days * 24);
    }
    
    public static int ticksForHours(int hours) {
        return ticksForMinutes(hours * 60);
    }

    public static int ticksForMinutes(int minutes) {
        return ticksForSeconds(minutes * 60);
    }

    public static int ticksForSeconds(int seconds) {
        return seconds * 20;
    }

    public static List<ChunkCoordinates> getCoordinatesAround(ChunkCoordinates center) {
        List<ChunkCoordinates> coords = new ArrayList<ChunkCoordinates>();
        coords.add(new ChunkCoordinates(center.posX, center.posY, center.posZ));
        coords.add(new ChunkCoordinates(center.posX + 1, center.posY,     center.posZ));
        coords.add(new ChunkCoordinates(center.posX,     center.posY,     center.posZ + 1));
        coords.add(new ChunkCoordinates(center.posX - 1, center.posY,     center.posZ));
        coords.add(new ChunkCoordinates(center.posX,     center.posY,     center.posZ - 1));
        coords.add(new ChunkCoordinates(center.posX,     center.posY - 1, center.posZ));
        coords.add(new ChunkCoordinates(center.posX,     center.posY + 1, center.posZ));
        return coords;
    }

    public static NetworkRegistry.TargetPoint getTargetPoint(World world, Entity entity, double range) {
        return getTargetPoint(world, range, getPositionVector(entity));
    }

    public static NetworkRegistry.TargetPoint getTargetPoint(World world, TileEntity tileEntity, double range) {
        return getTargetPoint(world, range, new Vector3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
    }

    public static NetworkRegistry.TargetPoint getTargetPoint(World world, double range, Vector3 target) {
        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, target.getX(), target.getY(), target.getZ(), range);
    }

    //Nothing to see here. Please move on.
    public static boolean isANotApprovedOrMisunderstoodPersonFromMoreDoor(EntityPlayer player) {
        int uuHash = player.getUniqueID().hashCode();
        return uuHash == 1529485240 || uuHash == 914342508;
    }

    //For ppl we like. OK?!?!?!!!111111ELEVEN!!111
    public static boolean isPrivilegedUser(EntityPlayer player) {
        if(isANotApprovedOrMisunderstoodPersonFromMoreDoor(player)) return true;
        int uuHashOther = player.getUniqueID().hashCode();
        switch (uuHashOther) {
            case -1899266570: //SH_F
            case  -335313669: //RCNumbers
            case   126020810: //Rk
                return true;
        }
        return false;
    }

    public static boolean isPlayerFakeMP(EntityPlayerMP player) {
        if(player instanceof FakePlayer) return true;
        if(player.getClass() != EntityPlayerMP.class) return true;
        if(player.playerNetServerHandler == null) return true;
        try {
            player.getPlayerIP().length();
            player.playerNetServerHandler.netManager.getSocketAddress().toString();
        } catch (Exception exc) {
            return true;
        }
        if(MinecraftServer.getServer().getConfigurationManager().playerEntityList == null) return true;
        return !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
    }

    public static Vector3 getPositionVector(Entity e) {
        Vector3 v = new Vector3(e.posX, e.posY, e.posZ);
        if(e instanceof EntityItem) {
            v.setY(v.getY() + 0.2F);
        }
        return v;
    }

}
