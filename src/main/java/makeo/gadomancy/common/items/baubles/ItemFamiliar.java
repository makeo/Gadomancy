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
import net.minecraft.util.EnumChatFormatting;
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

        //Shut up FML.
        setTextureName(Gadomancy.MODID + ":category_icon");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stack = new ItemStack(item);
        setAspect(stack, Aspect.MAGIC);
        list.add(stack);

        stack = new ItemStack(item);
        setAspect(stack, Aspect.MAGIC);
        for(FamiliarUpgrade upgrade : FamiliarUpgrade.values()) {
            addUpgrade(stack, upgrade);
        }
        list.add(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore, boolean par4Flag) {
        if(stack.hasTagCompound()) {
            if(hasAspect(stack)) {
                lore.add(getAspect(stack).getName());
            }

            if(hasUpgrade(stack, FamiliarUpgrade.ATTACK_1) || hasUpgrade(stack, FamiliarUpgrade.ATTACK_2) || hasUpgrade(stack, FamiliarUpgrade.ATTACK_3)) {
                String strength = EnumChatFormatting.DARK_RED + "Strength ";
                if(hasUpgrade(stack, FamiliarUpgrade.ATTACK_1)) strength += "I";
                if(hasUpgrade(stack, FamiliarUpgrade.ATTACK_2)) strength += "I";
                if(hasUpgrade(stack, FamiliarUpgrade.ATTACK_3)) strength += "I";
                lore.add(strength);
            }

            if(hasUpgrade(stack, FamiliarUpgrade.RANGE_1)) {
                lore.add(EnumChatFormatting.GOLD + "Reach I");
            }

            if(hasUpgrade(stack, FamiliarUpgrade.COOLDOWN_1)) {
                lore.add(EnumChatFormatting.AQUA + "Swiftness I");
            }
        }
        super.addInformation(stack, player, lore, par4Flag);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public BaubleType getBaubleType(ItemStack paramItemStack) {
        return BaubleType.AMULET;
    }

    public Aspect getAspect(ItemStack stack) {
        if(!hasAspect(stack)) return null;
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar)) return null;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("aspect")) return null;
        return Aspect.getAspect(stack.getTagCompound().getString("aspect"));
    }

    public boolean hasAspect(ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar)) return false;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("aspect")) return false;
        return Aspect.getAspect(stack.getTagCompound().getString("aspect")) != null;
    }

    public void setAspect(ItemStack stack, Aspect aspect) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar)) return;
        NBTHelper.getData(stack).setString("aspect", aspect.getTag());
    }

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

        int range = 0;

        if(hasUpgrade(itemStack, FamiliarUpgrade.RANGE_1)) {
            range += 3;
        }
        return range;
    }

    public int getAttackCooldownReduction(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar)) return 0;
        return hasUpgrade(itemStack, FamiliarUpgrade.COOLDOWN_1) ? 10 : 0;
    }

    public float getAttackStrength(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar)) return 0F;

        float attack = 1F;

        if(hasUpgrade(itemStack, FamiliarUpgrade.ATTACK_1)) {
            attack += 1F;
        }
        if(hasUpgrade(itemStack, FamiliarUpgrade.ATTACK_2)) {
            attack += 2F;
        }
        if(hasUpgrade(itemStack, FamiliarUpgrade.ATTACK_3)) {
            attack += 4F;
        }
        return attack;
    }

    public boolean hasUpgrade(ItemStack stack, FamiliarUpgrade upgrade) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar)) return false;
        if(upgrade == null) return true;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey(upgrade.getTag())) return false;
        return stack.getTagCompound().getBoolean(upgrade.getTag());
    }

    public void addUpgrade(ItemStack stack, FamiliarUpgrade upgrade) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar)) return;
        NBTHelper.getData(stack).setBoolean(upgrade.getTag(), true);
    }

    public static enum FamiliarUpgrade {

        ATTACK_1("AttackUpgrade1"),
        ATTACK_2("AttackUpgrade2", ATTACK_1),
        ATTACK_3("AttackUpgrade3", ATTACK_2),
        COOLDOWN_1("CdUpgrade1"),
        RANGE_1("RangeUpgrade1");

        private String tag;
        private FamiliarUpgrade neededUpgrade;

        private FamiliarUpgrade(String tagStr) {
            this(tagStr, null);
        }

        private FamiliarUpgrade(String tagStr, FamiliarUpgrade neededUpgrade) {
            this.tag = tagStr;
            this.neededUpgrade = neededUpgrade;
        }

        public String getTag() {
            return tag;
        }

        public FamiliarUpgrade getNeededPreviousUpgrade() {
            return neededUpgrade;
        }
    }

}
