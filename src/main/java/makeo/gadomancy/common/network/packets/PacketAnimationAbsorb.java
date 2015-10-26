package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.util.MultiTickEffectDispatcher;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import thaumcraft.common.Thaumcraft;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 25.10.2015 18:04
 */
public class PacketAnimationAbsorb implements IMessage, IMessageHandler<PacketAnimationAbsorb, IMessage> {

    private double x, y, z;
    private int targetX, targetY, targetZ;

    public PacketAnimationAbsorb() {}

    public PacketAnimationAbsorb(double x, double y, double z, int targetX, int targetY, int targetZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.targetX = buf.readInt();
        this.targetY = buf.readInt();
        this.targetZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(targetX);
        buf.writeInt(targetY);
        buf.writeInt(targetZ);
    }

    @Override
    public IMessage onMessage(PacketAnimationAbsorb p, MessageContext ctx) {
        Block b = Minecraft.getMinecraft().theWorld.getBlock(p.targetX, p.targetY, p.targetZ);
        int md = Minecraft.getMinecraft().theWorld.getBlockMetadata(p.targetX, p.targetY, p.targetZ);
        MultiTickEffectDispatcher.VortexDigInfo info =
                new MultiTickEffectDispatcher.VortexDigInfo(Minecraft.getMinecraft().theWorld.provider.dimensionId, (int) p.x, (int) p.y, (int) p.z, p.targetX, p.targetY, p.targetZ, b, md);
        MultiTickEffectDispatcher.registerVortexDig(info);
        return null;
    }
}
