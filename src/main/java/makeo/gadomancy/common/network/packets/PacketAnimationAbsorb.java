package makeo.gadomancy.common.network.packets;

import makeo.gadomancy.client.util.MultiTickEffectDispatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.targetX = buf.readInt();
        this.targetY = buf.readInt();
        this.targetZ = buf.readInt();
        this.tickCap = buf.readInt();
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
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketAnimationAbsorb p, MessageContext ctx) {
        IBlockState state = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(p.targetX, p.targetY, p.targetZ));
        Block b = state.getBlock();
        int md = b.getMetaFromState(state);
        //Block b = Minecraft.getMinecraft().theWorld.getBlock(p.targetX, p.targetY, p.targetZ);
        //int md = Minecraft.getMinecraft().theWorld.getBlockMetadata(p.targetX, p.targetY, p.targetZ);
        MultiTickEffectDispatcher.VortexDigInfo info =
                new MultiTickEffectDispatcher.VortexDigInfo(Minecraft.getMinecraft().theWorld.provider.getDimensionId(), p.x, p.y, p.z, p.targetX, p.targetY, p.targetZ, b, md, p.tickCap);
        MultiTickEffectDispatcher.registerVortexDig(info);
        return null;
    }
}
