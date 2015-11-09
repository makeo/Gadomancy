package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.utils.world.fake.FakeWorld;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EnumGolemType;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 25.10.2015 15:03
 */
public class PacketTransformationInfo implements IMessage, IMessageHandler<PacketTransformationInfo, IMessage> {
    private static final World WORLD = new FakeWorld();

    private EntityGolemBase golem = null;

    public PacketTransformationInfo(EntityGolemBase golem) {
        this.golem = golem;
    }

    public PacketTransformationInfo() { }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.golem = (EntityGolemBase) EntityList.createEntityFromNBT(ByteBufUtils.readTag(buf), WORLD);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound compound = new NBTTagCompound();
        golem.writeEntityToNBT(compound);
        ByteBufUtils.writeTag(buf, compound);
    }

    public static class Request implements IMessage, IMessageHandler<Request, PacketTransformationInfo> {
        private int player;

        public Request(EntityPlayer player) {
            this.player = player.getEntityId();
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            player = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(player);
        }

        @Override
        public PacketTransformationInfo onMessage(Request message, MessageContext ctx) {
            //TODO: send transformation info
            EntityPlayer sender = ctx.getServerHandler().playerEntity;
            EntityPlayer player = (EntityPlayer) sender.worldObj.getEntityByID(this.player);

            //get from custom data
            /*if(player != null) {

            }*/

            return new PacketTransformationInfo(new EntityGolemBase(WORLD, EnumGolemType.CLAY, true));
        }
    }

    @Override
    public IMessage onMessage(PacketTransformationInfo message, MessageContext ctx) {
        //handle stuff

        return null;
    }
}
