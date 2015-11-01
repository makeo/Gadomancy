package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.util.FamiliarHandlerClient;

import java.util.ArrayList;
import java.util.List;

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

        public float targetX, targetY, targetZ;
        public String owner;

        public PacketFamiliarBolt() {}

        public PacketFamiliarBolt(String whoseFamiliar, float targetX, float targetY, float targetZ) {
            this.targetX = targetX;
            this.targetY = targetY;
            this.targetZ = targetZ;
            this.owner = whoseFamiliar;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.targetX = buf.readFloat();
            this.targetY = buf.readFloat();
            this.targetZ = buf.readFloat();

            int length = buf.readInt();
            byte[] strBytes = new byte[length];
            buf.readBytes(strBytes, 0, length);
            owner = new String(strBytes);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeFloat(targetX);
            buf.writeFloat(targetY);
            buf.writeFloat(targetZ);

            byte[] str = owner.getBytes();
            buf.writeInt(str.length);
            buf.writeBytes(str);
        }

        @Override
        public IMessage onMessage(PacketFamiliarBolt message, MessageContext ctx) {
            FamiliarHandlerClient.processPacket(message);
            return null;
        }
    }

    public static class PacketFamiliarSyncCompletely extends PacketFamiliar implements IMessage, IMessageHandler<PacketFamiliarSyncCompletely, IMessage> {

        public List<String> playerNamesWithFamiliars = new ArrayList<String>();

        public PacketFamiliarSyncCompletely() {}

        public PacketFamiliarSyncCompletely(List<String> playerNamesWithFamiliars) {
            this.playerNamesWithFamiliars = playerNamesWithFamiliars;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int cnt = buf.readInt();
            for (int i = 0; i < cnt; i++) {
                int length = buf.readInt();
                byte[] strBytes = new byte[length];
                buf.readBytes(strBytes, 0, length);
                playerNamesWithFamiliars.add(new String(strBytes));
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(playerNamesWithFamiliars.size());
            for(String str : playerNamesWithFamiliars) {
                byte[] strBytes = str.getBytes();
                buf.writeInt(strBytes.length);
                buf.writeBytes(strBytes);
            }
        }

        @Override
        public IMessage onMessage(PacketFamiliarSyncCompletely message, MessageContext ctx) {
            FamiliarHandlerClient.processPacket(message);
            return null;
        }
    }

    public static class PacketFamiliarSyncSingle extends PacketFamiliar implements IMessage, IMessageHandler<PacketFamiliarSyncSingle, IMessage> {

        //True=add, False=remove
        public boolean status;
        public String name;

        public PacketFamiliarSyncSingle() {}

        public PacketFamiliarSyncSingle(boolean status, String name) {
            this.status = status;
            this.name = name;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            status = buf.readBoolean();
            int length = buf.readInt();
            byte[] strBytes = new byte[length];
            buf.readBytes(strBytes, 0, length);
            name = new String(strBytes);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBoolean(status);
            byte[] str = name.getBytes();
            buf.writeInt(str.length);
            buf.writeBytes(str);
        }

        @Override
        public IMessage onMessage(PacketFamiliarSyncSingle message, MessageContext ctx) {
            FamiliarHandlerClient.processPacket(message);
            return null;
        }
    }
}