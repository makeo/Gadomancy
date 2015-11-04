package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.ArrayList;
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
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileRemoteJar();
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);

        this.iconJarSide = ir.registerIcon(Gadomancy.MODID + ":jar_remote_side");
        this.iconJarTop = ir.registerIcon(Gadomancy.MODID + ":jar_remote_top");
        this.iconJarBottom = ir.registerIcon(Gadomancy.MODID + ":transparent");
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = super.getDrops(world, x, y, z, metadata, fortune);

        for(ItemStack drop : drops) {
            if(drop.getItem() == ConfigItems.itemJarFilled || drop.getItem() == Item.getItemFromBlock(this)) {
                drop.func_150996_a(Item.getItemFromBlock(this));

                TileRemoteJar tile = getJarTile(world, x, y, z);
                if(tile.networkId != null) {
                    NBTHelper.setUUID(NBTHelper.getData(drop), "networkId", tile.networkId);
                }
            }
        }
        return drops;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float these, float are, float variables) {
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() == ConfigItems.itemEssence) {
            if(!world.isRemote) {
                BlockStickyJar.handlePhial(world, x, y, z, player, heldItem, (TileJarFillable) world.getTileEntity(x, y, z));
            }
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, side, these, are, variables);
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

    public static TileRemoteJar getJarTile(World world, int x, int y, int z) {
        return getJarTile(world.getTileEntity(x, y, z));
    }
}
