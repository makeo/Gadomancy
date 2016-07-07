package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.CommonProxy;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 07.07.2016 / 21:46
 */
public class PacketUpdateOnlineState implements IMessage, IMessageHandler<PacketUpdateOnlineState, IMessage> {

    private boolean state;

    public PacketUpdateOnlineState() {}

    public PacketUpdateOnlineState(boolean state) {
        this.state = state;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        state = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(state);
    }

    @Override
    public IMessage onMessage(PacketUpdateOnlineState message, MessageContext ctx) {
        CommonProxy.serverOnlineState = message.state;
        return null;
    }

}
