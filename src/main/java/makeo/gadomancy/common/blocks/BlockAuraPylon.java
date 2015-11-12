package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 12.11.2015 22:26
 */
public class BlockAuraPylon extends BlockContainer implements IBlockTransparent {

    private IIcon icon;

    public BlockAuraPylon() {
        super(Material.iron);
        setHardness(10F);
        setResistance(50F);
        setStepSound(Block.soundTypeStone);
        setBlockBounds(0.0625F, 0, 0.0625F, 0.9375F, 1, 0.9375F);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    protected String getTextureName() {
        return "minecraft:quartz_block_top";
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        icon = ir.registerIcon(Gadomancy.MODID + ":transparent");
        super.registerBlockIcons(ir);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage == 1 && (y < 1 || world.getBlock(x, y - 1, z) != RegisteredBlocks.blockAuraPylon) && (y < 1 || world.getBlockMetadata(x, y - 1, z) != 0)) {
            return false;
        }
        return super.canReplace(world, x, y, z, side, stack);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if(meta == 0) {
            return new TileAuraPylon();
        } else {
            return new TileAuraPylonTop();
        }
    }

    @Override
    public int getRenderType() {
        return RegisteredBlocks.rendererTransparentBlock;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IIcon getTransparentIcon() {
        return icon;
    }
}
