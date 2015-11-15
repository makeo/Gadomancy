package makeo.gadomancy.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 17:05
 */
public class ItemBlockStoneMachine extends ItemBlock {
    public ItemBlockStoneMachine(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int metadata) {
        if(metadata == 11 || metadata == 15) {
            return this.field_150939_a.getIcon(0, metadata);
        }
        return super.getIconFromDamage(metadata);
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
