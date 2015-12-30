package makeo.gadomancy.common.crafting;

import makeo.gadomancy.common.items.baubles.ItemFamiliar_Old;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 29.12.2015 15:29
 */
public class FamiliarUndoRecipe extends ShapelessRecipes {

    public FamiliarUndoRecipe() {
        super(new ItemStack(RegisteredItems.itemPackage, 1, 2).setStackDisplayName(EnumChatFormatting.GOLD + "Used Familiar Items"), new ArrayList() {
            {
                add(new ItemStack(RegisteredItems.itemFamiliar_old));
            }
        });
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {

        boolean foundOldFamiliar = false;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if(itemstack != null && itemstack.getItem() != null && itemstack.getItem() instanceof ItemFamiliar_Old) {
                    if(foundOldFamiliar) return false;
                    foundOldFamiliar = true;
                } else if(itemstack != null && itemstack.getItem() != null) {
                    return false;
                }
            }
        }

        return foundOldFamiliar;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if(itemstack != null && itemstack.getItem() != null && itemstack.getItem() instanceof ItemFamiliar_Old) {
                    return ItemFamiliar_Old.getPaybackPackage(itemstack);
                }
            }
        }

        return null;
    }
}
