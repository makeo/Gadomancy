package makeo.gadomancy.common.crafting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;

/**
 * HellFirePvP@Admin
 * Date: 21.04.2016 / 21:00
 * on Gadomancy_1_8
 * InfusionEnchRecipe
 */
public class InfusionEnchRecipe extends InfusionRecipe {

    private ItemStack matchTrial;

    public InfusionEnchRecipe(String research, Enchantment output, int inst, AspectList aspects2, Object[] recipe) {
        super(research, output, inst, aspects2, null, recipe);
    }

    @Override
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        this.matchTrial = central;
        boolean matches = super.matches(input, central, world, player);
        this.matchTrial = null;
        return matches;
    }

    @Override
    public Object getRecipeInput() {
        if(matchTrial == null) {
            return new ItemStack(Blocks.air);
        } else {
            return matchTrial;
        }
    }
}
