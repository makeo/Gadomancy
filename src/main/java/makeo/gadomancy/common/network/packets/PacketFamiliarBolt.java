package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.utils.StringHelper;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 31.10.2015 14:36
 */
public class PacketFamiliarBolt implements IMessage, IMessageHandler<PacketFamiliarBolt, IMessage> {

    public float targetX, targetY, targetZ;
    public int type;
    public boolean mightGetLong;
    public String owner;

    public PacketFamiliarBolt() {}

    public PacketFamiliarBolt(String whoseFamiliar, float targetX, float targetY, float targetZ, int type, boolean mightGetLong) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.owner = whoseFamiliar;
        this.type = type;
        this.mightGetLong = mightGetLong;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.targetX = buf.readFloat();
        this.targetY = buf.readFloat();
        this.targetZ = buf.readFloat();

        this.type = buf.readInt();
        this.mightGetLong = buf.readBoolean();

        owner = StringHelper.readFromBuffer(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(targetX);
        buf.writeFloat(targetY);
        buf.writeFloat(targetZ);

        buf.writeInt(type);
        buf.writeBoolean(mightGetLong);

        StringHelper.writeToBuffer(buf, owner);
    }

    @Override
    public IMessage onMessage(PacketFamiliarBolt message, MessageContext ctx) {
        FamiliarHandlerClient.processBoltPacket(message);
        return null;
    }
}