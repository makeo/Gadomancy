package makeo.gadomancy.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.12.2015 16:15
 */
public class EnchantmentRevealer extends Enchantment {
    public EnchantmentRevealer(int id) {
        super(id, 1, EnumEnchantmentType.armor_head);
    }

    @Override
    public String getName() {
        return "gadomancy.enchantment.revealer";
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 15;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }
}
