package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.util.MultiTickEffectDispatcher;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 25.10.2015 18:04
 */
public class PacketAnimationAbsorb implements IMessage, IMessageHandler<PacketAnimationAbsorb, IMessage> {

    private int x, y, z;
    private int targetX, targetY, targetZ;
    private int tickCap;
    private int bid = -1, bmd = -1;

    public PacketAnimationAbsorb() {}

    public PacketAnimationAbsorb(int x, int y, int z, int targetX, int targetY, int targetZ, int tickCap) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.tickCap = tickCap;
    }

    public PacketAnimationAbsorb(int x, int y, int z, int targetX, int targetY, int targetZ, int tickCap, int bid, int bmd) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.tickCap = tickCap;
        this.bid = bid;
        this.bmd = bmd;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.targetX = buf.readInt();
        this.targetY = buf.readInt();
        this.targetZ = buf.readInt();
        this.tickCap = buf.readInt();
        this.bid = buf.readInt();
        this.bmd = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(targetX);
        buf.writeInt(targetY);
        buf.writeInt(targetZ);
        buf.writeInt(tickCap);
        buf.writeInt(bid);
        buf.writeInt(bmd);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketAnimationAbsorb p, MessageContext ctx) {
        Block b;
        int md;
        if(p.bid != -1 && p.bmd != -1) {
            b = Block.getBlockById(p.bid);
            md = p.bmd;
        } else {
            b = Minecraft.getMinecraft().theWorld.getBlock(p.targetX, p.targetY, p.targetZ);
            md = Minecraft.getMinecraft().theWorld.getBlockMetadata(p.targetX, p.targetY, p.targetZ);
        }
        MultiTickEffectDispatcher.VortexDigInfo info =
                new MultiTickEffectDispatcher.VortexDigInfo(Minecraft.getMinecraft().theWorld.provider.dimensionId, p.x, p.y, p.z, p.targetX, p.targetY, p.targetZ, b, md, p.tickCap);
        MultiTickEffectDispatcher.registerVortexDig(info);
        return null;
    }
}
