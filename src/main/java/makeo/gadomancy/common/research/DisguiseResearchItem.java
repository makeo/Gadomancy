package makeo.gadomancy.common.research;

import makeo.gadomancy.common.crafting.InfusionVisualDisguiseArmor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 08.01.2016 22:48
 */
public class DisguiseResearchItem extends AlternatingResearchItem {
    public DisguiseResearchItem(String key, int col, int row, int complex, AspectList tags) {
        super(key, col, row, complex, tags, new ItemStack(Items.bread));
    }

    private InfusionVisualDisguiseArmor recipe;

    @Override
    protected ItemStack getIcon() {
        return recipe == null ? new ItemStack(Items.cookie) : recipe.getRecipeInput();
    }

    @Override
    public ResearchItem setPages(ResearchPage... pages) {
        for(ResearchPage page : pages) {
            if(page.type == ResearchPage.PageType.INFUSION_CRAFTING
                    && page.recipe instanceof InfusionVisualDisguiseArmor) {
                recipe = (InfusionVisualDisguiseArmor) page.recipe;
            }
        }
        return super.setPages(pages);
    }
}
