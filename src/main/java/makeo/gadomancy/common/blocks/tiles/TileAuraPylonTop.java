package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 13.11.2015 00:01
 */
public class TileAuraPylonTop extends SynchronizedTileEntity implements IAspectContainer, ITickable {

    public Orbital orbital;
    private boolean shouldRender = false;
    private boolean shouldRenderAura = false;

    @Override
    public void update() {
        BlockPos oneDown = getPos().add(0, -1, 0);
        TileEntity te = worldObj.getTileEntity(oneDown);
        if(!worldObj.isRemote) {
            if(te == null || !(te instanceof TileAuraPylon)) {
                breakTile();
                return;
            }
            IBlockState state = worldObj.getBlockState(oneDown);
            if(!state.getBlock().equals(RegisteredBlocks.blockAuraPylon) || state.getBlock().getMetaFromState(state) != 0) {
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
        TileEntity te = worldObj.getTileEntity(getPos().add(0, -1, 0));
        if(te == null || !(te instanceof TileAuraPylon)) {
            return null;
        }
        return ((TileAuraPylon) te).getAspectType();
    }

    private void breakTile() {
        IBlockState state = worldObj.getBlockState(getPos());
        if(state.getBlock() != null) {
            List<ItemStack> stacks = state.getBlock().getDrops(worldObj, getPos(), state, 0);
            double xCoord = getPos().getX();
            double yCoord = getPos().getY();
            double zCoord = getPos().getZ();
            for(ItemStack i : stacks) {
                EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, i);
                worldObj.spawnEntityInWorld(item);
            }
        }
        worldObj.removeTileEntity(getPos());
        worldObj.setBlockToAir(getPos());
        worldObj.markBlockForUpdate(getPos());
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
    public AspectList getAspects() {
        TileEntity master = worldObj.getTileEntity(getPos().add(0, -1, 0));
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
