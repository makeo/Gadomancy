package makeo.gadomancy.client.renderers.item.familiar;

import makeo.gadomancy.common.familiar.FamiliarHandlerServer;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 12:13
 */
public class FamiliarHandlerClient extends FamiliarHandlerServer {

    //TODO has to handle rendering and processing of animation packets.

    @Override
    public void setupPostInit() {
        super.setupPostInit();


    }

    @Override
    public void notifyEquip(World world, ItemStack familiarStack, EntityPlayer player) {
        super.notifyEquip(world, familiarStack, player);


    }

    @Override
    public void notifyUnequip(World world, ItemStack familiarStack, EntityPlayer player) {
        super.notifyUnequip(world, familiarStack, player);


    }

    @Override
    public void equippedTick(World world, ItemStack familiarStack, EntityPlayer player) {
        super.equippedTick(world, familiarStack, player);


    }

    public void processPacket(PacketFamiliar packet) {
        if(packet instanceof PacketFamiliar.PacketFamiliarBolt) {
            //TODO zap given coordinates from current familiar render coordinates
        }
    }
}
