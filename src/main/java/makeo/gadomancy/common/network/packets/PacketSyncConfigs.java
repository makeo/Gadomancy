package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.data.config.Sync;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 15.11.2015 21:10
 */
public class PacketSyncConfigs implements IMessage, IMessageHandler<PacketSyncConfigs, IMessage> {
    private Tuple[] fieldData;

    @Override
    public void fromBytes(ByteBuf buf) {
        int count = buf.readByte();
        fieldData = new Tuple[count];

        for(int i = 0; i < count; i++) {
            byte[] data = new byte[buf.readShort()];
            buf.readBytes(data);

            ByteArrayInputStream bain = new ByteArrayInputStream(data);
            Tuple tuple = null;
            try {
                tuple = new Tuple();
                tuple.field = new DataInputStream(bain).readUTF();
                tuple.value = new ObjectInputStream(bain).readObject();
            } catch (Exception ignored) {
            }

            if(tuple == null) {
                fieldData = null;
                break;
            }
            fieldData[i] = tuple;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        List<byte[]> fieldData = new ArrayList<byte[]>();

        for(Field field : ModConfig.class.getFields()) {
            if(Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Sync.class)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    new DataOutputStream(baos).writeUTF(field.getName());
                    new ObjectOutputStream(baos).writeObject(field.get(null));
                } catch (Exception ignored) {
                }
                fieldData.add(baos.toByteArray());

                try {
                    baos.close();
                } catch (IOException ignored) {
                }
            }
        }

        buf.writeByte(fieldData.size());
        for(byte[] data : fieldData) {
            buf.writeShort(data.length);
            buf.writeBytes(data);
        }
    }

    @Override
    public IMessage onMessage(PacketSyncConfigs message, MessageContext ctx) {
        try {
            for(Tuple tuple : message.fieldData) {
                Field field = ModConfig.class.getField(tuple.field);
                field.set(null, tuple.value);
            }
        } catch (Throwable ignored) {
            throw new RuntimeException(ignored);
        }

        return null;
    }

    private static class Tuple {
        private String field;
        private Object value;
    }
}