package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.Thaumcraft;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 25.10.2015 19:42
 */
public class PacketTCNodeBolt implements IMessage, IMessageHandler<PacketTCNodeBolt, IMessage> {

    private float x, y, z;
    private float targetX, targetY, targetZ;

    public PacketTCNodeBolt() {}

    public PacketTCNodeBolt(float x, float y, float z, float targetX, float targetY, float targetZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.targetX = buf.readFloat();
        this.targetY = buf.readFloat();
        this.targetZ = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeFloat(targetX);
        buf.writeFloat(targetY);
        buf.writeFloat(targetZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketTCNodeBolt p, MessageContext ctx) {
        Thaumcraft.proxy.nodeBolt(Minecraft.getMinecraft().theWorld, p.x, p.y, p.z, p.targetX, p.targetY, p.targetZ);
        return null;
    }
}
