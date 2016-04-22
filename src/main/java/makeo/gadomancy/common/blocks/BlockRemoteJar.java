package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.devices.BlockJar;
import thaumcraft.common.tiles.essentia.TileJarFillable;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 14:56
 */
public class BlockRemoteJar extends BlockJar {

    public BlockRemoteJar() {
        setCreativeTab(CommonProxy.creativeTab);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileRemoteJar();
    }

    /*@Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);

        this.iconJarSide = ir.registerIcon(Gadomancy.MODID + ":jar_remote_side");
        this.iconJarTop = ir.registerIcon(Gadomancy.MODID + ":jar_remote_top");
        this.iconJarBottom = ir.registerIcon(Gadomancy.MODID + ":transparent");
    }*/

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = super.getDrops(world, pos, state, fortune);

        for(ItemStack drop : drops) {
            if(drop.getItem() == Item.getItemFromBlock(BlocksTC.jar) || drop.getItem() == Item.getItemFromBlock(this)) {
                drop.setItem(Item.getItemFromBlock(this));

                TileRemoteJar tile = getJarTile(world, pos);
                if(tile.networkId != null) {
                    NBTHelper.setUUID(NBTHelper.getData(drop), "networkId", tile.networkId);
                }
            }
        }
        return drops;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float these, float are, float variables) {
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() == ItemsTC.phial) {
            if(!world.isRemote) {
                BlockStickyJar.handlePhial(world, pos, player, heldItem, (TileJarFillable) world.getTileEntity(pos));
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, side, these, are, variables);
    }

    public static TileRemoteJar getJarTile(TileEntity tile) {
        if (tile != null) {
            if (tile instanceof TileRemoteJar) {
                return (TileRemoteJar) tile;
            }
            if (tile instanceof TileStickyJar && ((TileStickyJar) tile).getParent() instanceof TileRemoteJar) {
                return (TileRemoteJar) ((TileStickyJar) tile).getParent();
            }
        }
        return null;
    }

    public static TileRemoteJar getJarTile(IBlockAccess world, BlockPos pos) {
        return getJarTile(world.getTileEntity(pos));
    }
}
