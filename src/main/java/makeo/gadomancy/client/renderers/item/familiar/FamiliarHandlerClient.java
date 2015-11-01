package makeo.gadomancy.client.renderers.item.familiar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.familiar.FamiliarHandlerServer;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

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

    @SideOnly(Side.CLIENT)
    public void processPacket(PacketFamiliar packet) {
        if(packet instanceof PacketFamiliar.PacketFamiliarBolt) {
            PacketFamiliar.PacketFamiliarBolt pkt = (PacketFamiliar.PacketFamiliarBolt) packet;
            EntityPlayer p = Minecraft.getMinecraft().thePlayer;
            Thaumcraft.proxy.nodeBolt(Minecraft.getMinecraft().theWorld, (float) p.posX, (float) (p.posY + p.eyeHeight), (float) p.posZ, pkt.targetX, pkt.targetY, pkt.targetZ);
            //TODO zap given coordinates from current familiar render coordinates
        }
    }
}
