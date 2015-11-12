package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.blocks.BlockAuraPylon;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.ItemCrystalEssence;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 11.11.2015 14:30
 */
public class TileAuraPylon extends SynchronizedTileEntity implements IAspectContainer, IEssentiaTransport {

    //This TileEntity is designed to be used stacked with others of its kind - without limits.
    //The aspects the tiles contain are all provided in the top tile of the structure
    //The actual work is done in the top tile of the structure

    //The inner aspect type is defined by a piece of crystallized Essentia being checked by the master-tile
    //The master tile informs all the other tiles what aspect currently is in use.

    //All tiles check each 8 ticks if the structure is still complete/contains an IO- and a master-tile
    //Master tile updates its state and the state of all tiles every 4 ticks so Aspect information and item information are still valid.
    //Master tile updates effects all 8 ticks if there is an internal aspect present.
    //Input tile handles Essentia IO all 8 ticks and thus provides needed amount information.

    private EntityPermanentItem tcPermItem;
    private boolean isPartOfMultiblock = false;

    private int ticksExisted = 0;
    private Aspect holdingAspect;
    private int amount = 0;
    private int maxAmount = 16;
    private boolean isMasterTile;
    private boolean isInputTile;

    //Individual.
    @Override
    public void updateEntity() {
        ticksExisted++;

        if(!worldObj.isRemote) {
            if ((ticksExisted & 7) == 0) {
                if (checkComponents()) return;
            }

            if (isInputTile()) {
                handleIO();
            }
            if (isMasterTile()) {
                handleCrystal();
                if (holdingAspect != null) {
                    handleEffectDistribution();
                }
            }
        } else {
            if(isInputTile() && holdingAspect != null) {
                doEssentiaTrail();
            }
        }
    }

    //Client-Side input tile only!
    private void doEssentiaTrail() {
        if((ticksExisted & 1) == 0) return;
        TileAuraPylon masterTile = getMasterTile();
        if(masterTile == null) return;
        Thaumcraft.proxy.essentiaTrailFx(worldObj, xCoord, yCoord, zCoord, masterTile.xCoord, masterTile.yCoord, masterTile.zCoord, 10, holdingAspect.getColor(), 1);
    }

    //Special to masterTile only!
    private void handleEffectDistribution() {
        int count = 1;
        TileEntity iter = worldObj.getTileEntity(xCoord, yCoord - count, zCoord);
        while(iter != null && iter instanceof TileAuraPylon) {
            ((TileAuraPylon) iter).holdingAspect = holdingAspect;
            count++;
            worldObj.markBlockForUpdate(xCoord, yCoord - count, zCoord);
            iter.markDirty();
        }
    }

    //Special to masterTile only!
    private void handleCrystal() {
        if ((ticksExisted & 3) != 0) return;

        tcPermItem = searchForPermItem();

        if (holdingAspect == null) {
            if(tcPermItem == null) return;
            holdingAspect = ((ItemCrystalEssence) tcPermItem.getEntityItem().getItem()).getAspects(tcPermItem.getEntityItem()).getAspects()[0];
        } else {
            if(tcPermItem == null) {
                holdingAspect = null;
            }
        }
    }

    //Special to masterTile only!
    private EntityPermanentItem searchForPermItem() {
        if(tcPermItem != null) return tcPermItem;
        EntityPermanentItem tcPermItem = tryFindPermItem();
        if (tcPermItem == null) {
            tcPermItem = tryVortexPossibleItems();
        }
        return tcPermItem;
    }

    //Special to masterTile only!
    private EntityPermanentItem tryVortexPossibleItems() {
        TileAuraPylon io = getInputTile();
        if (io == null) return null;
        ChunkCoordinates rel = new ChunkCoordinates(xCoord, yCoord - (yCoord - io.yCoord), zCoord);
        List entityItems = worldObj.selectEntitiesWithinAABB(EntityItem.class,
                AxisAlignedBB.getBoundingBox(rel.posX - 0.5, rel.posY - 0.5, rel.posZ - 0.5, rel.posX + 0.5, rel.posY + 0.5, rel.posZ + 0.5).expand(8, 8, 8), new IEntitySelector() {
                    @Override
                    public boolean isEntityApplicable(Entity e) {
                        return !(e instanceof EntityPermanentItem) && !(e instanceof EntitySpecialItem) &&
                                e instanceof EntityItem && ((EntityItem) e).getEntityItem() != null &&
                                ((EntityItem) e).getEntityItem().getItem() instanceof ItemCrystalEssence;
                    }
                });
        Entity dummy = new EntityItem(worldObj);
        dummy.posX = rel.posX + 0.5;
        dummy.posY = rel.posY + 0.5;
        dummy.posZ = rel.posZ + 0.5;

        //MC code.
        EntityItem entity = null;
        double d0 = Double.MAX_VALUE;
        for (Object entityItem : entityItems) {
            EntityItem entityIt = (EntityItem) entityItem;
            if (entityIt != dummy) {
                double d1 = dummy.getDistanceSqToEntity(entityIt);
                if (d1 <= d0) {
                    entity = entityIt;
                    d0 = d1;
                }
            }
        }
        if(entity == null) return null;
        if(dummy.getDistanceSqToEntity(entity) < 0.2) {
            EntityPermanentItem item = new EntityPermanentItem(entity.worldObj, entity.posX, entity.posY, entity.posZ, entity.getEntityItem());
            entity.worldObj.spawnEntityInWorld(item);
            item.setVelocity(0, 0, 0);
            item.hoverStart = entity.hoverStart;
            item.age = entity.age;
            entity.setDead();
            return item;
        } else {
            applyMovementVectors(entity);
        }
        return null;
    }

    //Special to masterTile only!
    private void applyMovementVectors(EntityItem entity) {
        double var3 = (this.xCoord + 0.5D - entity.posX) / 15.0D;
        double var5 = (this.yCoord + 0.5D - entity.posY) / 15.0D;
        double var7 = (this.zCoord + 0.5D - entity.posZ) / 15.0D;
        double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
        double var11 = 1.0D - var9;
        if (var11 > 0.0D) {
            var11 *= var11;
            entity.motionX += var3 / var9 * var11 * 0.15D;
            entity.motionY += var5 / var9 * var11 * 0.25D;
            entity.motionZ += var7 / var9 * var11 * 0.15D;
        }
    }

    //Special to masterTile only!
    private EntityPermanentItem tryFindPermItem() {
        TileAuraPylon io = getInputTile();
        if (io == null) return null;
        ChunkCoordinates rel = new ChunkCoordinates(xCoord, yCoord - (yCoord - io.yCoord), zCoord);
        Entity dummy = new EntityItem(worldObj);
        dummy.posX = rel.posX + 0.5;
        dummy.posY = rel.posY + 0.5;
        dummy.posZ = rel.posZ + 0.5;
        Entity permItem = worldObj.findNearestEntityWithinAABB(EntityPermanentItem.class,
                AxisAlignedBB.getBoundingBox(rel.posX - 0.5, rel.posY - 0.5, rel.posZ - 0.5, rel.posX + 0.5, rel.posY + 0.5, rel.posZ + 0.5), dummy);
        if (permItem == null) return null;
        if (((EntityPermanentItem) permItem).getEntityItem() != null && ((EntityPermanentItem) permItem).getEntityItem().getItem() instanceof ItemCrystalEssence) {
            return (EntityPermanentItem) permItem;
        }
        return null;
    }

    //Special to inputTile only!
    private void handleIO() {
        if ((!worldObj.isRemote) && ((ticksExisted & 7) == 0) && (getEssentiaAmount() < getMaxAmount())) {
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.DOWN);
            if (te != null) {
                IEssentiaTransport ic = (IEssentiaTransport) te;
                if (!ic.canOutputTo(ForgeDirection.UP)) {
                    return;
                }

                if ((holdingAspect != null) && (ic.getSuctionAmount(ForgeDirection.UP) < getSuctionAmount(ForgeDirection.DOWN))) {
                    addToContainer(holdingAspect, ic.takeEssentia(holdingAspect, 1, ForgeDirection.UP));
                }
            }
        }
    }

    //Individual.
    //Returns true, if state has changed, false if still complete state.
    private boolean checkComponents() {
        TileAuraPylon te = getMasterTile();
        if (te == null) {
            breakTile();
            return true;
        }
        te = getInputTile();
        if (te == null) {
            breakTile();
            return true;
        }
        if(!hasTopTile()) {
            breakTile();
            return true;
        }
        return false;
    }

    private boolean hasTopTile() {
        TileAuraPylon master = getMasterTile();
        if(master == null) return false;
        TileEntity te = worldObj.getTileEntity(master.xCoord, master.yCoord + 1, master.zCoord);
        return !(te == null || !(te instanceof TileAuraPylonTop));
    }

    //Individual.
    private void breakTile() {
        if(!isPartOfMultiblock || worldObj.isRemote) return;

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

    //Individual
    public boolean isPartOfMultiblock() {
        return isPartOfMultiblock;
    }

    //Individual.
    public void setPartOfMultiblock(boolean isPartOfMultiblock) {
        this.isPartOfMultiblock = isPartOfMultiblock;
    }

    //Individual.
    @Override
    public boolean canUpdate() {
        return true;
    }

    //Individual.
    public void setTileInformation(boolean isMaster, boolean isInput) {
        this.isMasterTile = isMaster;
        this.isInputTile = isInput;
    }

    //Individual.
    public boolean isInputTile() {
        return isInputTile;
    }

    //Individual.
    public TileAuraPylon getInputTile() {
        if (isInputTile()) return this;
        TileEntity superTile = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (superTile == null || !(superTile instanceof TileAuraPylon)) return null;
        return ((TileAuraPylon) superTile).getInputTile();
    }

    //Individual.
    public boolean isMasterTile() {
        return isMasterTile;
    }

    //Individual.
    public TileAuraPylon getMasterTile() {
        if (isMasterTile()) return this;
        TileEntity superTile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        if (superTile == null || !(superTile instanceof TileAuraPylon)) return null;
        return ((TileAuraPylon) superTile).getMasterTile();
    }

    //Dynamic.
    public int getEssentiaAmount() {
        TileAuraPylon io = getInputTile();
        if (io == null) return 0;
        return io.amount;
    }

    //Dynamic.
    public Aspect getAspectType() {
        TileAuraPylon io = getInputTile();
        if (io == null) return null;
        return io.holdingAspect;
    }

    //Dynamic.
    public int getMaxAmount() {
        TileAuraPylon io = getInputTile();
        if (io == null) return 0;
        int max = io.maxAmount;
        int counter = 1;
        TileEntity superTile = worldObj.getTileEntity(xCoord, yCoord + counter, zCoord);
        while (superTile != null && superTile instanceof TileAuraPylon) {
            max += ((TileAuraPylon) superTile).maxAmount;
            counter++;
            superTile = worldObj.getTileEntity(xCoord, yCoord + counter, zCoord);
        }
        return max;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        this.isInputTile = compound.getBoolean("input");
        this.isMasterTile = compound.getBoolean("master");

        String tag = compound.getString("aspect");
        if (tag != null && !tag.equals("")) {
            this.holdingAspect = Aspect.getAspect(tag);
        }
        this.amount = compound.getInteger("amount");
        this.maxAmount = compound.getInteger("maxAmount");
        this.isPartOfMultiblock = compound.getBoolean("partOfMultiblock");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setBoolean("input", isInputTile);
        compound.setBoolean("master", isMasterTile);
        if (holdingAspect != null) {
            compound.setString("aspect", holdingAspect.getTag());
        }
        compound.setInteger("amount", amount);
        compound.setInteger("maxAmount", maxAmount);
        compound.setBoolean("partOfMultiblock", isPartOfMultiblock);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // IAspectContainer
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //Dynamic.
    @Override
    public AspectList getAspects() {
        TileAuraPylon io = getInputTile();
        AspectList al = new AspectList();
        if (io != null && io.holdingAspect != null && io.amount > 0) {
            al.add(io.holdingAspect, io.amount);
        }
        return al;
    }

    //NO-OP
    @Override
    public void setAspects(AspectList aspectList) {
    }

    //Individual.
    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return isInputTile() && holdingAspect != null && holdingAspect.equals(aspect);
    }

    //Individual.
    @Override
    public int addToContainer(Aspect aspect, int amount) {
        if (amount == 0) {
            return amount;
        }

        if (!isInputTile()) return 0;
        if (aspect == null) return 0;

        if (holdingAspect != null && this.amount < maxAmount && aspect == holdingAspect) {
            int added = Math.min(amount, this.maxAmount - this.amount);
            this.amount += added;
            amount -= added;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            markDirty();
        }
        return amount;
    }

    //Individual.
    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        if (!isInputTile()) return false;
        if (aspect == null || holdingAspect == null) return false;

        if (this.amount >= i && holdingAspect.equals(aspect)) {
            this.amount -= i;
            if (this.amount <= 0) {
                this.holdingAspect = null;
                this.amount = 0;
            }
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            markDirty();
            return true;
        }
        return false;
    }

    //Individual.
    //You may only extract 1 at a time.
    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    //Individual.
    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return isInputTile() && holdingAspect != null && this.amount >= i && aspect == holdingAspect;
    }

    //Individual.
    @Override
    public boolean doesContainerContain(AspectList list) {
        if (!isInputTile()) return false;
        if (holdingAspect == null) return false;

        for (Aspect a : list.getAspects()) if ((this.amount > 0) && (a == holdingAspect)) return true;
        return false;
    }

    //Individual.
    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // IEssentiaTransport
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //Individual.
    @Override
    public boolean isConnectable(ForgeDirection face) {
        return isInputTile() && face == ForgeDirection.DOWN;
    }

    //Individual.
    @Override
    public boolean canInputFrom(ForgeDirection face) {
        return isInputTile() && face == ForgeDirection.DOWN;
    }

    //Individual.
    @Override
    public boolean canOutputTo(ForgeDirection face) {
        return false;
    }

    //NO-OP
    @Override
    public void setSuction(Aspect aspect, int i) {
    }

    //Individual.
    @Override
    public Aspect getSuctionType(ForgeDirection forgeDirection) {
        return isInputTile() ? holdingAspect : null;
    }

    @Override
    public int getSuctionAmount(ForgeDirection forgeDirection) {
        if (!isInputTile()) return 0;
        if (holdingAspect == null) return 0;
        return getMinimumSuction();
    }

    @Override
    public int takeEssentia(Aspect aspect, int amt, ForgeDirection direction) {
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amt, ForgeDirection direction) {
        if (!isInputTile()) return 0;
        return canInputFrom(direction) ? amount - addToContainer(aspect, amount) : 0;
    }

    //Individual.
    @Override
    public Aspect getEssentiaType(ForgeDirection forgeDirection) {
        return isInputTile() ? holdingAspect : null;
    }

    //Individual.
    @Override
    public int getEssentiaAmount(ForgeDirection forgeDirection) {
        return isInputTile() ? amount : 0;
    }

    @Override
    public int getMinimumSuction() {
        return 128;
    }

    //Individual.
    @Override
    public boolean renderExtendedTube() {
        return false;
    }
}
