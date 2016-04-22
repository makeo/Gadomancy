package makeo.gadomancy.common.crafting;

import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.research.ResearchHelper;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 19:21
 */
public class RecipeStickyJar implements IArcaneRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return false;
        //return ResearchHelper.isResearchComplete(player.getName(), SimpleResearchItem.PREFIX + "STICKYJAR") && getJarItem(inventoryCrafting) != null;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world, EntityPlayer player) {
        return ResearchHelper.isResearchComplete(player.getName(), SimpleResearchItem.PREFIX + "STICKYJAR") && getJarItem(inventoryCrafting) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack jarItem = getJarItem(inventoryCrafting);
        if(jarItem != null) {
            ItemStack result = jarItem.copy();
            result.stackSize = 1;
            NBTHelper.getData(result).setBoolean("isStickyJar", true);

            return result;
        }

        return null;
    }

    private ItemStack getJarItem(IInventory inv) {
        int itemCount = 0;
        int invWidth = (int) Math.sqrt(inv.getSizeInventory());

        ItemStack jarItem = null;

        for(int i = 0; i < invWidth*invWidth; i++) {
            ItemStack current = inv.getStackInSlot(i);
            if(current != null) {
                itemCount++;
                if(itemCount > 2) {
                    return null;
                }

                if(jarItem == null) {
                    if(current.getItem() == Items.slime_ball) {
                        return null;
                    }

                    boolean isSticky = RegisteredItems.isStickyableJar(current) && (!current.hasTagCompound()
                            || !current.getTagCompound().getBoolean("isStickyJar"));
                    if(current.getItem() != Items.slime_ball && !isSticky) {
                        return null;
                    }


                    if(isSticky) {
                        int slimeSlot = i + invWidth;

                        if(slimeSlot < 0 || slimeSlot > inv.getSizeInventory()) {
                            return null;
                        }

                        ItemStack slime = inv.getStackInSlot(slimeSlot);
                        if(slime == null || slime.getItem() != Items.slime_ball) {
                            return null;
                        }

                        jarItem = current;
                    }
                }
            }
        }
        return jarItem;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        return new ItemStack[0];
    }

    @Override
    public AspectList getAspects() {
        return new AspectList().add(Aspect.WATER, 10).add(Aspect.EARTH, 10);
    }

    @Override
    public AspectList getAspects(InventoryCrafting inventoryCrafting) {
        return null;
    }

    @Override
    public String[] getResearch() {
        return new String[] { SimpleResearchItem.PREFIX + "STICKYJAR" };
    }
}
