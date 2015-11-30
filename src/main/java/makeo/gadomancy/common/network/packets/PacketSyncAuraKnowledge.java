package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.aura.AuraResearchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 30.11.2015 00:00
 */
public class PacketSyncAuraKnowledge implements IMessage, IMessageHandler<PacketSyncAuraKnowledge, IMessage> {

    private List<String> aspectTags = new ArrayList<String>();

    public PacketSyncAuraKnowledge() {}

    public PacketSyncAuraKnowledge(List<String> aspectTags) {
        this.aspectTags = aspectTags;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int it = buf.readInt();

        for (int i = 0; i < it; i++) {
            int length = buf.readInt();
            byte[] strBytes = new byte[length];
            buf.readBytes(strBytes, 0, length);
            aspectTags.add(new String(strBytes));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(aspectTags.size());

        for(String tag : aspectTags) {
            byte[] str = tag.getBytes();
            buf.writeInt(str.length);
            buf.writeBytes(str);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncAuraKnowledge message, MessageContext ctx) {
        AuraResearchManager.recieveServerData(message.aspectTags);
        return null;
    }
}
