package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.registration.StickyJarInfo;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.devices.BlockJar;
import thaumcraft.common.items.resources.ItemPhial;
import thaumcraft.common.tiles.essentia.TileJarFillable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 15:42
 */
public class BlockStickyJar extends BlockJar {

    public BlockStickyJar() {
        setCreativeTab(null);
    }

    /*private IIcon iconTransparent;

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof TileStickyJar) {
            TileStickyJar stickyJar = (TileStickyJar) tile;
            if(stickyJar.isValid()) {
                return stickyJar.getParentBlock().getIcon(world, x, y, z, side);
            }
        }
        return iconTransparent;
    }*/

    /*@Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);
        iconTransparent = ir.registerIcon(Gadomancy.MODID + ":transparent");
    }*/

    /*@Override
    public IIcon getTransparentIcon() {
        return iconTransparent;
    }*/

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        super.setBlockBoundsBasedOnState(worldIn, pos);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileStickyJar) {
            flipBlockBounds((TileStickyJar) tile);
        }
    }

    /*public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        super.setBlockBoundsBasedOnState(world, x, y, z);

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileStickyJar) {
            flipBlockBounds((TileStickyJar) tile);
        }
    }*/

    private void flipBlockBounds(TileStickyJar stickyJar) {
        EnumFacing placedOn = stickyJar.placedOn;

        if (placedOn != EnumFacing.DOWN) {
            float minX = (float) getBlockBoundsMinX();
            float minY = (float) getBlockBoundsMinY();
            float minZ = (float) getBlockBoundsMinZ();
            float maxX = (float) getBlockBoundsMaxX();
            float maxY = (float) getBlockBoundsMaxY();
            float maxZ = (float) getBlockBoundsMaxZ();

            float temp;
            if (placedOn == EnumFacing.NORTH || placedOn == EnumFacing.SOUTH) {
                temp = minZ;
                minZ = minY;
                minY = temp;

                temp = maxZ;
                maxZ = maxY;
                maxY = temp;
            } else if (placedOn == EnumFacing.WEST || placedOn == EnumFacing.EAST) {
                temp = minX;
                minX = minY;
                minY = temp;

                temp = maxX;
                maxX = maxY;
                maxY = temp;
            }

            if (placedOn == EnumFacing.UP || placedOn == EnumFacing.SOUTH || placedOn == EnumFacing.EAST) {
                minX = 1 - minX;
                minY = 1 - minY;
                minZ = 1 - minZ;
                maxX = 1 - maxX;
                maxY = 1 - maxY;
                maxZ = 1 - maxZ;

                temp = minX;
                minX = maxX;
                maxX = temp;

                temp = minY;
                minY = maxY;
                maxY = temp;

                temp = minZ;
                minZ = maxZ;
                maxZ = temp;
            }

            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileStickyJar) {
            ItemStack stack = ((TileStickyJar) tile).getParentBlock().getPickBlock(target, world, pos, player);
            NBTHelper.getData(stack).setBoolean("isStickyJar", true);
            return stack;
        }
        return null;
    }

    /*@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileStickyJar) {
            ItemStack stack = ((TileStickyJar) tile).getParentBlock().getPickBlock(target, world, x, y, z, player);
            NBTHelper.getData(stack).setBoolean("isStickyJar", true);
            return stack;
        }
        return null;
    }*/

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileStickyJar();
    }

    /*@Override
    public TileStickyJar createTileEntity(World world, int metadata) {
        return new TileStickyJar();
    }*/

    @Override
    public int getRenderType() {
        return 2; //TESR
        //return RegisteredBlocks.rendererTransparentBlock;
    }

    public void onBlockPlacedOn(TileStickyJar tile, EntityLivingBase player) {
        if (tile.placedOn != EnumFacing.UP && tile.placedOn != EnumFacing.DOWN) {

            float pitch = player.rotationPitch;

            float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw);

            switch (tile.placedOn) {
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

            if (Math.abs(yaw) < Math.abs(pitch)) {
                tile.facing = pitch < 0 ? EnumFacing.SOUTH.ordinal() : EnumFacing.NORTH.ordinal();
            } else {
                tile.facing = yaw < 0 ? EnumFacing.EAST.ordinal() : EnumFacing.WEST.ordinal();
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileStickyJar) {
            TileStickyJar stickyJar = (TileStickyJar) tile;

            if (stickyJar.isValid()) {
                stickyJar.getParentBlock().onBlockPlacedBy(world, pos, state, ent, stack);
            }

            onBlockPlacedOn(stickyJar, ent);
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileStickyJar && ((TileStickyJar) tile).isValid() && world instanceof World) {

            ((TileStickyJar) tile).getParent().validate(); //Ugh...
            ((World) world).setTileEntity(pos, ((TileStickyJar) tile).getParent()); //Ugh. that fix...

            List<ItemStack> drops = ((TileStickyJar) tile).getParentBlock()
                    .getDrops(world, pos, ((TileStickyJar) tile).getParentState(), fortune);

            ((TileStickyJar) tile).getParent().invalidate(); //Uhm...
            ((World) world).setTileEntity(pos, tile); //Revert. xD

            boolean found = false;
            for (ItemStack drop : drops) {
                if (RegisteredItems.isStickyableJar(drop)) {
                    NBTHelper.getData(drop).setBoolean("isStickyJar", true);
                    found = true;
                    break;
                }
            }

            if (!found) {
                ItemStack item = new ItemStack(Items.slime_ball, 1);
                drops.add(item);
            }
            return drops;
        }
        return new ArrayList<ItemStack>();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float these, float are, float variables) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileStickyJar) {
            TileStickyJar tileStickyJar = (TileStickyJar) tile;

            StickyJarInfo info = RegisteredBlocks.getStickyJarInfo(state);

            ItemStack heldItem = player.getHeldItem();

            if (info.needsPhialHandling() && heldItem != null && heldItem.getItem() == ItemsTC.phial) {
                if(!world.isRemote) {
                    handlePhial(world, pos, player, heldItem, tileStickyJar);
                }
                return true;
            }

            boolean handleAspectFilter = info.needsLabelHandling() && heldItem != null && heldItem.getItem() == ItemsTC.label
                    && heldItem.getItemDamage() == 13 && tileStickyJar.aspectFilter == null
                    && (((IEssentiaContainerItem) heldItem.getItem()).getAspects(heldItem) != null || tileStickyJar.amount != 0);

            EnumFacing newDir = tileStickyJar.changeDirection(side);
            boolean result = ((TileStickyJar) tile).getParentBlock().onBlockActivated(world, pos, state, player, newDir, these, are, variables);

            if (handleAspectFilter) {
                tileStickyJar.facing = newDir.ordinal();
                tileStickyJar.markDirty();
            }

            return result;
        }
        return false;
    }

    /*@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float these, float are, float variables) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileStickyJar) {
            TileStickyJar tileStickyJar = (TileStickyJar) tile;

            StickyJarInfo info = RegisteredBlocks.getStickyJarInfo(tileStickyJar.getParentBlock(), world.getBlockMetadata(x, y, z));

            ItemStack heldItem = player.getHeldItem();

            if (info.needsPhialHandling() && heldItem != null && heldItem.getItem() == ConfigItems.itemEssence) {
                if(!world.isRemote) {
                    handlePhial(world, x, y, z, player, heldItem, tileStickyJar);
                }
                return true;
            }

            boolean handleAspectFilter = info.needsLabelHandling() && heldItem != null && heldItem.getItem() == ConfigItems.itemResource
                    && heldItem.getItemDamage() == 13 && tileStickyJar.aspectFilter == null
                    && (((IEssentiaContainerItem) heldItem.getItem()).getAspects(heldItem) != null || tileStickyJar.amount != 0);

            ForgeDirection newDir = tileStickyJar.changeDirection(ForgeDirection.getOrientation(side));
            boolean result = ((TileStickyJar) tile).getParentBlock().onBlockActivated(world, x, y, z, player, newDir.ordinal(), these, are, variables);

            if (handleAspectFilter) {
                tileStickyJar.facing = newDir.ordinal();
                tileStickyJar.markDirty();
            }

            return result;
        }
        return false;
    }*/

    public static void handlePhial(World world, BlockPos pos, EntityPlayer player, ItemStack stack, TileJarFillable tile) {
        ItemPhial phial = (ItemPhial) ItemsTC.phial;

        AspectList aspects = phial.getAspects(stack);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if(aspects != null) {
            Map.Entry<Aspect, Integer> entry = aspects.aspects.entrySet().iterator().next();

            Aspect jarAspect = tile.aspectFilter == null ? tile.aspect : tile.aspectFilter;

            if((jarAspect == null || entry.getKey() == jarAspect) && tile.amount + entry.getValue() <= tile.maxAmount) {
                tile.addToContainer(entry.getKey(), entry.getValue());
                stack.stackSize--;

                ItemStack essenceStack = new ItemStack(phial, 1, 0);
                if (!player.inventory.addItemStackToInventory(essenceStack)) {
                    world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, essenceStack));
                }
                player.inventoryContainer.detectAndSendChanges();
            }
        } else {
            Aspect aspect = tile.aspect;
            if(aspect != null && tile.takeFromContainer(aspect, 8)) {
                stack.stackSize--;

                ItemStack essenceStack = new ItemStack(phial, 1, 1);
                phial.setAspects(essenceStack, new AspectList().add(aspect, 8));

                if (!player.inventory.addItemStackToInventory(essenceStack)) {
                    world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, essenceStack));
                }
                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }
}
