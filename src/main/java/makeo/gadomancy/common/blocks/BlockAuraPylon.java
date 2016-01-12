package makeo.gadomancy.common.blocks;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.blocks.tiles.TileNodeManipulator;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.registration.RegisteredMultiblocks;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import makeo.gadomancy.common.utils.MultiblockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

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
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if(meta == 1) {
            return 10;
        }
        return super.getLightValue(world, x, y, z);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean canReplace(World world, int x, int y, int z, int side, ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage == 1 && (y < 1 || world.getBlock(x, y - 1, z) != RegisteredBlocks.blockAuraPylon || world.getBlockMetadata(x, y - 1, z) != 0)) {
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if(world.getBlockMetadata(x, y, z) != 1) {
            Block up = world.getBlock(x, y + 1, z);
            return up != null && up instanceof BlockAuraPylon && up.onBlockActivated(world, x, y + 1, z, player, side, par7, par8, par9);
        }
        ItemStack heldItem = player.getHeldItem();
        if(!world.isRemote && heldItem != null && heldItem.getItem() instanceof ItemWandCasting &&
                ResearchManager.isResearchComplete(player.getCommandSenderName(), Gadomancy.MODID.toUpperCase() + ".AURA_PYLON")) {
            TileAuraPylon tileAuraPylon = (TileAuraPylon) world.getTileEntity(x, y - 1, z);
            if(MultiblockHelper.isMultiblockPresent(world, x, y, z, RegisteredMultiblocks.auraPylonPattern) &&
                    !tileAuraPylon.isPartOfMultiblock() &&
                    ThaumcraftApiHelper.consumeVisFromWandCrafting(player.getCurrentEquippedItem(), player, RegisteredRecipes.costsAuraPylonMultiblock, true)) {
                PacketStartAnimation pkt = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, x, y, z);
                NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32);
                PacketHandler.INSTANCE.sendToAllAround(pkt, point);
                TileAuraPylon ta = (TileAuraPylon) world.getTileEntity(x, y - 1, z);
                ta.setTileInformation(true, false);
                ta = (TileAuraPylon) world.getTileEntity(x, y - 3, z);
                ta.setTileInformation(false, true);
                int count = 1;
                TileEntity iter = world.getTileEntity(x, y - count, z);
                while(iter != null && iter instanceof TileAuraPylon) {
                    ((TileAuraPylon) iter).setPartOfMultiblock(true);
                    world.markBlockForUpdate(x, y - count, z);
                    iter.markDirty();
                    pkt = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, x, y - count, z);
                    PacketHandler.INSTANCE.sendToAllAround(pkt, point);
                    count++;
                    iter = world.getTileEntity(x, y - count, z);
                }
            }
        }
        return false;
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
