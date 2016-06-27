package makeo.gadomancy.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.DataFamiliar;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.familiar.FamiliarAugment;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.12.2015 23:09
 */
public class ItemEtherealFamiliar extends Item implements IBauble {

    public ItemEtherealFamiliar() {
        setUnlocalizedName("ItemEtherealFamiliar");
        setMaxStackSize(1);
        setFull3D();
        setCreativeTab(RegisteredItems.creativeTab);

        setTextureName(Gadomancy.MODID + ":category_icon");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stack = new ItemStack(item);
        setFamiliarAspect(stack, Aspect.MAGIC);
        list.add(stack);

        stack = new ItemStack(item);
        setFamiliarAspect(stack, Aspect.FIRE);
        addAugmentUnsafe(stack, FamiliarAugment.FIRE, 3);
        addAugmentUnsafe(stack, FamiliarAugment.DAMAGE_INCREASE, 3);
        list.add(stack);

        stack = new ItemStack(item);
        setFamiliarAspect(stack, Aspect.WEATHER);
        addAugmentUnsafe(stack, FamiliarAugment.SHOCK, 3);
        addAugmentUnsafe(stack, FamiliarAugment.ATTACK_SPEED, 2);
        list.add(stack);

        stack = new ItemStack(item);
        setFamiliarAspect(stack, Aspect.POISON);
        addAugmentUnsafe(stack, FamiliarAugment.POISON, 3);
        addAugmentUnsafe(stack, FamiliarAugment.ATTACK_SPEED, 2);
        list.add(stack);

        stack = new ItemStack(item);
        setFamiliarAspect(stack, Aspect.WEAPON);
        addAugmentUnsafe(stack, FamiliarAugment.WEAKNESS, 3);
        addAugmentUnsafe(stack, FamiliarAugment.RANGE_INCREASE, 2);
        list.add(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore, boolean flag) {
        List<String> newLore = new ArrayList<String>();

        if(hasFamiliarAspect(stack)) {
            newLore.add(EnumChatFormatting.GRAY + getFamiliarAspect(stack).getName());
        }

        List<FamiliarAugment.FamiliarAugmentPair> augments = getAugments(stack);
        for (FamiliarAugment.FamiliarAugmentPair pair : augments) {
            newLore.add(EnumChatFormatting.GRAY + pair.augment.getLocalizedName() + " " + MiscUtils.toRomanNumeral(pair.level));
        }


        if(Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
            lore.addAll(newLore);
        } else {
            if(!newLore.isEmpty()) lore.add(EnumChatFormatting.DARK_GRAY + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("gadomancy.lore.hasAdditionalLore"));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.AMULET;
    }

    public static boolean hasFamiliarAspect(ItemStack stack) {
        return getFamiliarAspect(stack) != null;
    }

    public static void setFamiliarAspect(ItemStack stack, Aspect aspect) {
        if(stack == null || !(stack.getItem() instanceof ItemEtherealFamiliar)) return;
        NBTHelper.getData(stack).setString("aspect", aspect.getTag());
    }

    public static Aspect getFamiliarAspect(ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof ItemEtherealFamiliar)) return null;
        String tag = NBTHelper.getData(stack).getString("aspect");
        if(tag == null || tag.equalsIgnoreCase("")) return null;
        return Aspect.getAspect(tag);
    }

    public static boolean hasAugment(ItemStack stack, FamiliarAugment augment) {
        List<FamiliarAugment.FamiliarAugmentPair> pairs = getAugments(stack);
        for (FamiliarAugment.FamiliarAugmentPair pair : pairs) {
            if(pair.augment.equals(augment)) return true;
        }
        return false;
    }

    public static FamiliarAugment.FamiliarAugmentList getAugments(ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof ItemEtherealFamiliar)) return new FamiliarAugment.FamiliarAugmentList();
        NBTTagCompound augmentTag = NBTHelper.getData(stack).getCompoundTag("augments");
        Set<String> strKeySet = augmentTag.func_150296_c();
        if(strKeySet == null || strKeySet.size() <= 0) return new FamiliarAugment.FamiliarAugmentList();
        FamiliarAugment.FamiliarAugmentList augmentList = new FamiliarAugment.FamiliarAugmentList();
        for(String key : strKeySet) {
            FamiliarAugment augment = FamiliarAugment.getByUnlocalizedName(key);
            int level = augmentTag.getInteger(key);
            augmentList.add(new FamiliarAugment.FamiliarAugmentPair(augment, level));
        }
        return augmentList;
    }

    /*public static boolean doesAcceptAugment(ItemStack stack, FamiliarAugment augment, int level) {
        if(stack == null || !(stack.getItem() instanceof ItemEtherealFamiliar)) return false;
        FamiliarAugment.FamiliarAugmentList currentAugments = getAugments(stack);
        for(FamiliarAugment.FamiliarAugmentPair pair : currentAugments) {
            if(pair.augment.equals(augment)) return false;
        }
        return augment.checkConditions(currentAugments, level);
    }*/

    public static boolean incrementAugmentLevel(ItemStack stack, FamiliarAugment toAdd) {
        if(!hasAugment(stack, toAdd)) {
            return addAugmentUnsafe(stack, toAdd, 1);
        } else {
            int current = getAugments(stack).getLevel(toAdd);
            return addAugmentUnsafe(stack, toAdd, current + 1);
        }
    }

    /*public static boolean addAugment(ItemStack stack, FamiliarAugment augment, int level) {
        if(stack == null || !(stack.getItem() instanceof ItemEtherealFamiliar) || level <= 0) return false;
        return doesAcceptAugment(stack, augment, level) && addAugmentUnsafe(stack, augment, level);
    }*/

    public static boolean addAugmentUnsafe(ItemStack stack, FamiliarAugment augment, int level) {
        if(stack == null || !(stack.getItem() instanceof ItemEtherealFamiliar) || level <= 0) return false;
        NBTTagCompound tag = NBTHelper.getData(stack);

        NBTTagCompound augmentTag = NBTHelper.getData(stack).getCompoundTag("augments");
        augmentTag.setInteger(augment.getUnlocalizedName(), level);

        tag.setTag("augments", augmentTag);
        return true;
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {
        if(itemStack == null) return;
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemEtherealFamiliar) {
            DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
            Aspect a = getFamiliarAspect(itemStack);
            if (a != null) {
                familiarData.equipTick(((EntityPlayer) entity).worldObj, (EntityPlayer) entity, a);
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {
        if(itemStack == null) return;
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemEtherealFamiliar) {
            DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
            Aspect a = getFamiliarAspect(itemStack);
            if(a != null) {
                familiarData.handleEquip(((EntityPlayer) entity).worldObj, (EntityPlayer) entity, a);
            }
        }
    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entity) {
        if(itemStack == null) return;
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemEtherealFamiliar) {
            DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
            Aspect a = getFamiliarAspect(itemStack);
            if(a != null) {
                familiarData.handleUnequip(((EntityPlayer) entity).worldObj, (EntityPlayer) entity, a);
            }
        }
    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        if(!(entityLivingBase instanceof EntityPlayer)) return false;
        return ResearchManager.isResearchComplete(entityLivingBase.getCommandSenderName(), Gadomancy.MODID.toUpperCase() + ".ETHEREAL_FAMILIAR");
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

}
