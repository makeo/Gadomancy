package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
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
        //setCreativeTab(RegisteredItems.creativeTab);
        setCreativeTab(null);
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
}
