package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.blocks.tiles.TileArcaneDropper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.09.2015 20:15
 */
public class BlockArcaneDropper extends BlockContainer {

    public static PropertyDirection FACING = PropertyDirection.create("facing");
    public static PropertyBool FLIPPED = PropertyBool.create("flipped");

    public BlockArcaneDropper() {
        super(Material.rock);
        setHardness(3.5f);

        setCreativeTab(CommonProxy.creativeTab);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileArcaneDropper();
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(FACING);
        Boolean flipped = state.getValue(FLIPPED);
        if(facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
            flipped = !flipped;
        }
        EnumFacing rotated = null; //TODO MAKEEOOOO PLZ FIX
        return facing != side && rotated != side && rotated.getOpposite() != side;
    }

    /*@Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        int metadata = world.getBlockMetadata(x, y, z);

        ForgeDirection direction = ForgeDirection.getOrientation(metadata & 7);
        boolean flipped = (metadata & 8) == 8;

        if(direction == ForgeDirection.SOUTH
                || direction == ForgeDirection.NORTH) {
            flipped = !flipped;
        }

        ForgeDirection rotated = direction.getRotation(flipped ? ForgeDirection.getOrientation((direction.ordinal() + 4) % 6) : ForgeDirection.getOrientation((direction.ordinal() + 2) % 6));

        return direction != side && rotated != side && rotated.getOpposite() != side;
    }*/

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta & 7);
        boolean flipped = (meta & 8) == 8;
        return getDefaultState().withProperty(FACING, facing).withProperty(FLIPPED, flipped);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing type = state.getValue(FACING);
        boolean flipped = state.getValue(FLIPPED);
        int meta = type.getIndex();
        meta |= flipped ? 8 : 0;
        return meta;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, FLIPPED);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return createBlockState().getBaseState().withProperty(FACING, facing).withProperty(FLIPPED, false); //TODO uuuuuuhhhh.... ? maybe?
        //return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    protected void setBlockBounds(AxisAlignedBB box) {
        super.minX = box.minX;
        super.minY = box.minY;
        super.minZ = box.minZ;
        super.maxX = box.maxX;
        super.maxY = box.maxY;
        super.maxZ = box.maxZ;
    }

    private static final AxisAlignedBB[] COLLISION_BOXES = new AxisAlignedBB[]{
            AxisAlignedBB.fromBounds(0, 0, 0, 1, 0.3125f, 1),//down
            AxisAlignedBB.fromBounds(0, 0.6875f, 0, 1, 1, 1),//up

            AxisAlignedBB.fromBounds(0, 0, 0, 1, 1, 0.3125f),//north
            AxisAlignedBB.fromBounds(0, 0, 0.6875f, 1, 1, 1),//south

            AxisAlignedBB.fromBounds(0, 0, 0, 0.3125f, 1, 1),//west
            AxisAlignedBB.fromBounds(0.6875f, 0, 0, 1, 1, 1), //east
    };

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        EnumFacing side = state.getValue(FACING);

        for(int i = 0; i < COLLISION_BOXES.length; i++) {
            if(i != side.ordinal()) {
                this.setBlockBounds(COLLISION_BOXES[i]);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        int sideOffsetX = side.getFrontOffsetX();
        int sideOffsetY = side.getFrontOffsetY();
        int sideOffsetZ = side.getFrontOffsetZ();
        if(sideOffsetX + sideOffsetY + sideOffsetZ < 0) {
            this.setBlockBounds(sideOffsetX * -0.125f, sideOffsetY * -0.125f, sideOffsetZ * -0.125f, 1f, 1f, 1f);
        } else {
            this.setBlockBounds(0f, 0f, 0f, 1 - sideOffsetX * 0.125f, 1 - sideOffsetY * 0.125f, 1 - sideOffsetZ * 0.125f);
        }
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = state.getValue(FACING);

        float yaw = MathHelper.wrapAngleTo180_float(placer.rotationYaw);
        float pitch = placer.rotationPitch;

        boolean flipped;
        if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            yaw += 180;
            flipped = yaw > 315 || yaw < 45 || (yaw < 225 && yaw > 135);
        } else {
            switch (facing.getOpposite()) {
                case WEST:
                    yaw -= 90;
                    break;
                case NORTH:
                    yaw = (180 - Math.abs(yaw)) * (yaw < 0 ? 1 : -1);
                    break;
                case EAST:
                    yaw += 90;
                    break;
            }
            flipped = Math.abs(yaw) < Math.abs(pitch);
        }

        IBlockState newState = getDefaultState().withProperty(FACING, facing).withProperty(FLIPPED, flipped);
        worldIn.setBlockState(pos, newState, 2);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
    }

}
