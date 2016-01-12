package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.ItemUtils;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;

import java.util.ArrayList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 13.11.2015 00:01
 */
public class TileAuraPylonTop extends SynchronizedTileEntity implements IAspectContainer {

    public Orbital orbital;
    private boolean shouldRender = false;
    private boolean shouldRenderAura = false;

    @Override
    public void updateEntity() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if(!worldObj.isRemote) {
            if(te == null || !(te instanceof TileAuraPylon)) {
                breakTile();
                return;
            }
            if(worldObj.getBlock(xCoord, yCoord - 1, zCoord) != RegisteredBlocks.blockAuraPylon || worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord) != 0) {
                breakTile();
                return;
            }
            TileAuraPylon pylon = (TileAuraPylon) te;
            if(pylon.isPartOfMultiblock() && !pylon.isMasterTile()) breakTile();
        } else {
            shouldRender = te != null && te instanceof TileAuraPylon && ((TileAuraPylon) te).isPartOfMultiblock();
            shouldRenderAura = te != null && te instanceof TileAuraPylon && ((TileAuraPylon) te).getEssentiaAmount() > 0;
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

    public boolean shouldRenderAuraEffect() {
        return shouldRenderAura;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(orbital != null) {
            orbital.clearOrbitals();
            if(orbital.registered)
                EffectHandler.getInstance().unregisterOrbital(orbital);
        }
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public AspectList getAspects() {
        TileEntity master = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if(master == null || !(master instanceof TileAuraPylon)) return null;
        return ((TileAuraPylon) master).getAspects();
    }

    @Override
    public void setAspects(AspectList aspectList) {}

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return false;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        return 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }
}
