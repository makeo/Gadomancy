package makeo.gadomancy.common.items;

import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.items.ItemLootBag;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 19.12.2015 21:46
 */
public class ItemFakeLootbag extends ItemLootBag {
    public ItemFakeLootbag() {
        setCreativeTab(null);
        setUnlocalizedName("ItemLootBag");
    }

    @Override
    public IIcon getIconFromDamage(int index) {
        return index >= super.icon.length ? null : super.getIconFromDamage(index);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        return RegisteredItems.itemPackage.onItemRightClick(stack, world, player);
    }
}
