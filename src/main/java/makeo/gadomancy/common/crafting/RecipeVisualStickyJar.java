package makeo.gadomancy.common.crafting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 19.12.2015 23:08
 */
public class RecipeVisualStickyJar extends ShapedArcaneRecipe {
    public RecipeVisualStickyJar() {
        super("Mirror mirror on the wall, who is the fairest one of all?", new ItemStack(Blocks.dirt), new AspectList().add(Aspect.WATER, 10).add(Aspect.EARTH, 10), "   ", " A ", "   ", 'A', new ItemStack(Items.diamond));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AspectList getAspects() {
        updateVisualStickyJarRecipes();
        return super.getAspects();
    }

    @SideOnly(Side.CLIENT)
    private void updateVisualStickyJarRecipes() {
        List<ItemStack> stacks = RegisteredItems.getStickyJarStacks(Minecraft.getMinecraft().thePlayer);
        if(stacks.size() > 0) {
            ItemStack stack = stacks.get((int)(System.currentTimeMillis() / 1000L % stacks.size()));
            output = stack.copy();
            NBTHelper.getData(output).setBoolean("isStickyJar", true);

            input = new Object[9];
            input[4] = stack;
            input[7] = new ItemStack(Items.slime_ball, 1, 0);
        }
    }
}
