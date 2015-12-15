package makeo.gadomancy.common.utils.world;

import cpw.mods.fml.common.FMLCommonHandler;
import makeo.gadomancy.common.data.config.ModConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 05.11.2015 14:01
 */
public class WorldUtil {

    public static void teleportToFakeOuter(EntityPlayerMP player) {
        MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(player.worldObj.provider.dimensionId != 0) {
            mServer.getConfigurationManager().transferPlayerToDimension(player, 0, new TeleporterNothing(mServer.worldServerForDimension(0)));
        }
        mServer.getConfigurationManager().transferPlayerToDimension(player, ModConfig.dimOuterId, new TeleporterNothing(mServer.worldServerForDimension(ModConfig.dimOuterId)));
    }

    public static void tryTeleportBack(EntityPlayerMP player, int dim) {
        MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        mServer.getConfigurationManager().transferPlayerToDimension(player, dim, new TeleporterNothing(mServer.worldServerForDimension(dim)));
    }

}
