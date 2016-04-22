package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileArcanePackager;
import makeo.gadomancy.common.blocks.tiles.TileBlockProtector;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.essentia.TileJarFillable;

import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 13:16
 */
public class BlockStoneMachine extends BlockContainer {

    public static PropertyEnum<MachineType> MACHINE_TYPE = PropertyEnum.create("machine", MachineType.class);

    public BlockStoneMachine() {
        super(Material.rock);
        setHardness(3.0F);
        setResistance(25.0F);
        setStepSound(Block.soundTypeStone);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setCreativeTab(CommonProxy.creativeTab);
    }

    /*private IIcon pillarIcon;
    private IIcon focusIcon;
    public IIcon pedestalSideIcon;
    public IIcon pedestalTopIcon;
    public IIcon protectorIcon;
    public IIcon packagerIcon;

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        pillarIcon = ir.registerIcon("thaumcraft:es_1");
        focusIcon = ir.registerIcon("thaumcraft:thaumiumblock");
        pedestalTopIcon = ir.registerIcon(Gadomancy.MODID + ":manipulator_bot");
        pedestalSideIcon = ir.registerIcon(Gadomancy.MODID + ":ancient_pedestal_side");
        protectorIcon = ir.registerIcon("thaumcraft:pipe_1");
        packagerIcon = ir.registerIcon("thaumcraft:planks_greatwood");
    }*/

    /*@Override
    public IIcon getIcon(int side, int metadata) {
        if (metadata == 11 || metadata == 15) {
            return pillarIcon;
        } else if (metadata == 0 || metadata == 3) {
            return focusIcon;
        } else if (metadata == 1) {
            if (side > 1) {
                return pedestalSideIcon;
            } else {
                return pedestalTopIcon;
            }
        } else if (metadata == 2) {
            return protectorIcon;
        } else if(metadata == 4) {
            return packagerIcon;
        }

        return super.getIcon(side, metadata);
    }*/

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(MACHINE_TYPE) != null;
    }

    /*@Override
    public boolean hasTileEntity(int metadata) {
        return metadata == 15 || metadata == 0 || metadata == 1 || metadata == 2 || metadata == 3 || metadata == 4;
    }*/

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return super.getPickBlock(target, world, pos, player);
    }

    /*@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 11 || metadata == 15) {
            return new ItemStack(getItemDropped(metadata, null, 0), 1, damageDropped(metadata));
        }

        return super.getPickBlock(target, world, x, y, z, player);
    }*/

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type == null) return null;
        return type.provideTileEntity(world, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    /*@Override
    public TileEntity createTileEntity(World world, int metadata) {
        if (metadata == 15) {
            return new TileManipulatorPillar();
        } else if (metadata == 0) {
            return new TileManipulationFocus();
        } else if (metadata == 1) {
            return new TilePedestal();
        } else if (metadata == 2) {
            return new TileBlockProtector();
        } else if (metadata == 3) {
            return new TileManipulationFocus();
        } else if(metadata == 4) {
            return new TileArcanePackager();
        }
        return null;
    }*/

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return meta < MachineType.values().length ? getDefaultState().withProperty(MACHINE_TYPE, MachineType.values()[meta]) : getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        MachineType type = state.getValue(MACHINE_TYPE);
        return type == null ? 0 : type.ordinal();
    }

    @Override
    protected BlockState createBlockState(){
        return new BlockState(this, MACHINE_TYPE);
    }

    /*public IProperty[] getProperties() {
        return new IProperty[] { MACHINE_TYPE };
    }

    public String getStateName(IBlockState state, boolean fullName) {
        MachineType type = state.getValue(MACHINE_TYPE);

        return type.getName() + (fullName ? "_metal" : "");
    }*/

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    /*@Override
    public int damageDropped(int metadata) {
        return metadata;
    }*/

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote) {
            TileArcanePackager tile = (TileArcanePackager) worldIn.getTileEntity(pos);

            for(int i = 0; i < tile.getSizeInventory(); i++) {
                ItemStack stack = tile.getStackInSlot(i);
                if(stack != null && stack.stackSize > 0) {
                    float f = worldIn.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = worldIn.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = worldIn.rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, stack.copy());
                    ItemUtils.applyRandomDropOffset(entityItem, worldIn.rand);

                    worldIn.spawnEntityInWorld(entityItem);
                    tile.setInventorySlotContents(i, null);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type != null) {
            switch (type) {
                case PACKAGER:
                case PROTECTOR:
                    ((TileJarFillable) worldIn.getTileEntity(pos)).facing = MathHelper.floor_double(-placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
                    break;
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    /*@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack stack) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 2 || metadata == 4) {
            ((TileJarFillable) world.getTileEntity(x, y, z)).facing = MathHelper.floor_double(-ent.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
        }
    }*/

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type == null) return;
        switch (type) {
            case PACKAGER:
                if(!worldIn.isRemote) {
                    TileArcanePackager tile = (TileArcanePackager) worldIn.getTileEntity(pos);
                    tile.updateRedstone(worldIn.isBlockPowered(pos));
                }
                break;
        }
    }

    /*@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 15) {
            if (world.getBlock(x, y + 1, z) != this || world.getBlockMetadata(x, y + 1, z) != 11) {
                dropBlockAsItem(world, x, y, z, metadata, 0);
                world.setBlockToAir(x, y, z);
            }
        } else if (metadata == 11) {
            if (world.getBlock(x, y - 1, z) != this || world.getBlockMetadata(x, y - 1, z) != 15) {
                dropBlockAsItem(world, x, y, z, metadata, 0);
                world.setBlockToAir(x, y, z);
            }
        } else if (metadata == 0 || metadata == 3) {
            if (world.getBlock(x, y - 1, z) != RegisteredBlocks.blockNodeManipulator || world.getBlockMetadata(x, y - 1, z) != 5) {
                dropBlockAsItem(world, x, y, z, metadata, 0);
                world.setBlockToAir(x, y, z);
            }
        } else if(metadata == 1) {
            if (!world.isAirBlock(x, y + 1, z)) {
                InventoryUtils.dropItems(world, x, y, z);
            }
        } else if(metadata == 4) {
            if(!world.isRemote) {
                TileArcanePackager tile = (TileArcanePackager) world.getTileEntity(x, y, z);
                tile.updateRedstone(world.isBlockIndirectlyGettingPowered(x, y, z));
            }
        }
    }*/

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type != null) {
            switch (type) {
                case PROTECTOR:
                    TileBlockProtector protector = (TileBlockProtector) world.getTileEntity(pos);
                    return protector.getCurrentRange();
            }
        }
        return super.getLightValue(world, pos);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type != null) {
            switch (type) {
                case PROTECTOR:
                    TileBlockProtector protector = (TileBlockProtector) worldIn.getTileEntity(pos);
                    return protector.getCurrentRange();
            }
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type != null) {
            switch (type) {
                case PACKAGER:
                    playerIn.openGui(Gadomancy.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
                    return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    /*@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata == 0 || metadata == 3) {
            if (world.getBlock(x, y - 1, z).equals(RegisteredBlocks.blockNodeManipulator) && world.getBlockMetadata(x, y - 1, z) == 5) {
                return world.getBlock(x, y - 1, z).onBlockActivated(world, x, y - 1, z, player, side, hitX, hitY, hitZ);
            }
        } else if (metadata == 1) {
            if(world.isAirBlock(x, y + 1, z)) {
                return ConfigBlocks.blockStoneDevice.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
            }
            return true;
        } else if(metadata == 4) {
            player.openGui(Gadomancy.instance, 2, world, x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }*/

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type != null) {
            switch (type) {
                case PACKAGER:
                    setBlockBounds(0, 0, 0, 1, 12/16f, 1);
                    break;
                case PROTECTOR:
                    setBlockBounds(0, 0, 0, 1, 3/16f, 1);
                    break;
            }
        }
        super.setBlockBoundsBasedOnState(worldIn, pos);
    }

    /*@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 15) {
            setBlockBounds(0, 0, 0, 1, 0.5f, 1);
        } else if (metadata == 11) {
            setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, -0.5F, 1.0F);
        } else if (metadata == 0 || metadata == 3) {
            setBlockBounds(3 / 16f, 0, 3 / 16f, 1 - (3 / 16f), 6 / 16f, 1 - (3 / 16f));
        } else if (metadata == 1) {
            setBlockBounds(0.25f, 0, 0.25f, 0.75f, 0.99f, 0.75f);
        } else if(metadata == 2) {
            setBlockBounds(0, 0, 0, 1, 3/16f, 1);
        } else if(metadata == 4) {
             setBlockBounds(0, 0, 0, 1, 12/16f, 1);
        }
        super.setBlockBoundsBasedOnState(world, x, y, z);
    }*/

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        MachineType type = state.getValue(MACHINE_TYPE);
        if(type != null) {
            switch (type) {
                case PACKAGER:
                    break;
                case PROTECTOR:
                    setBlockBounds(3 / 16f, 0, 3 / 16f, 1 - (3 / 16f), 9.5f/16f, 1 - (3 / 16f));
                    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
                    //super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                    setBlockBoundsBasedOnState(worldIn, pos);
                    //setBlockBoundsBasedOnState(world, x, y, z);
                    break;
            }
        }
        setBlockBoundsBasedOnState(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    /*@Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 11 || metadata == 15 || metadata == 1) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
        } else if(metadata == 2) {
            setBlockBounds(3 / 16f, 0, 3 / 16f, 1 - (3 / 16f), 9.5f/16f, 1 - (3 / 16f));
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
            setBlockBoundsBasedOnState(world, x, y, z);
        } else {
            setBlockBoundsBasedOnState(world, x, y, z);
        }
        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
    }*/

    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 2; //TESR
    }

    public static enum MachineType implements IStringSerializable, IVariantTileProvider {

        PACKAGER() {
            @Override
            public TileEntity provideTileEntity(World world, IBlockState state) {
                return new TileArcanePackager();
            }
        },
        PROTECTOR() {
            @Override
            public TileEntity provideTileEntity(World world, IBlockState state) {
                return new TileBlockProtector();
            }
        };

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}
