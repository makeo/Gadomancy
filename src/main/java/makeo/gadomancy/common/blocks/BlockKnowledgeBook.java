package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.blocks.tiles.TileKnowledgeBook;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.devices.BlockPedestal;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 19.04.2016 / 14:52
 * on Gadomancy
 * BlockKnowledgeBook
 */
public class BlockKnowledgeBook extends BlockContainer implements TileKnowledgeBook.IKnowledgeProvider {

    public BlockKnowledgeBook() {
        super(Material.circuits);
        setBlockBounds(0.0625F, 0.125F, 0.0625F, 0.9375F, 0.5F, 0.9375F);
        setCreativeTab(CommonProxy.creativeTab);
    }

    /*@Override
    public String getTextureName() {
        return "minecraft:wool_colored_white";
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 8;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        icon = ir.registerIcon(Gadomancy.MODID + ":transparent");
        super.registerBlockIcons(ir);
    }*/

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack thaumonomicon = new ItemStack(ItemsTC.thaumonomicon);
        ItemsTC.thaumonomicon.onItemRightClick(thaumonomicon, worldIn, playerIn);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if(worldIn.isRemote) return;
        IBlockState lower = worldIn.getBlockState(pos.add(0, -1, 0));
        if(!lower.equals(BlocksTC.pedestal.getDefaultState().withProperty(BlockPedestal.VARIANT, BlockPedestal.PedestalType.NORMAL))) {
            breakThisBlock(worldIn, pos);
        }
        if(!worldIn.isAirBlock(pos.add(0, 1, 0))) {
            breakThisBlock(worldIn, pos);
        }
    }

    public void breakThisBlock(World world, BlockPos pos) {
        if(world.isRemote) return;
        IBlockState state = world.getBlockState(pos);
        List<ItemStack> stacks = getDrops(world, pos, state, 0);
        for(ItemStack i : stacks) {
            EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, i);
            world.spawnEntityInWorld(item);
        }

        world.removeTileEntity(pos);
        world.setBlockToAir(pos);
        world.markBlockForUpdate(pos);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te != null && te instanceof TileKnowledgeBook) {
            if(((TileKnowledgeBook) te).isResearching()) {
                if(((TileKnowledgeBook) te).hasCognitio()) {
                    return 15;
                }
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 2; //TESR
    }

    @Override
    public int getProvidedKnowledge(World world, BlockPos pos) {
        return 2;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileKnowledgeBook();
    }
}
