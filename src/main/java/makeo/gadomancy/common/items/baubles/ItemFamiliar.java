package makeo.gadomancy.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public BaubleType getBaubleType(ItemStack paramItemStack) {
        return BaubleType.AMULET;
    }

    //Called both server- and clientside!

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar) {
            Gadomancy.proxy.familiarHandler.equippedTick(((EntityPlayer) entity).worldObj, (EntityPlayer) entity);
        }
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar) {
            Gadomancy.proxy.familiarHandler.notifyEquip(((EntityPlayer) entity).worldObj, (EntityPlayer) entity);
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

}
