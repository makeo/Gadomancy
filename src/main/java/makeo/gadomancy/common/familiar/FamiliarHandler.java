package makeo.gadomancy.common.familiar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 31.10.2015 12:11
 */
public interface FamiliarHandler {

    public void setupPostInit();

    public void notifyEquip(World world, ItemStack familiarStack, EntityPlayer player);

    public void notifyUnequip(World world, ItemStack familiarStack, EntityPlayer player);

    public void equippedTick(World world, ItemStack familiarStack, EntityPlayer player);

}
