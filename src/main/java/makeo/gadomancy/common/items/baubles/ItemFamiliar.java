package makeo.gadomancy.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 11:38
 */
public class ItemFamiliar extends Item implements IBauble {

    public ItemFamiliar() {
        setUnlocalizedName("ItemFamiliar");
        setMaxStackSize(1);
        setFull3D();
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stack = new ItemStack(item);
        NBTHelper.getData(stack).setString("aspect", Aspect.MAGIC.getName());
        list.add(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public BaubleType getBaubleType(ItemStack paramItemStack) {
        return BaubleType.AMULET;
    }

    //Called both server- and clientside!

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar) {
            Gadomancy.proxy.familiarHandler.equippedTick(((EntityPlayer) entity).worldObj, itemStack, (EntityPlayer) entity);
        }
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar) {
            Gadomancy.proxy.familiarHandler.notifyEquip(((EntityPlayer) entity).worldObj, itemStack, (EntityPlayer) entity);
        }
    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar) {
            Gadomancy.proxy.familiarHandler.notifyUnequip(((EntityPlayer) entity).worldObj, (EntityPlayer) entity);
        }
    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    //Infusion stuff.

    public int getAttackRangeIncrease(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar)) return 0;

        int attack = 0;

        if(NBTHelper.getData(itemStack).getBoolean("RangeUpgrade1")) {
            attack += 3;
        }
        return attack;
    }

    public int getAttackCooldownReduction(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar)) return 0;
        return NBTHelper.getData(itemStack).getBoolean("CdUpgrade1") ? 10 : 0;
    }

    public float getAttackStrength(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar)) return 0F;

        float attack = 1F;

        if(NBTHelper.getData(itemStack).getBoolean("AttackUpgrade1")) {
            attack += 1F;
        }
        if(NBTHelper.getData(itemStack).getBoolean("AttackUpgrade2")) {
            attack += 2F;
        }
        if(NBTHelper.getData(itemStack).getBoolean("AttackUpgrade3")) {
            attack += 4F;
        }
        return attack;
    }
}
