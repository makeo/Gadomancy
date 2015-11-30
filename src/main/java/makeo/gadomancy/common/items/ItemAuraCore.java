package makeo.gadomancy.common.items;

import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 16.11.2015 15:23
 */
public class ItemAuraCore extends Item {

    public ItemAuraCore() {
        setUnlocalizedName("ItemAuraCore");
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        AuraCoreType type = getCoreType(stack);
        if(type != null) {
            list.add(EnumChatFormatting.GRAY + type.getLocalizedName());
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < AuraCoreType.values().length; i++) {
            ItemStack stack = new ItemStack(item);
            setCoreType(stack, AuraCoreType.values()[i]);
            list.add(stack);
        }
    }

    public void setCoreType(ItemStack itemStack, AuraCoreType type) {
        if(itemStack == null || !(itemStack.getItem() instanceof ItemAuraCore)) return;
        if(type == null) return;
        NBTHelper.getData(itemStack).setInteger("type", type.ordinal());
    }

    public AuraCoreType getCoreType(ItemStack itemStack) {
        if(itemStack == null || !(itemStack.getItem() instanceof ItemAuraCore)) return null;
        int ordinal = NBTHelper.getData(itemStack).getInteger("type");
        return AuraCoreType.values()[ordinal];
    }

    public boolean isBlank(ItemStack itemStack) {
        AuraCoreType type = getCoreType(itemStack);
        return type == AuraCoreType.BLANK || type == null;
    }

    public static enum AuraCoreType {

        BLANK("Blank", "blank"),

        AIR("Aer", "aer"),
        FIRE("Ignis", "ignis"),
        WATER("Aqua", "aqua"),
        EARTH("Terra", "terra"),
        ORDER("Ordo", "ordo"),
        ENTROPY("Perditio", "perditio");

        public final String unlocName, fallback;

        AuraCoreType(String fallback, String unlocName) {
            this.unlocName = "gadomancy.auracore." + unlocName;
            this.fallback = fallback;
        }

        public String getLocalizedName() {
            return StatCollector.canTranslate(unlocName) ? StatCollector.translateToLocal(unlocName) : fallback;
        }
    }

}
