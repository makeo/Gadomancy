package makeo.gadomancy.common.items;

import makeo.gadomancy.common.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.devices.BlockPedestal;

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
        setCreativeTab(CommonProxy.creativeTab);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return CommonProxy.RARITY_SACRED;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState at = worldIn.getBlockState(pos);
        Block against = at.getBlock();
        if(!against.equals(BlocksTC.pedestal)) return false;
        BlockPedestal.PedestalType type = (BlockPedestal.PedestalType) at.getValue(BlockPedestal.VARIANT);
        if(side.equals(EnumFacing.UP) && type.equals(BlockPedestal.PedestalType.NORMAL)) {
            if(worldIn.isAirBlock(pos.add(0, 2, 0))) {
                return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /*@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
         placedAgainstDir = ForgeDirection.getOrientation(side);
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
    }*/
}
