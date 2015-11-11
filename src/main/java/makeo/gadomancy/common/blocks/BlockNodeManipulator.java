package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileNodeManipulator;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.blocks.BlockStoneDevice;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        TileNodeManipulator tile = (TileNodeManipulator) world.getTileEntity(x, y, z);
        ItemStack heldItem = player.getHeldItem();
        if(tile.isInMultiblock()) {
            super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
        } else if(!world.isRemote && heldItem != null && heldItem.getItem() instanceof ItemWandCasting) {
            tile.checkMultiblock();
            if (tile.isMultiblockStructurePresent()) {
                String research = tile.getMultiblockType().getResearchNeeded();
                if(!ResearchManager.isResearchComplete(player.getCommandSenderName(), research)) return false;
                if (ThaumcraftApiHelper.consumeVisFromWandCrafting(player.getCurrentEquippedItem(), player, tile.getMultiblockType().getMultiblockCosts(), true)) {
                    tile.formMultiblock();
                    return true;
                }
            }
        }
        return false;
    }
}
