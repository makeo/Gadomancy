package makeo.gadomancy.common.blocks;

import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 05.10.2015 21:38
 */
public class BlockInfusionClaw extends BlockContainer {

    public BlockInfusionClaw() {
        super(Material.rock);
        setHardness(3.5f);

        setCreativeTab(CommonProxy.creativeTab);
    }

    /*@Override
    protected String getTextureName() {
        return "thaumcraft:pedestal_top";
    }*/

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if(!worldIn.isRemote) {
            TileInfusionClaw tile = (TileInfusionClaw) worldIn.getTileEntity(pos);
            tile.updateRedstone(worldIn.isBlockPowered(pos));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(placer instanceof EntityPlayer) {
            TileInfusionClaw tile = (TileInfusionClaw) worldIn.getTileEntity(pos);
            tile.setOwner((EntityPlayer) placer);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileInfusionClaw();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            TileInfusionClaw tile = (TileInfusionClaw) worldIn.getTileEntity(pos);
            if(tile.hasOwner()) {
                playerIn.openGui(Gadomancy.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
            } else if(tile.setOwner(playerIn)) {
                playerIn.addChatComponentMessage(new ChatComponentTranslation("gadomancy.info.InfusionClaw.owner"));
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileInfusionClaw tile = (TileInfusionClaw) worldIn.getTileEntity(pos);
        ItemStack wandStack = tile.getStackInSlot(0);
        if(wandStack != null && wandStack.stackSize > 0) {
            float f = worldIn.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = worldIn.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = worldIn.rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, wandStack.copy());
            ItemUtils.applyRandomDropOffset(entityItem, worldIn.rand);

            worldIn.spawnEntityInWorld(entityItem);
        }
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        if(pos.getY() > 0) {
            ClickBehavior behavior = RegisteredBlocks.getClawClickBehavior(worldIn, pos.add(0, -1, 0));
            if(behavior != null) {
                return behavior.getComparatorOutput();
            }
        }
        return 0;
    }
}
