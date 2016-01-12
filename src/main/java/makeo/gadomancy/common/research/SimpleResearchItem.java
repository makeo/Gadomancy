package makeo.gadomancy.common.research;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 15.06.2015 19:05
 */
public class SimpleResearchItem extends ResearchItem {

    private static final String PREFIX = Gadomancy.MODID.toUpperCase() + ".";

    public SimpleResearchItem(String key, int col, int row, int complex, ResourceLocation icon, AspectList tags) {
        super(PREFIX + key, Gadomancy.MODID, tags, col, row, complex, icon);
    }

    public SimpleResearchItem(String key, int col, int row, int complex, ItemStack icon, AspectList tags) {
        super(PREFIX + key, Gadomancy.MODID, tags, col, row, complex, icon);
    }

    public static String getFullName(String name) {
        return PREFIX + name;
    }

}
