package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.transformation.TransformationHelper;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 03.07.2015 21:42
 */
public class PacketAbortTransform implements IMessage, IMessageHandler<PacketAbortTransform, IMessage> {
    private int entityId;
    private byte reason;

    public PacketAbortTransform() {}
    public PacketAbortTransform(AbortReason reason) {
        this.reason = (byte) reason.ordinal();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        reason = buf.readByte();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(reason);
    }

    public static enum AbortReason {
        PLAYER_EXIT, NO_VIS, FOREIGN_TRANSFORMATION, TRANSFORMATION_FAILED, DAMAGE, MOVE
    }

    @Override
    public IMessage onMessage(PacketAbortTransform message, MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            if(ctx.getServerHandler().playerEntity.getEntityId() == entityId) {

            }
        } else {
            TransformationHelper.onAbortTransformation(entityId);
        }
        return null;
    }
}
