package makeo.gadomancy.common.crafting;

import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.items.ItemWispEssence;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 29.12.2015 15:03
 */
public class DeprecationItemPaybacks {

    public static void addFamiliarRangePayback(List<ItemStack> contents, ItemStack oldFamiliarStack) {
        Aspect aspect = RegisteredItems.itemFamiliar_old.getAspect(oldFamiliarStack);
        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack wispyEssence = null;
        if(aspect != null) {
            wispyEssence = new ItemStack(ConfigItems.itemWispEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));
        }

        List<ItemStack> reward = new ArrayList<ItemStack>();
        reward.add(new ItemStack(ConfigItems.itemResource, 2, 17));
        reward.add(new ItemStack(ConfigItems.itemResource, 2, 0));

        if(wispyEssence != null) {
            ItemStack add = wispyEssence.copy();
            add.stackSize = 4;
            reward.add(add);
        }

        int auram = 44;
        int magic = 51;
        int other = 37;
        ItemStack phials = getPhialStack(Aspect.AURA, auram);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.MAGIC, magic);
        if(phials != null) reward.add(phials);
        if(aspect != null) {
            phials = getPhialStack(aspect, other);
            if(phials != null) reward.add(phials);
        }

        ItemStack craftingPackage = new ItemStack(RegisteredItems.itemPackage, 1, 1);
        RegisteredItems.itemPackage.setContents(craftingPackage, reward);
        contents.add(craftingPackage);
    }

    public static void addFamiliarCooldownPayback(List<ItemStack> contents, ItemStack oldFamiliarStack) {
        Aspect aspect = RegisteredItems.itemFamiliar_old.getAspect(oldFamiliarStack);
        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack wispyEssence = null;
        if(aspect != null) {
            wispyEssence = new ItemStack(ConfigItems.itemWispEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));
        }

        List<ItemStack> reward = new ArrayList<ItemStack>();
        reward.add(new ItemStack(ConfigItems.itemEldritchObject, 1, 3));
        reward.add(new ItemStack(ConfigItems.itemFocusPrimal));
        reward.add(new ItemStack(ConfigItems.itemFocusPrimal));
        reward.add(new ItemStack(ConfigItems.itemBathSalts, 2, 0));

        if(wispyEssence != null) {
            ItemStack add = wispyEssence.copy();
            add.stackSize = 2;
            reward.add(add);
        }

        int auram = 79;
        int magic = 101;
        int permutatio = 54;
        int other = 81;
        ItemStack phials = getPhialStack(Aspect.AURA, auram);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.EXCHANGE, permutatio);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.MAGIC, magic);
        if(phials != null) reward.add(phials);
        if(aspect != null) {
            phials = getPhialStack(aspect, other);
            if(phials != null) reward.add(phials);
        }

        ItemStack craftingPackage = new ItemStack(RegisteredItems.itemPackage, 1, 1);
        RegisteredItems.itemPackage.setContents(craftingPackage, reward);
        contents.add(craftingPackage);
    }

    public static void addFamiliarAttack3Payback(List<ItemStack> contents, ItemStack oldFamiliarStack) {
        Aspect aspect = RegisteredItems.itemFamiliar_old.getAspect(oldFamiliarStack);
        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack wispyEssence = null;
        if(aspect != null) {
            wispyEssence = new ItemStack(ConfigItems.itemWispEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));
        }

        List<ItemStack> reward = new ArrayList<ItemStack>();
        reward.add(new ItemStack(ConfigItems.itemEldritchObject, 1, 3));
        reward.add(new ItemStack(Items.nether_star));
        reward.add(new ItemStack(ConfigBlocks.blockCustomPlant, 2, 4));

        if(wispyEssence != null) {
            ItemStack add = wispyEssence.copy();
            add.stackSize = 4;
            reward.add(add);
        }

        int auram = 77;
        int magic = 93;
        int telum = 49;
        int other = 104;
        ItemStack phials = getPhialStack(Aspect.AURA, auram);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.WEAPON, telum);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.MAGIC, magic);
        if(phials != null) reward.add(phials);
        if(aspect != null) {
            phials = getPhialStack(aspect, other);
            if(phials != null) reward.add(phials);
        }

        ItemStack craftingPackage = new ItemStack(RegisteredItems.itemPackage, 1, 1);
        RegisteredItems.itemPackage.setContents(craftingPackage, reward);
        contents.add(craftingPackage);
    }

    public static void addFamiliarAttack2Payback(List<ItemStack> contents, ItemStack oldFamiliarStack) {
        Aspect aspect = RegisteredItems.itemFamiliar_old.getAspect(oldFamiliarStack);
        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack wispyEssence = null;
        if(aspect != null) {
            wispyEssence = new ItemStack(ConfigItems.itemWispEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));
        }

        List<ItemStack> reward = new ArrayList<ItemStack>();
        reward.add(new ItemStack(ConfigBlocks.blockCrystal, 4, 6));
        reward.add(new ItemStack(ConfigItems.itemResource, 2, 15));
        reward.add(new ItemStack(ConfigItems.itemResource, 2, 17));
        if(wispyEssence != null) {
            ItemStack add = wispyEssence.copy();
            add.stackSize = 4;
            reward.add(add);
        }

        int auram = 43;
        int magic = 71;
        int other = 59;
        int eldritch = 37;
        ItemStack phials = getPhialStack(Aspect.AURA, auram);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.ELDRITCH, eldritch);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.MAGIC, magic);
        if(phials != null) reward.add(phials);
        if(aspect != null) {
            phials = getPhialStack(aspect, other);
            if(phials != null) reward.add(phials);
        }
        ItemStack craftingPackage = new ItemStack(RegisteredItems.itemPackage, 1, 1);
        RegisteredItems.itemPackage.setContents(craftingPackage, reward);
        contents.add(craftingPackage);
    }

    public static void addFamiliarAttack1Payback(List<ItemStack> contents, ItemStack oldFamiliarStack) {
        Aspect aspect = RegisteredItems.itemFamiliar_old.getAspect(oldFamiliarStack);
        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack wispyEssence = null;
        if(aspect != null) {
            wispyEssence = new ItemStack(ConfigItems.itemWispEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));
        }

        List<ItemStack> reward = new ArrayList<ItemStack>();
        reward.add(new ItemStack(ConfigItems.itemResource, 2, 4));
        reward.add(new ItemStack(ConfigItems.itemResource, 2, 6));
        if(wispyEssence != null) {
            ItemStack add = wispyEssence.copy();
            add.stackSize = 4;
            reward.add(add);
        }
        int auram = 37;
        int magic = 53;
        int other = 35;
        ItemStack phials = getPhialStack(Aspect.AURA, auram);
        if(phials != null) reward.add(phials);
        phials = getPhialStack(Aspect.MAGIC, magic);
        if(phials != null) reward.add(phials);
        if(aspect != null) {
            phials = getPhialStack(aspect, other);
            if(phials != null) reward.add(phials);
        }
        ItemStack craftingPackage = new ItemStack(RegisteredItems.itemPackage, 1, 1);
        RegisteredItems.itemPackage.setContents(craftingPackage, reward);
        contents.add(craftingPackage);
    }

    public static void addFamiliarCraftingPaybackStack(List<ItemStack> outputList, ItemStack oldFamiliar) {
        Aspect aspect = RegisteredItems.itemFamiliar_old.getAspect(oldFamiliar);
        ItemWispEssence itemEssence = (ItemWispEssence) ConfigItems.itemWispEssence;
        ItemStack wispyEssence = null;
        if(aspect != null) {
            wispyEssence = new ItemStack(ConfigItems.itemWispEssence, 1, 0);
            itemEssence.setAspects(wispyEssence, new AspectList().add(aspect, 2));
        }

        List<ItemStack> rewardCrafting = new ArrayList<ItemStack>();
        rewardCrafting.add(new ItemStack(ConfigItems.itemResource, 2, 15));
        rewardCrafting.add(new ItemStack(ConfigBlocks.blockCrystal, 2, 6));
        rewardCrafting.add(new ItemStack(ConfigItems.itemResource, 2, 14));
        rewardCrafting.add(new ItemStack(ConfigItems.itemResource, 2, 1));
        rewardCrafting.add(new ItemStack(ConfigItems.itemAmuletRunic, 1, 0));
        if(wispyEssence != null) {
            ItemStack add = wispyEssence.copy();
            add.stackSize = 4;
            rewardCrafting.add(add);
        }
        int auram = 34;
        int magic = 51;
        int otherAspect = 46;
        ItemStack phials = getPhialStack(Aspect.AURA, auram);
        if(phials != null) rewardCrafting.add(phials);
        phials = getPhialStack(Aspect.MAGIC, magic);
        if(phials != null) rewardCrafting.add(phials);
        if(aspect != null) {
            phials = getPhialStack(aspect, otherAspect);
            if(phials != null) rewardCrafting.add(phials);
        }
        ItemStack craftingPackage = new ItemStack(RegisteredItems.itemPackage, 1, 1);
        RegisteredItems.itemPackage.setContents(craftingPackage, rewardCrafting);
        outputList.add(craftingPackage);
    }

    private static ItemStack getPhialStack(Aspect aspect, int aspectAmount) {
        ItemEssence phial = (ItemEssence) ConfigItems.itemEssence;
        ItemStack phialStack = new ItemStack(phial, getPhialAmount(aspectAmount), 1);
        if(phialStack.stackSize > 0) {
            phial.setAspects(phialStack, new AspectList().add(aspect, 8));
            return phialStack;
        }
        return null;
    }

    private static int getPhialAmount(int aspectAmount) {
        return (aspectAmount / 8) + (aspectAmount % 8 == 0 ? 0 : 1);
    }
}
