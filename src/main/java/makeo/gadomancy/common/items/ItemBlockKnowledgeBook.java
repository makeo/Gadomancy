package makeo.gadomancy.common.items;

import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;

/**
 * HellFirePvP@Admin
 * Date: 19.04.2016 / 14:51
 * on Gadomancy
 * ItemBlockKnowledgeBook
 */
public class ItemBlockKnowledgeBook extends ItemBlock {

    public ItemBlockKnowledgeBook(Block block) {
        super(block);
        setMaxDamage(0);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return RegisteredItems.raritySacred;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        ForgeDirection placedAgainstDir = ForgeDirection.getOrientation(side);
        Block placedAgainst = world.getBlock(x, y, z);
        int againstMeta = world.getBlockMetadata(x, y, z);
        if(placedAgainstDir.equals(ForgeDirection.UP) && placedAgainst.equals(ConfigBlocks.blockStoneDevice) && againstMeta == 1) {
            if(world.isAirBlock(x, y + 2, z)) {
                return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
