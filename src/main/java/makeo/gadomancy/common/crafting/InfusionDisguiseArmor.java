package makeo.gadomancy.common.crafting;

import makeo.gadomancy.common.research.SimpleResearchItem;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 25.12.2015 15:35
 */
public class InfusionDisguiseArmor extends InfusionRunicAugmentRecipe {

    //TODO change back to original?
    public static final ItemStack[] COMPONENTS = new ItemStack[] {new ItemStack(Items.slime_ball), new ItemStack(ItemsTC.quicksilver), new ItemStack(Items.slime_ball), new ItemStack(ItemsTC.quicksilver)};
    public static final AspectList ASPECTS = new AspectList().add(Aspect.ORDER, 12).add(Aspect.PROTECT, 10).add(Aspect.AURA, 4);

    private Map<ItemStack, List<ItemStack>> cachedItems = new HashMap<ItemStack, List<ItemStack>>();

    @Override
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        if(input.size() != COMPONENTS.length + 1 || !ResearchManager.isResearchComplete(player.getName(), SimpleResearchItem.PREFIX + "STICKYJAR")) {
            return false;
        }

        List<ItemStack> copy = (List<ItemStack>) input.clone();
        List<ItemStack> result = new ArrayList<ItemStack>(copy.size());
        for(ItemStack required : COMPONENTS) {
            boolean contains = false;
            for(int i = 0; i < copy.size(); i++) {
                if(ThaumcraftApiHelper.areItemsEqual(required, copy.get(i))) {
                    contains = true;
                    result.add(copy.get(i));
                    copy.remove(i);
                    break;
                }
            }
            if(!contains) {
                return false;
            }
        }

        ItemStack disguise = copy.get(0);
        if(!isValidDisguise(central, disguise)) {
            return false;
        }

        result.add(0, disguise);
        cachedItems.put(central, result);
        return true;
    }

    @Override
    public ItemStack[] getComponents(ItemStack input) {
        List<ItemStack> components = cachedItems.get(input);
        if(components != null) {
            return components.toArray(new ItemStack[components.size()]);
        }
        return new ItemStack[0];
    }

    @Override
    public AspectList getAspects(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
        cachedItems.remove(input);
        return ASPECTS;
    }

    @Override
    public int getInstability(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
        return -20;
    }

    @Override
    public Object getRecipeOutput(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
        List<ItemStack> components = cachedItems.get(input);
        if(components != null) {
            return disguiseStack(input, components.get(0));
        }
        return input;
    }

    public static ItemStack disguiseStack(ItemStack stack, ItemStack disguise) {
        ItemStack out = stack.copy();
        NBTTagCompound compound = NBTHelper.getPersistentData(out);
        if(disguise.getItem() == Items.potionitem) {
            compound.setBoolean("disguise", true);
        } else {
            compound.setTag("disguise", disguise.writeToNBT(new NBTTagCompound()));
        }
        return out;
    }

    private boolean isValidDisguise(ItemStack armor, ItemStack disguise) {
        if(isDisguised(armor) || isDisguised(disguise)) {
            return false;
        }

        int armorPos = EntityLiving.getArmorPosition(armor);
        return armorPos != 0 && (isInvisItem(disguise) || EntityLiving.getArmorPosition(disguise) == armorPos);
    }

    private boolean isDisguised(ItemStack stack) {
        return NBTHelper.hasPersistentData(stack) && NBTHelper.getPersistentData(stack).hasKey("disguise");
    }

    private boolean isInvisItem(ItemStack disguise) {
        if(disguise.getItem() == Items.potionitem) {
            List effects = Items.potionitem.getEffects(disguise);
            for(Object obj : effects) {
                if(obj instanceof PotionEffect) {
                    if(((PotionEffect)obj).getPotionID() == Potion.invisibility.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
