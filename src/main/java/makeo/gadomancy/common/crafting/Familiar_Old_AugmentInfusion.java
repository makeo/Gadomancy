package makeo.gadomancy.common.crafting;

import makeo.gadomancy.common.items.baubles.ItemFamiliar_Old;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 02.11.2015 17:39
 */
public class Familiar_Old_AugmentInfusion extends InfusionRecipe {

    private ItemFamiliar_Old.FamiliarUpgrade upgradeToAdd;

    public Familiar_Old_AugmentInfusion(String research, int inst, AspectList aspects2, ItemStack input, ItemFamiliar_Old.FamiliarUpgrade upgradeToAdd, ItemStack[] recipe) {
        super(research, null, inst, aspects2, input, recipe);
        this.upgradeToAdd = upgradeToAdd;
    }

    @Override
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        if(central == null || !(central.getItem() instanceof ItemFamiliar_Old)) return false; //We call it "FamiliarAugment" Recipe for a reason..
        if(getRecipeInput() == null || !(getRecipeInput().getItem() instanceof ItemFamiliar_Old)) return false; //A bit late but still working..

        if ((this.research.length() > 0) && (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), this.research))) {
            return false;
        }

        ItemStack centralCopy = central.copy();
        if(!((ItemFamiliar_Old) centralCopy.getItem()).hasUpgrade(centralCopy, getUpgradeToAdd().getNeededPreviousUpgrade())) return false;

        ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
        for (ItemStack is : input) {
            ii.add(is.copy());
        }
        for (ItemStack comp : getComponents()) {
            boolean b = false;
            for (int a = 0; a < ii.size(); a++) {
                centralCopy = ii.get(a).copy();
                if (comp.getItemDamage() == 32767) {
                    centralCopy.setItemDamage(32767);
                }
                if (areItemStacksEqual(centralCopy, comp, true)) {
                    ii.remove(a);
                    b = true;
                    break;
                }
            }
            if (!b) {
                return false;
            }
        }
        return ii.size() == 0;
    }

    @Override
    public Object getRecipeOutput(ItemStack input) {
        ItemStack inputCopy = input.copy();
        ((ItemFamiliar_Old) input.getItem()).addUpgrade(inputCopy, getUpgradeToAdd());
        return inputCopy;
    }

    public ItemFamiliar_Old.FamiliarUpgrade getUpgradeToAdd() {
        return upgradeToAdd;
    }
}
