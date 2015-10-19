package makeo.gadomancy.common.entities.fake;

import com.mojang.authlib.GameProfile;
import makeo.gadomancy.common.Gadomancy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 19.09.2015 17:14
 */
public class AdvancedFakePlayer extends FakePlayer {
    protected static final UUID DEFAULT_UUID = UUID.fromString("d36a4fa5-c70e-4358-8418-9ed683687d0a");

    public AdvancedFakePlayer(WorldServer world, UUID uuid, String name) {
        super(world, new GameProfile(uuid, name));

        this.playerNetServerHandler = new FakeNetServerHandler(MinecraftServer.getServer(), this);
    }

    public AdvancedFakePlayer(WorldServer world, UUID uuid) {
        this(world, uuid, "[" + Gadomancy.MODID + ":" + uuid.hashCode() + "]");
    }

    public AdvancedFakePlayer(WorldServer world) {
        this(world, DEFAULT_UUID);
    }

    public void setHeldItem(ItemStack item) {
        inventory.currentItem = 0;
        inventory.mainInventory[0] = item;
    }

    public static boolean isFakePlayer(EntityPlayer player) {
        if(!player.worldObj.isRemote) {
            if(player.getClass() != EntityPlayerMP.class) {
                return true;
            }
            EntityPlayerMP mp = (EntityPlayerMP) player;

            if(mp.playerNetServerHandler == null
                    || !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player)
                    || mp.getPlayerIP() == null || mp.getPlayerIP().trim().isEmpty()) {
                return true;
            }

            try {
                mp.playerNetServerHandler.netManager.getSocketAddress().toString();
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
}
