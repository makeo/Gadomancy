package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.registration.StickyJarInfo;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 15:42
 */
public class BlockStickyJar extends BlockJar implements IBlockTransparent {

    public BlockStickyJar() {
        setCreativeTab(null);
    }

    private IIcon iconTransparent;

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
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);
        iconTransparent = ir.registerIcon(Gadomancy.MODID + ":transparent");
    }

    @Override
    public IIcon getTransparentIcon() {
        return iconTransparent;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        super.setBlockBoundsBasedOnState(world, x, y, z);

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileStickyJar) {
            flipBlockBounds((TileStickyJar) tile);
        }
    }

    private void flipBlockBounds(TileStickyJar stickyJar) {
        ForgeDirection placedOn = stickyJar.placedOn;

        if (placedOn != ForgeDirection.DOWN) {
            float minX = (float) getBlockBoundsMinX();
            float minY = (float) getBlockBoundsMinY();
            float minZ = (float) getBlockBoundsMinZ();
            float maxX = (float) getBlockBoundsMaxX();
            float maxY = (float) getBlockBoundsMaxY();
            float maxZ = (float) getBlockBoundsMaxZ();

            float temp;
            if (placedOn == ForgeDirection.NORTH || placedOn == ForgeDirection.SOUTH) {
                temp = minZ;
                minZ = minY;
                minY = temp;

                temp = maxZ;
                maxZ = maxY;
                maxY = temp;
            } else if (placedOn == ForgeDirection.WEST || placedOn == ForgeDirection.EAST) {
                temp = minX;
                minX = minY;
                minY = temp;

                temp = maxX;
                maxX = maxY;
                maxY = temp;
            }

            if (placedOn == ForgeDirection.UP || placedOn == ForgeDirection.SOUTH || placedOn == ForgeDirection.EAST) {
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileStickyJar) {
            ItemStack stack = ((TileStickyJar) tile).getParentBlock().getPickBlock(target, world, x, y, z, player);
            NBTHelper.getData(stack).setBoolean("isStickyJar", true);
            return stack;
        }
        return null;
    }

    @Override
    public TileStickyJar createTileEntity(World world, int metadata) {
        return new TileStickyJar();
    }

    @Override
    public int getRenderType() {
        return RegisteredBlocks.rendererTransparentBlock;
    }

    public void onBlockPlacedOn(TileStickyJar tile, EntityLivingBase player) {
        if (tile.placedOn != ForgeDirection.UP && tile.placedOn != ForgeDirection.DOWN) {

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
                tile.facing = pitch < 0 ? ForgeDirection.SOUTH.ordinal() : ForgeDirection.NORTH.ordinal();
            } else {
                tile.facing = yaw < 0 ? ForgeDirection.EAST.ordinal() : ForgeDirection.WEST.ordinal();
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase ent, ItemStack stack) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileStickyJar) {
            TileStickyJar stickyJar = (TileStickyJar) tile;

            if (stickyJar.isValid()) {
                stickyJar.getParentBlock().onBlockPlacedBy(world, x, y, z, ent, stack);
            }

            onBlockPlacedOn(stickyJar, ent);
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileStickyJar && ((TileStickyJar) tile).isValid()) {

            ((TileStickyJar) tile).getParent().validate(); //Ugh...
            world.setTileEntity(x, y, z, ((TileStickyJar) tile).getParent()); //Ugh. that fix...

            ArrayList<ItemStack> drops = ((TileStickyJar) tile).getParentBlock()
                    .getDrops(world, x, y, z, ((TileStickyJar) tile).getParentMetadata(), fortune);

            ((TileStickyJar) tile).getParent().invalidate(); //Uhm...
            world.setTileEntity(x, y, z, tile); //Revert. xD

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
    }

    public static void handlePhial(World world, int x, int y, int z, EntityPlayer player, ItemStack stack, TileJarFillable tile) {
        ItemEssence itemEssence = (ItemEssence) ConfigItems.itemEssence;

        AspectList aspects = itemEssence.getAspects(stack);
        if(aspects != null) {
            Map.Entry<Aspect, Integer> entry = aspects.aspects.entrySet().iterator().next();

            Aspect jarAspect = tile.aspectFilter == null ? tile.aspect : tile.aspectFilter;

            if((jarAspect == null || entry.getKey() == jarAspect) && tile.amount + entry.getValue() <= tile.maxAmount) {
                tile.addToContainer(entry.getKey(), entry.getValue());
                stack.stackSize--;

                ItemStack essenceStack = new ItemStack(itemEssence, 1, 0);
                if (!player.inventory.addItemStackToInventory(essenceStack)) {
                    world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, essenceStack));
                }
                player.inventoryContainer.detectAndSendChanges();
            }
        } else {
            Aspect aspect = tile.aspect;
            if(aspect != null && tile.takeFromContainer(aspect, 8)) {
                stack.stackSize--;

                ItemStack essenceStack = new ItemStack(itemEssence, 1, 1);
                itemEssence.setAspects(essenceStack, new AspectList().add(aspect, 8));

                if (!player.inventory.addItemStackToInventory(essenceStack)) {
                    world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, essenceStack));
                }
                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }
}
