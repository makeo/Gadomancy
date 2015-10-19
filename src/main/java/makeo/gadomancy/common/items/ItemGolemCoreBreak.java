package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 02.10.2015 19:40
 */
public class ItemGolemCoreBreak extends Item {
    public ItemGolemCoreBreak() {
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    protected String getIconString() {
        return Gadomancy.MODID + ":golem_core_break";
    }

    @Override
    public String getUnlocalizedName() {
        return "item.ItemGolemCore";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean showAdvanced) {
        list.add(StatCollector.translateToLocal("gadomancy.golem.breakcore"));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
