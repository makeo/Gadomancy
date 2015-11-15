package makeo.gadomancy.common.items;

import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 13.11.2015 00:43
 */
public class ItemBlockAuraPylon extends ItemBlock {

    public ItemBlockAuraPylon(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }
}
