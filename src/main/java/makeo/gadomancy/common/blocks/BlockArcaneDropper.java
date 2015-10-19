package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.blocks.tiles.TileArcaneDropper;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.09.2015 20:15
 */
public class BlockArcaneDropper extends BlockTransparent {
    public BlockArcaneDropper() {
        super(Material.rock);
        setHardness(3.5f);

        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileArcaneDropper createTileEntity(World world, int metadata) {
        return new TileArcaneDropper();
    }

    @Override
    protected String getTextureName() {
        return "thaumcraft:pedestal_top";
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
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
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        return side;
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
            AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 0.3125f, 1),//down
            AxisAlignedBB.getBoundingBox(0, 0.6875f, 0, 1, 1, 1),//up

            AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 0.3125f),//north
            AxisAlignedBB.getBoundingBox(0, 0, 0.6875f, 1, 1, 1),//south

            AxisAlignedBB.getBoundingBox(0, 0, 0, 0.3125f, 1, 1),//west
            AxisAlignedBB.getBoundingBox(0.6875f, 0, 0, 1, 1, 1), //east
    };

    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB boundingBox, List list, Entity entity) {
        ForgeDirection side = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z) & 7);

        for(int i = 0; i < COLLISION_BOXES.length; i++) {
            if(i != side.ordinal()) {
                this.setBlockBounds(COLLISION_BOXES[i]);
                super.addCollisionBoxesToList(world, x, y, z, boundingBox, list, entity);
            }
        }

        if(side.offsetX + side.offsetY + side.offsetZ < 0) {
            this.setBlockBounds(side.offsetX * -0.125f, side.offsetY * -0.125f, side.offsetZ * -0.125f, 1f, 1f, 1f);
        } else {
            this.setBlockBounds(0f, 0f, 0f, 1 - side.offsetX * 0.125f, 1 - side.offsetY * 0.125f, 1 - side.offsetZ * 0.125f);
        }
        super.addCollisionBoxesToList(world, x, y, z, boundingBox, list, entity);

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int metadata = world.getBlockMetadata(x, y, z);

        ForgeDirection side = ForgeDirection.getOrientation(metadata);

        float yaw = MathHelper.wrapAngleTo180_float(entity.rotationYaw);
        float pitch = entity.rotationPitch;

        boolean flipped;
        if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) {
            yaw += 180;
            flipped = yaw > 315 || yaw < 45 || (yaw < 225 && yaw > 135);
        } else {
            switch (side.getOpposite()) {
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

        metadata |= flipped ? 8 : 0;
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
