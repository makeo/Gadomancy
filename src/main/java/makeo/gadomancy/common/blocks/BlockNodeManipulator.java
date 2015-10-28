package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileNodeManipulator;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.blocks.BlockStoneDevice;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 26.10.2015 19:23
 */
public class BlockNodeManipulator extends BlockStoneDevice {

    public BlockNodeManipulator() {
        setBlockName("blockNodeManipulator");
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);

        iconPedestal[1] = ir.registerIcon(Gadomancy.MODID + ":manipulator_bot");
        iconWandPedestal[0] = ir.registerIcon(Gadomancy.MODID + ":manipulator_side");
        iconWandPedestal[1] = ir.registerIcon(Gadomancy.MODID + ":manipulator_top");

        iconWandPedestalFocus[0] = ir.registerIcon(Gadomancy.MODID + ":manipulator_focus_side");
        iconWandPedestalFocus[1] = ir.registerIcon(Gadomancy.MODID + ":manipulator_focus_top");
        iconWandPedestalFocus[2] = ir.registerIcon(Gadomancy.MODID + ":manipulator_focus_bot");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }

    //TC stuff...
    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (metadata == 5)
            return new TileNodeManipulator();
        return null;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
        int ret = super.getComparatorInputOverride(world, x, y, z, rs);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileNodeManipulator) return 0;
        return ret;
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 5));
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y + 1, z) == Blocks.air && super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if(world.getBlock(x, y + 1, z) != Blocks.air) {
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileNodeManipulator) {
                if(((TileNodeManipulator) te).isInMultiblock())
                    ((TileNodeManipulator) te).breakMultiblock();
            }
            dropBlockAsItem(world, x, y, z, 5, 0);
            world.setBlockToAir(x, y, z);
        }
        super.onNeighborBlockChange(world, x, y, z, par5);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if(world.isRemote) return true;

        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileNodeManipulator) {
            TileNodeManipulator manipulator = (TileNodeManipulator) te;
            if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemWandCasting) {
                manipulator.checkMultiblock();
                if (manipulator.isMultiblockStructurePresent()) {
                    if (manipulator.isInMultiblock()) {
                        return tryAddWandOrRemove(manipulator, x, y, z, player);
                    } else if (ThaumcraftApiHelper.consumeVisFromWandCrafting(player.getCurrentEquippedItem(), player, RegisteredRecipes.costsNodeManipulatorMultiblock, true)) {
                        manipulator.formMultiblock();
                        return true;
                    }
                }
            } else {
                tryAddWandOrRemove(manipulator, x, y, z, player);
            }
        }
        return true;
    }

    private boolean tryAddWandOrRemove(TileNodeManipulator manipulator, int x, int y, int z, EntityPlayer player) {
        World world = manipulator.getWorldObj();
        if (manipulator.getStackInSlot(0) != null) {
            InventoryUtils.dropItemsAtEntity(world, x, y, z, player);
            world.markBlockForUpdate(x, y, z);
            manipulator.markDirty();
            world.playSoundEffect(x, y, z, "random.pop", 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 1.5F);
            return true;
        }
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemWandCasting) {
            ItemStack i = player.getCurrentEquippedItem().copy();
            i.stackSize = 1;
            manipulator.setInventorySlotContents(0, i);
            player.getCurrentEquippedItem().stackSize -= 1;
            if (player.getCurrentEquippedItem().stackSize == 0) {
                player.setCurrentItemOrArmor(0, null);
            }
            player.inventory.markDirty();
            world.markBlockForUpdate(x, y, z);
            manipulator.markDirty();
            world.playSoundEffect(x, y, z, "random.pop", 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 1.6F);

            return true;
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return RegisteredBlocks.rendererNodeManipulator;
    }
}
