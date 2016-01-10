package makeo.gadomancy.common.aura;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketTCNotificationText;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 29.11.2015 18:14
 */
public class AuraResearchManager {

    public static final String TC_AURA_RESEARCH_STR = "GADOMANCY.AURA.%s";

    public static void tryUnlockAuraEffect(EntityPlayer player, Aspect aspect) {
        if(!AuraEffectHandler.registeredEffects.containsKey(aspect)) return;
        if(isBlacklisted(aspect)) return;

        if(!ResearchManager.isResearchComplete(player.getCommandSenderName(), Gadomancy.MODID.toUpperCase() + ".AURA_EFFECTS")) return;
        if(!Thaumcraft.proxy.getPlayerKnowledge().hasDiscoveredAspect(player.getCommandSenderName(), aspect)) return;

        String res = String.format(TC_AURA_RESEARCH_STR, aspect.getTag());
        if(ResearchManager.isResearchComplete(player.getCommandSenderName(), res)) return;
        Thaumcraft.proxy.getResearchManager().completeResearch(player, res);

        PacketTCNotificationText text = new PacketTCNotificationText("gadomancy.aura.research.unlock", aspect.getTag());
        PacketHandler.INSTANCE.sendTo(text, (EntityPlayerMP) player);
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
        List<String> lines = new ArrayList<String>();
        for(Aspect a : AuraEffectHandler.registeredEffects.keySet()) {
            if(ResearchManager.isResearchComplete(player.getCommandSenderName(), String.format(TC_AURA_RESEARCH_STR, a.getTag()))) {
                if(!isBlacklisted(a)) lines.add(a.getTag());
            }
        }
        return lines;
    }

    public static boolean isBlacklisted(Aspect a) {
        for(String aspectTag : ModConfig.blacklistAuraEffects) {
            if(aspectTag == null) continue;
            if(aspectTag.equals(a.getTag())) return true;
        }
        return false;
    }

    public static void registerAuraResearches() {
        for(Aspect a : AuraEffectHandler.registeredEffects.keySet()) {
            new ResearchItem(String.format(TC_AURA_RESEARCH_STR, a.getTag()), Gadomancy.MODID).registerResearchItem();
        }
    }
}
