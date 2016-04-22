package makeo.gadomancy.common.network.packets;

import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.utils.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 29.11.2015 18:40
 */
public class PacketTCNotificationText implements IMessage, IMessageHandler<PacketTCNotificationText, IMessage> {

    private String text, additionalInfo;
    private int color;

    public PacketTCNotificationText() {}

    //Color is not chat color! the color is supposedly the image color..
    public PacketTCNotificationText(String text, Color color, String additionalInfo) {
        this(text, color.getRGB(), additionalInfo);
    }

    public PacketTCNotificationText(String text, int color, String additionalInfo) {
        this.text = text;
        this.additionalInfo = additionalInfo;
        this.color = color;
    }

    public PacketTCNotificationText(String text, String additionalInfo) {
        this(text, 0, additionalInfo);
    }

    public PacketTCNotificationText(String text) {
        this(text, "");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        color = buf.readInt();

        text = StringHelper.readFromBuffer(buf);

        additionalInfo = StringHelper.readFromBuffer(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(color);

        StringHelper.writeToBuffer(buf, text);

        if(additionalInfo == null) additionalInfo = "";

        StringHelper.writeToBuffer(buf, additionalInfo);
    }

    @Override
    public IMessage onMessage(PacketTCNotificationText message, MessageContext ctx) {
        if(message.text == null) return null;
        String translated = StatCollector.translateToLocal(message.text);
        ResourceLocation image = null;
        int color = message.color;
        Aspect a = Aspect.getAspect(message.additionalInfo);
        if(a != null) {
            image = a.getImage();
            color = a.getColor();
        }
        if(message.text.equals("gadomancy.aura.research.unlock")) {
            if(a != null) {
                translated = EnumChatFormatting.GREEN + String.format(translated, a.getName());
            } else {
                translated = EnumChatFormatting.GREEN + String.format(translated, message.additionalInfo);
            }
        }
        color &= 0xFFFFFF;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(translated)); //TODO thaumcraft plz?
        //PlayerNotifications.addNotification(translated, image, color);
        return null;
    }
}
