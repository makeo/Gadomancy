package makeo.gadomancy.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.DataFamiliar;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.crafting.DeprecationItemPaybacks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 11:38
 */
public class ItemFamiliar_Old extends Item implements IBauble {

    public ItemFamiliar_Old() {
        setUnlocalizedName("ItemFamiliar");
        setMaxStackSize(1);
        setFull3D();
        setCreativeTab(RegisteredItems.creativeTab);

        //Shut up FML.
        setTextureName(Gadomancy.MODID + ":category_icon");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {}

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lore, boolean flag) {
        lore.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gadomancy.lore.deprecation.back"));

        if(stack.hasTagCompound()) {
            if(hasAspect(stack)) {
                lore.add(getAspect(stack).getName());
            }

            if(Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
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
            } else {
                if(hasAnyUpgrade(stack)) {
                    lore.add(EnumChatFormatting.DARK_GRAY + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("gadomancy.lore.hasAdditionalLore"));
                }
            }
        }
        super.addInformation(stack, player, lore, flag);
    }

    public boolean hasAnyUpgrade(ItemStack stack) {
        for(FamiliarUpgrade upgrade : FamiliarUpgrade.values()) {
            if(hasUpgrade(stack, upgrade)) return true;
        }
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.common;
    }

    @Override
    public BaubleType getBaubleType(ItemStack paramItemStack) {
        return BaubleType.AMULET;
    }

    public Aspect getAspect(ItemStack stack) {
        if(!hasAspect(stack)) return null;
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar_Old)) return null;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("aspect")) return null;
        return Aspect.getAspect(stack.getTagCompound().getString("aspect"));
    }

    public boolean hasAspect(ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar_Old)) return false;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("aspect")) return false;
        return Aspect.getAspect(stack.getTagCompound().getString("aspect")) != null;
    }

    public void setAspect(ItemStack stack, Aspect aspect) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar_Old)) return;
        NBTHelper.getData(stack).setString("aspect", aspect.getTag());
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {/*
        if(itemStack == null) return;
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar_Old) {
            DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
            Aspect a = getAspect(itemStack);
            if(a != null) {
                familiarData.equipTick(((EntityPlayer) entity).worldObj, (EntityPlayer) entity, a);
            }

            if(((EntityPlayer) entity).worldObj.isRemote) return;

            int probability = 0;
            if(hasUpgrade(itemStack, FamiliarUpgrade.ATTACK_2)) probability += 1;
            if(hasUpgrade(itemStack, FamiliarUpgrade.ATTACK_3)) probability += 1;
            if(probability > 0) {
                int rand = itemRand.nextInt(20000 / probability);
                if(rand == 42) {
                    Thaumcraft.addWarpToPlayer((EntityPlayer) entity, 1 + itemRand.nextInt(4), true);
                }
            }
        }*/
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {/*
        if(itemStack == null) return;
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar_Old) {
            DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
            Aspect a = getAspect(itemStack);
            if(a != null) {
                familiarData.handleEquip(((EntityPlayer) entity).worldObj, (EntityPlayer) entity, a);
            }
        }*/
    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entity) {/*
        if(itemStack == null) return;
        if(entity instanceof EntityPlayer && itemStack.getItem() instanceof ItemFamiliar_Old) {
            DataFamiliar familiarData = SyncDataHolder.getDataServer("FamiliarData");
            Aspect a = getAspect(itemStack);
            if(a != null) {
                familiarData.handleUnequip(((EntityPlayer) entity).worldObj, (EntityPlayer) entity, a);
            }
        }*/
    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
        return false;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    //Infusion stuff.

    public int getAttackRangeIncrease(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar_Old)) return 0;

        int range = 0;

        if(hasUpgrade(itemStack, FamiliarUpgrade.RANGE_1)) {
            range += 3;
        }
        return range;
    }

    public int getAttackCooldownReduction(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar_Old)) return 0;
        return hasUpgrade(itemStack, FamiliarUpgrade.COOLDOWN_1) ? 10 : 0;
    }

    public float getAttackStrength(ItemStack itemStack) {
        if(!(itemStack.getItem() instanceof ItemFamiliar_Old)) return 0F;

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
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar_Old)) return false;
        if(upgrade == null) return true;
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey(upgrade.getTag())) return false;
        return stack.getTagCompound().getBoolean(upgrade.getTag());
    }

    public void addUpgrade(ItemStack stack, FamiliarUpgrade upgrade) {
        if(stack == null || !(stack.getItem() instanceof ItemFamiliar_Old)) return;
        NBTHelper.getData(stack).setBoolean(upgrade.getTag(), true);
    }

    public static ItemStack getPaybackPackage(ItemStack oldFamiliarStack) {
        if(oldFamiliarStack == null || !(oldFamiliarStack.getItem() instanceof ItemFamiliar_Old)) return null;
        ItemStack arcPackage = new ItemStack(RegisteredItems.itemPackage, 1, 2);
        List<ItemStack> contents = new ArrayList<ItemStack>();
        arcPackage.setStackDisplayName(EnumChatFormatting.GOLD + "Used Familiar Items");

        DeprecationItemPaybacks.addFamiliarCraftingPaybackStack(contents, oldFamiliarStack);

        if(RegisteredItems.itemFamiliar_old.hasUpgrade(oldFamiliarStack, FamiliarUpgrade.ATTACK_1)) {
            DeprecationItemPaybacks.addFamiliarAttack1Payback(contents, oldFamiliarStack);
        }
        if(RegisteredItems.itemFamiliar_old.hasUpgrade(oldFamiliarStack, FamiliarUpgrade.ATTACK_2)) {
            DeprecationItemPaybacks.addFamiliarAttack2Payback(contents, oldFamiliarStack);
        }
        if(RegisteredItems.itemFamiliar_old.hasUpgrade(oldFamiliarStack, FamiliarUpgrade.ATTACK_3)) {
            DeprecationItemPaybacks.addFamiliarAttack3Payback(contents, oldFamiliarStack);
        }
        if(RegisteredItems.itemFamiliar_old.hasUpgrade(oldFamiliarStack, FamiliarUpgrade.COOLDOWN_1)) {
            DeprecationItemPaybacks.addFamiliarCooldownPayback(contents, oldFamiliarStack);
        }
        if(RegisteredItems.itemFamiliar_old.hasUpgrade(oldFamiliarStack, FamiliarUpgrade.RANGE_1)) {
            DeprecationItemPaybacks.addFamiliarRangePayback(contents, oldFamiliarStack);
        }

        RegisteredItems.itemPackage.setContents(arcPackage, contents);

        return arcPackage;
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
