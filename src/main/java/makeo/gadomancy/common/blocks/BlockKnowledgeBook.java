package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileKnowledgeBook;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.ArrayList;

/**
 * HellFirePvP@Admin
 * Date: 19.04.2016 / 14:52
 * on Gadomancy
 * BlockKnowledgeBook
 */
public class BlockKnowledgeBook extends BlockContainer implements IBlockTransparent, TileKnowledgeBook.IKnowledgeProvider {

    private IIcon icon;

    public BlockKnowledgeBook() {
        super(Material.circuits);
        setBlockBounds(0.0625F, 0.125F, 0.0625F, 0.9375F, 0.5F, 0.9375F);
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
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
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        ItemStack thaumonomicon = new ItemStack(ConfigItems.itemThaumonomicon);
        ConfigItems.itemThaumonomicon.onItemRightClick(thaumonomicon, world, player);
        /*if(world.isRemote) {
            try {
                player.openGui(Thaumcraft.instance, 12, world, x, y, z);
                Minecraft.getMinecraft().theWorld.playSound(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, "thaumcraft:page", 0.66F, 1.0F, false);
            } catch (Throwable tr) {
                Gadomancy.log.warn("Error on opening Thaumcraft Thaumonomicon via KnowledgeBook at world " + world.getWorldInfo().getWorldName() + ", x=" + x + ", y=" + y + ", z=" + z);
            }
        }*/
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if(world.isRemote) return;
        Block lower = world.getBlock(x, y - 1, z);
        int metaLower = world.getBlockMetadata(x, y - 1, z);
        if(!lower.equals(ConfigBlocks.blockStoneDevice) || metaLower != 1) {
            breakThisBlock(world, x, y, z);
        }
        if(!world.isAirBlock(x, y + 1, z)) {
            breakThisBlock(world, x, y, z);
        }
    }

    public void breakThisBlock(World world, int x, int y, int z) {
        if(world.isRemote) return;
        ArrayList<ItemStack> stacks = getDrops(world, x, y, z, 0, 0);
        for(ItemStack i : stacks) {
            EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, i);
            world.spawnEntityInWorld(item);
        }

        world.removeTileEntity(x, y, z);
        world.setBlockToAir(x, y, z);
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_) {
        TileEntity te = world.getTileEntity(x, y, z);
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IIcon getTransparentIcon() {
        return icon;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileKnowledgeBook();
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
    public int getProvidedKnowledge(World world, int blockX, int blockY, int blockZ) {
        return 2;
    }
}
