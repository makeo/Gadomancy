package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
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
//TODO rename when updating to 1.8
public class ItemGolemCoreBreak extends Item {

    public ItemGolemCoreBreak() {
        setHasSubtypes(true);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    private IIcon breakIcon;
    private IIcon bodyguardIcon;

    @Override
    public void registerIcons(IIconRegister ir) {
        breakIcon = ir.registerIcon(Gadomancy.MODID + ":golem_core_break");
        bodyguardIcon = ir.registerIcon("thaumcraft:golem_core_bodyguard");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if(damage == 0) {
            return breakIcon;
        } else if(damage == 1) {
            return bodyguardIcon;
        }

        return super.getIconFromDamage(damage);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
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
        int damage = stack.getItemDamage();
        if(damage == 0) {
            list.add(StatCollector.translateToLocal("gadomancy.golem.breakcore"));
        } else if(damage == 1) {
            list.add(StatCollector.translateToLocal("gadomancy.golem.bodyguardcore"));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
