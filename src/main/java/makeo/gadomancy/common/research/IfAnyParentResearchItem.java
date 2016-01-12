package makeo.gadomancy.common.research;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchBrowser;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 03.01.2016 14:35
 */
public class IfAnyParentResearchItem extends SimpleResearchItem {

    private String[] anyParents = null;

    public IfAnyParentResearchItem(String key, int col, int row, int complex, ItemStack icon, AspectList tags) {
        super(key, col, row, complex, icon, tags);
    }

    public IfAnyParentResearchItem(String key, int col, int row, int complex, ResourceLocation icon, AspectList tags) {
        super(key, col, row, complex, icon, tags);
    }

    @Override
    public boolean isConcealed() {
        if(anyParents != null) {
            for(String res : anyParents) {
                ResearchItem ri = ResearchCategories.getResearch(res);
                if(ri != null) {
                    if(GuiResearchBrowser.completedResearch.get(Minecraft.getMinecraft().thePlayer.getCommandSenderName()).contains(ri.key)) {
                        return super.isConcealed();
                    }
                }
            }
            return true;
        }
        return super.isConcealed();
    }

    public IfAnyParentResearchItem setAnyParents(String... parents) {
        this.anyParents = parents;
        return this;
    }

}
