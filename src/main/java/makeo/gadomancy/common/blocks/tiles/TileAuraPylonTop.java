package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 13.11.2015 00:01
 */
public class TileAuraPylonTop extends SynchronizedTileEntity {

    private boolean shouldRender = false;

    @Override
    public void updateEntity() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if(!worldObj.isRemote) {
            if(te == null || !(te instanceof TileAuraPylon)) {
                breakTile();
                return;
            }
            TileAuraPylon pylon = (TileAuraPylon) te;
            if(pylon.isPartOfMultiblock() && !pylon.isMasterTile()) breakTile();
        } else {
            shouldRender = !(te == null || !(te instanceof TileAuraPylon));
        }
    }

    public Aspect getAspect() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if(te == null || !(te instanceof TileAuraPylon)) {
            return null;
        }
        return ((TileAuraPylon) te).getAspectType();
    }

    private void breakTile() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        Block pylon = worldObj.getBlock(xCoord, yCoord, zCoord);
        if(pylon != null) {
            ArrayList<ItemStack> stacks = pylon.getDrops(worldObj, xCoord, yCoord, zCoord, meta, 0);
            for(ItemStack i : stacks) {
                EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, i);
                ItemUtils.applyRandomDropOffset(item, worldObj.rand);
                worldObj.spawnEntityInWorld(item);
            }
        }
        worldObj.removeTileEntity(xCoord, yCoord, zCoord);
        worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean shouldRenderEffect() {
        return shouldRender;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

}
