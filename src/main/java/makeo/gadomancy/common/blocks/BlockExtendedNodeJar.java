package makeo.gadomancy.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNodeJar;
import makeo.gadomancy.common.items.ItemExtendedNodeJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.blocks.CustomStepSound;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Original created by Azanor@Thaumcraft: thaumcraft.common.blocks.BlockJar
 * Modified to create compatibility with ExtendedNodes
 * Created by HellFirePvP @ 25.10.2015 22:02
 */
public class BlockExtendedNodeJar extends BlockContainer {

    public BlockExtendedNodeJar() {
        super(Material.glass);
        setHardness(0.3F);
        setStepSound(new CustomStepSound("jar", 1.0F, 1.0F));
        setLightLevel(0.66F);
        setBlockName("blockExNodeJar");
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {}

    @Override
    public IIcon getIcon(int side, int meta) {
        BlockJar jar = (BlockJar) ConfigBlocks.blockJar;
        return side == 1 ? jar.iconJarTop : side == 0 ? jar.iconJarBottom : jar.iconJarSide;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileExtendedNodeJar();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return RegisteredBlocks.rendererExtendedNodeJarBlock;
    }


    public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
        dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
        super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        TileEntity te = world.getTileEntity(x, y, z);
        if ((te != null) && ((te instanceof TileExtendedNodeJar)) && (((TileExtendedNodeJar) te).drop) &&
                (((TileExtendedNodeJar) te).getAspects() != null)) {
            ItemStack drop = new ItemStack(RegisteredItems.itemExtendedNodeJar);
            ((ItemExtendedNodeJar) drop.getItem()).setAspects(drop, ((TileExtendedNodeJar) te).getAspects().copy());
            ((ItemExtendedNodeJar) drop.getItem()).setNodeAttributes(drop, ((TileExtendedNodeJar) te).getNodeType(), ((TileExtendedNodeJar) te).getNodeModifier(), ((TileExtendedNodeJar) te).getExtendedNodeType(), ((TileExtendedNodeJar) te).getId());
            ((ItemExtendedNodeJar) drop.getItem()).setBehaviorSnapshot(drop, ((TileExtendedNodeJar) te).getBehaviorSnapshot());
            drops.add(drop);
        }
        return drops;
    }

    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return true;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
        setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.75F, 0.8125F);
        super.setBlockBoundsBasedOnState(world, i, j, k);
    }

    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
    }

    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 11;
    }

}
