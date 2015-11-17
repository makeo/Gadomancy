package makeo.gadomancy.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;
import java.util.UUID;

/**
 * HellFirePvP@Admin
 * Date: 17.06.2015 / 13:59
 * on SoulSorcery
 * MiscUtils
 */
public final class MiscUtils {

    private MiscUtils() {}

    public static String getNumberSuffix(int number) {
        int digit = number % 10;
        switch (digit) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
        }
        return "th";
    }

    public static EntityPlayer getOnlinePlayerByUUID(UUID playerUUID) {
        List<EntityPlayerMP> players = Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().playerEntityList;
        for(EntityPlayerMP player : players) {
            if(player.getGameProfile().getId().equals(playerUUID)) {
                return player;
            }
        }
        return null;
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
        return new Vector3(e.posX, e.posY, e.posZ);
    }

}
