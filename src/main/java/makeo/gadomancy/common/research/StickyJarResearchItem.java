package makeo.gadomancy.common.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 20.12.2015 01:01
 */
public class StickyJarResearchItem extends AlternatingResearchItem {
    public StickyJarResearchItem(String key, int col, int row, int complex, AspectList tags) {
        super(key, col, row, complex, tags, new ItemStack(ConfigBlocks.blockJar));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSpecial() {
        if(Gadomancy.proxy.getSide() == Side.CLIENT) {
            List<ItemStack> items = RegisteredItems.getStickyJarStacks(Minecraft.getMinecraft().thePlayer);
            if(items.size() > 0) {
                itemIcons = items;
            }
        }
        return super.isSpecial();
    }

}
