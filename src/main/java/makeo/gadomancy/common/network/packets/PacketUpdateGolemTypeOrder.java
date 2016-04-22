package makeo.gadomancy.common.network.packets;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 30.07.2015 18:07
 */
public class PacketUpdateGolemTypeOrder /*implements IMessage, IMessageHandler<PacketUpdateGolemTypeOrder, IMessage>*/ {
    /*private Map<String, Integer> mapping;

    TODO ugh.

    public PacketUpdateGolemTypeOrder() {}
    public PacketUpdateGolemTypeOrder(Map<String, Integer> mapping) {
        this.mapping = mapping;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] data = new byte[buf.readInt()];
        buf.readBytes(data);
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        try {
            mapping = (Map<String, Integer>) new ObjectInputStream(in).readObject();
            in.close();
        } catch (Exception ignored) { }//IOException | ClassNotFoundException ignored
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(out).writeObject(mapping);
        } catch (IOException ignored) { }

        byte[] data = out.toByteArray();

        buf.writeInt(data.length);
        buf.writeBytes(data);

        try {
            out.close();
        } catch (IOException ignored) { }
    }

    @Override
    public IMessage onMessage(PacketUpdateGolemTypeOrder message, MessageContext ctx) {
        if(message.mapping != null) {
            GolemEnumHelper.reorderEnum(message.mapping);
        }
        return null;
    }*/
}
