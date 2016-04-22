package makeo.gadomancy.common.network.packets;

import makeo.gadomancy.client.fx.FXLightningBolt;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    /**
     * 0: purple
     * 1: yellow
     * 2: dark blue
     * 3: green
     * 4: red
     * 5: dark purple
     * 6: blue
     */
    private int type;
    private boolean mightGetLong;

    public PacketTCNodeBolt() {}

    public PacketTCNodeBolt(float x, float y, float z, float targetX, float targetY, float targetZ, int type, boolean mightGetLong) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.type = type;
        this.mightGetLong = mightGetLong;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.targetX = buf.readFloat();
        this.targetY = buf.readFloat();
        this.targetZ = buf.readFloat();
        this.type = buf.readInt();
        this.mightGetLong = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        buf.writeFloat(targetX);
        buf.writeFloat(targetY);
        buf.writeFloat(targetZ);
        buf.writeInt(type);
        buf.writeBoolean(mightGetLong);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketTCNodeBolt p, MessageContext ctx) {
        FXLightningBolt bolt = new FXLightningBolt(Minecraft.getMinecraft().theWorld, p.x, p.y, p.z, p.targetX, p.targetY, p.targetZ, Minecraft.getMinecraft().theWorld.rand.nextLong(), 10, 4.0F, 5);
        bolt.defaultFractal();
        bolt.setType(p.type);
        bolt.finalizeBolt();
        return null;
    }
}
