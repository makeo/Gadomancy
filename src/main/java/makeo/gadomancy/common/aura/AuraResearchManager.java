package makeo.gadomancy.common.aura;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketSyncAuraKnowledge;
import makeo.gadomancy.common.network.packets.PacketTCNotificationText;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 29.11.2015 18:14
 */
public class AuraResearchManager {

    private static Map<UUID, List<String>> auraMap = new HashMap<UUID, List<String>>();

    public static void tryUnlockAuraEffect(EntityPlayer player, Aspect aspect) {
        UUID playerUUID = player.getUniqueID();
        if(!AuraEffectHandler.registeredEffects.containsKey(aspect)) return;
        if(!auraMap.containsKey(playerUUID)) {
            auraMap.put(playerUUID, new ArrayList<String>());
        }
        if(auraMap.get(playerUUID).contains(aspect.getTag())) return;
        auraMap.get(playerUUID).add(aspect.getTag());

        PacketTCNotificationText text = new PacketTCNotificationText("gadomancy.aura.research.unlock", aspect.getTag());
        PacketHandler.INSTANCE.sendTo(text, (EntityPlayerMP) player);

        PacketSyncAuraKnowledge sync = new PacketSyncAuraKnowledge(auraMap.get(playerUUID));
        PacketHandler.INSTANCE.sendTo(sync, (EntityPlayerMP) player);
    }

    //Side: CLIENT!!!
    public static void recieveServerData(List<String> aspectTags) {
        UUID ourPlayer = Minecraft.getMinecraft().thePlayer.getUniqueID();
        auraMap.put(ourPlayer, aspectTags);
    }

    public static List<String> getLines(String aspectTag) {
        List<String> lines = new ArrayList<String>();
        String base = "gadomancy.aura.effect." + aspectTag;
        if(StatCollector.canTranslate(base)) {
            lines.add(StatCollector.translateToLocal(base));
            return lines;
        }
        int count = 0;
        while(StatCollector.canTranslate(base + "_" + count)) {
            lines.add(StatCollector.translateToLocal(base + "_" + count));
            count++;
        }
        return lines;
    }

    public static List<String> getKnowledge(EntityPlayer player) {
        return getKnowledge(player.getUniqueID());
    }

    public static List<String> getKnowledge(UUID playerUUID) {
        return auraMap.get(playerUUID);
    }

    public static void loadDataFromFile(String playerName, UUID uniqueID, File auraData) {
        if(!auraData.exists()) {
            auraMap.put(uniqueID, new ArrayList<String>());
            return;
        }
        NBTTagCompound compound;
        try {
            compound = CompressedStreamTools.read(auraData);
        } catch (IOException e) {
            Gadomancy.log.warn("Unable to load aura data for " + uniqueID + "/" + playerName);
            auraMap.put(uniqueID, new ArrayList<String>());
            return;
        }

        List<String> tags = new ArrayList<String>();
        NBTTagList aspects = compound.getTagList("aspects", new NBTTagString().getId());
        for (int i = 0; i < aspects.tagCount(); i++) {
            String aspect = aspects.getStringTagAt(i);
            if(Aspect.getAspect(aspect) != null) {
                tags.add(aspect);
            }
        }
        auraMap.put(uniqueID, tags);
    }

    public static void saveDataToFile(String commandSenderName, UUID uniqueID, File auraData) {
        if(!auraMap.containsKey(uniqueID)) return;
        if(!auraData.exists()) {
            try {
                auraData.createNewFile();
            } catch (IOException e) {
                Gadomancy.log.error("Could not create aura data for " + uniqueID + "/" + commandSenderName);
                return;
            }
        }

        List<String> tags = auraMap.get(uniqueID);
        NBTTagList list = new NBTTagList();
        for(String s : tags) {
            list.appendTag(new NBTTagString(s));
        }
        NBTTagCompound cmp = new NBTTagCompound();
        cmp.setTag("aspects", list);
        try {
            CompressedStreamTools.write(cmp, auraData);
        } catch (IOException e) {
            Gadomancy.log.error("Could not write aura data for " + uniqueID + "/" + commandSenderName);
            return;
        }
    }

}
