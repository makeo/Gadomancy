package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.renderers.item.familiar.FamiliarHandlerClient;
import makeo.gadomancy.common.Gadomancy;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 31.10.2015 14:36
 */
public abstract class PacketFamiliar {

    public static class PacketFamiliarBolt extends PacketFamiliar implements IMessage, IMessageHandler<PacketFamiliarBolt, IMessage> {

        private float targetX, targetY, targetZ;

        public PacketFamiliarBolt() {}

        public PacketFamiliarBolt(float targetX, float targetY, float targetZ) {
            this.targetX = targetX;
            this.targetY = targetY;
            this.targetZ = targetZ;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.targetX = buf.readFloat();
            this.targetY = buf.readFloat();
            this.targetZ = buf.readFloat();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeFloat(targetX);
            buf.writeFloat(targetY);
            buf.writeFloat(targetZ);
        }

        @Override
        public IMessage onMessage(PacketFamiliarBolt packet, MessageContext ctx) {
            ((FamiliarHandlerClient) Gadomancy.proxy.familiarHandler).processPacket(packet);
            return null;
        }
    }
}