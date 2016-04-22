package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredMultiblocks;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import makeo.gadomancy.common.utils.MultiblockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.items.wands.ItemWand;
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
public class BlockAuraPylon extends BlockContainer {

    public static final PropertyEnum<PylonType> TYPE = PropertyEnum.create("variant", PylonType.class);

    //private IIcon icon;

    public BlockAuraPylon() {
        super(Material.iron);
        setHardness(10F);
        setResistance(50F);
        setStepSound(Block.soundTypeStone);
        setBlockBounds(0.0625F, 0, 0.0625F, 0.9375F, 1, 0.9375F);
        setCreativeTab(CommonProxy.creativeTab);
    }

    /*@Override
    protected String getTextureName() {
        return "minecraft:quartz_block_top";
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        icon = ir.registerIcon(Gadomancy.MODID + ":transparent");
        super.registerBlockIcons(ir);
    }*/

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if(state.getValue(TYPE).equals(PylonType.TOP)) {
            return 10;
        }
        return super.getLightValue(world, pos);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromPylonType(state.getValue(TYPE));
    }

    @Override
    public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
        int damage = stack.getItemDamage();
        PylonType stackType = getTypeFromMeta(damage);
        if(stackType.equals(PylonType.TOP)) {
            if(pos.getY() < 1) return false;
            IBlockState below = worldIn.getBlockState(pos.add(0, -1, 0));
            if(!below.getBlock().equals(RegisteredBlocks.blockAuraPylon)) return false;
            return below.getValue(TYPE).equals(PylonType.TOWER);
        }
        return super.canReplace(worldIn, pos, side, stack);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, getTypeFromMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getMetaFromPylonType(state.getValue(TYPE));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE);
    }

    private PylonType getTypeFromMeta(int meta) {
        return PylonType.values()[meta % PylonType.values().length];
    }

    private int getMetaFromPylonType(PylonType type) {
        return type.ordinal();
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        PylonType type = state.getValue(TYPE);
        switch (type) {
            case TOWER:
                return new TileAuraPylon();
            case TOP:
                return new TileAuraPylonTop();
        }
        return super.createTileEntity(world, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT; //TODO ugh
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(TYPE) != null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!state.getValue(TYPE).equals(PylonType.TOP)) {
            IBlockState up = worldIn.getBlockState(pos.add(0, 1, 0));
            return up != null && up.getBlock() instanceof BlockAuraPylon && up.getBlock().onBlockActivated(worldIn, pos.add(0, 1, 0), state, playerIn, side, hitX, hitY, hitZ);
        }
        ItemStack heldItem = playerIn.getHeldItem();
        if(!worldIn.isRemote && heldItem != null && heldItem.getItem() instanceof ItemWand &&
                ResearchManager.isResearchComplete(playerIn.getName(), Gadomancy.MODID.toUpperCase() + ".AURA_PYLON")) {
            TileAuraPylon tileAuraPylon = (TileAuraPylon) worldIn.getTileEntity(pos.add(0, -1, 0));
            if(MultiblockHelper.isMultiblockPresent(worldIn, pos, RegisteredMultiblocks.auraPylonPattern) &&
                    !tileAuraPylon.isPartOfMultiblock() &&
                    ((ItemWand) heldItem.getItem()).consumeAllVis(playerIn.getCurrentEquippedItem(), playerIn, RegisteredRecipes.costsAuraPylonMultiblock, true, true)) {
                PacketStartAnimation pkt = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, pos);
                NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(worldIn.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 32);
                PacketHandler.INSTANCE.sendToAllAround(pkt, point);
                TileAuraPylon ta = (TileAuraPylon) worldIn.getTileEntity(pos.add(0, -1, 0));
                ta.setTileInformation(true, false);
                ta = (TileAuraPylon) worldIn.getTileEntity(pos.add(0, -3, 0));
                ta.setTileInformation(false, true);
                int count = 1;
                TileEntity iter = worldIn.getTileEntity(pos.add(0, -count, 0));
                while(iter != null && iter instanceof TileAuraPylon) {
                    ((TileAuraPylon) iter).setPartOfMultiblock(true);
                    worldIn.markBlockForUpdate(pos.add(0, -count, 0));
                    iter.markDirty();
                    pkt = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, pos.add(0, -count, 0));
                    PacketHandler.INSTANCE.sendToAllAround(pkt, point);
                    count++;
                    iter = worldIn.getTileEntity(pos.add(0, -count, 0));
                }
            }
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public static enum PylonType implements IStringSerializable {

        TOWER,  TOP;

        private PylonType() {}

        public String getName()
        {
            return name().toLowerCase();
        }

        public String toString()
        {
            return getName();
        }
    }

}
