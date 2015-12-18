package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.AbstractData;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.utils.StringHelper;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 13.12.2015 15:54
 */
public class PacketSyncData implements IMessage, IMessageHandler<PacketSyncData, IMessage> {

    private Map<String, AbstractData> data = new HashMap<String, AbstractData>();
    private boolean shouldSyncAll = false;

    public PacketSyncData() {}

    public PacketSyncData(Map<String, AbstractData> dataToSend, boolean shouldSyncAll) {
        this.data = dataToSend;
        this.shouldSyncAll = shouldSyncAll;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            String key = StringHelper.readFromBuffer(buf);

            byte providerId = buf.readByte();
            AbstractData.AbstractDataProvider<? extends AbstractData> provider = AbstractData.Registry.getProvider(providerId);
            if(provider == null) {
                Gadomancy.log.warn("Provider for ID " + providerId + " doesn't exist! Skipping...");
                continue;
            }

            NBTTagCompound cmp;
            short compoundLength = buf.readShort();
            byte[] abyte = new byte[compoundLength];
            buf.readBytes(abyte);
            try {
                cmp = CompressedStreamTools.func_152457_a(abyte, new NBTSizeTracker(2097152L));
            } catch (IOException e) {
                Gadomancy.log.warn("Provider Compound of " + providerId + " threw an IOException! Skipping...");
                Gadomancy.log.warn("Exception message: " + e.getMessage());
                continue;
            }

            AbstractData dat = provider.provideNewInstance();
            dat.readRawFromPacket(cmp);

            data.put(key, dat);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(data.size());

        for(String key : data.keySet()) {
            AbstractData dat = data.get(key);
            NBTTagCompound cmp = new NBTTagCompound();
            if(shouldSyncAll) {
                dat.writeAllDataToPacket(cmp);
            } else {
                dat.writeToPacket(cmp);
            }

            StringHelper.writeToBuffer(buf, key);

            byte providerId = dat.getProviderID();
            buf.writeByte(providerId);

            byte[] abyte;
            try {
                abyte = CompressedStreamTools.compress(cmp);
            } catch (IOException e) {
                Gadomancy.log.warn("Compressing the NBTTagCompound of " + providerId + " threw an IOException! Skipping...");
                Gadomancy.log.warn("Exception message: " + e.getMessage());
                continue;
            }
            buf.writeShort((short) abyte.length);
            buf.writeBytes(abyte);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncData message, MessageContext ctx) {
        SyncDataHolder.receiveServerPacket(message.data);
        return null;
    }

}
