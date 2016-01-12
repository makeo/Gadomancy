package makeo.gadomancy.common.aura;

import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 29.11.2015 23:57
 */
public class ResearchPageAuraAspects extends ResearchPage {

    private static final ResourceLocation transparent = new SimpleResourceLocation("items/transparent.png");

    private ResearchPageAuraAspects(AspectList as) {
        super(as);
    }

    public static ResearchPage[] createAllAuraPagesFor(EntityPlayer player) {
        List<ResearchPage> pages = new ArrayList<ResearchPage>();
        int additionalPages = ResearchPageAuraAspects.getAdditionalPagesFor(player);
        for (int i = 0; i < additionalPages; i++) {
            List<String> tags = ResearchPageAuraAspects.getAspectTagsForPageIndex(player, i);
            AspectList list = ResearchPageAuraAspects.createPageAspectList(tags);
            pages.add(new ResearchPageAuraAspects(list));
        }
        ResearchPage[] t = new ResearchPage[1];
        return pages.toArray(t);
    }

    public static int getAdditionalPagesFor(EntityPlayer player) {
        List<String> knowledge = AuraResearchManager.getKnowledge(player);
        if(knowledge.size() == 0) return 0;
        return ((knowledge.size() & 3) == 0) ? (knowledge.size() >> 2) : ((knowledge.size() >> 2) + 1);
    }

    public static List<String> getAspectTagsForPageIndex(EntityPlayer player, int pageIndex) {
        List<String> knowledge = AuraResearchManager.getKnowledge(player);
        if(knowledge.size() <= 0) return new ArrayList<String>();
        Collections.sort(knowledge);
        int startIndex = pageIndex << 2;
        if(knowledge.size() <= startIndex) return new ArrayList<String>(); //Should not happen, because getAdditionalPages should only return indexes within range
        return knowledge.subList(startIndex, (startIndex + 4 > knowledge.size()) ? knowledge.size() : startIndex + 4);
    }

    public static AspectList createPageAspectList(List<String> tagList) {
        AspectList list = new AspectList();
        for(String tag : tagList) {
            Aspect a = createAuraFakeAspect(tag);
            if(a != null) list.add(a, 0);
        }
        return list;
    }

    private static Aspect createAuraFakeAspect(String tag) {
        Aspect original = Aspect.getAspect(tag);
        if(original == null) return null;
        String fakeTemp = "GADOMANCY_TEMP_" + tag;

        Aspect fake = new FakeAspect(fakeTemp, original.getColor(), null, original.getImage(), original.getBlend(), true);
        fake.setTag(original.getTag());
        return fake;
    }

    private static class FakeAspect extends Aspect {

        private boolean headAspect;

        public FakeAspect(String tag, int color, Aspect[] components, ResourceLocation image, int blend, boolean isHead) {
            super(tag, color, components, image, blend);
            Aspect.aspects.remove(tag);
            this.headAspect = isHead;
        }

        @Override
        public String getName() {
            if(headAspect) return super.getName();
            String tag = super.getTag();
            if(tag == null || tag.isEmpty() || tag.equals("")) return "";
            return StatCollector.translateToLocal(tag);
        }
    }

}
