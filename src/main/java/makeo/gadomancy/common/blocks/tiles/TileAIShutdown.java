package makeo.gadomancy.common.blocks.tiles;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.registration.AIShutdownWhitelist;
import makeo.gadomancy.common.registration.RegisteredMultiblocks;
import makeo.gadomancy.common.utils.Injector;
import makeo.gadomancy.common.utils.MultiblockHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 08.06.2016 22:56
 */
public class TileAIShutdown extends SynchronizedTileEntity implements IAspectContainer, IEssentiaTransport {

    private static final Random RAND = new Random();

    private static Map<ChunkCoordinates, List<AffectedEntity>> trackedEntities = Maps.newHashMap();
    private static AxisAlignedBB BOX = AxisAlignedBB.getBoundingBox(-3, -1, -3, 4, 2, 4);
    private static Injector injEntityLivingBase = new Injector(EntityLivingBase.class);

    public static final int MAX_AMT = 16;

    public Orbital orbital;

    private int ticksExisted = 0;
    private int storedAmount = 0;

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if(worldObj == null) return;
        ticksExisted++;

        if(!worldObj.isRemote) {
            ChunkCoordinates cc = getCoords();
            if (!trackedEntities.containsKey(cc)) trackedEntities.put(cc, Lists.<AffectedEntity>newLinkedList());

            if ((ticksExisted & 15) == 0) {
                killAI();
            }
            if (((ticksExisted & 7) == 0)) {
                handleIO();
            }
            if (((ticksExisted & 31) == 0)) {
                drainDefaultEssentia();
            }
        }

    }

    private void drainDefaultEssentia() {
        storedAmount = Math.max(0, storedAmount - 1);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    private void handleIO() {
        if (storedAmount < MAX_AMT) {
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.UP);
            if (te != null) {
                IEssentiaTransport ic = (IEssentiaTransport) te;
                if (!ic.canOutputTo(ForgeDirection.DOWN)) {
                    return;
                }

                if (ic.getSuctionAmount(ForgeDirection.DOWN) < getSuctionAmount(ForgeDirection.UP)) {
                    addToContainer(Aspect.ENTROPY, ic.takeEssentia(Aspect.ENTROPY, 1, ForgeDirection.DOWN));
                }
            }
        }
    }

    private void killAI() {
        ChunkCoordinates cc = getCoords();
        List objEntityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, BOX.copy().offset(xCoord, yCoord, zCoord));
        for (Object o : objEntityList) {
            if(o != null && o instanceof EntityLiving &&
                    !((EntityLiving) o).isDead && canAffect((EntityLiving) o)) {
                EntityLiving el = (EntityLiving) o;
                if(storedAmount <= 0) return;
                AffectedEntity affected = removeAI(el);
                trackedEntities.get(cc).add(affected);
            }
        }
    }

    private AffectedEntity removeAI(EntityLiving el) {
        if(RAND.nextInt(4) == 0) {
            storedAmount = Math.max(0, storedAmount - 1);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }

        UUID uu = el.getUniqueID();
        List<EntityAITasks.EntityAITaskEntry> tasks =
                new ArrayList<EntityAITasks.EntityAITaskEntry>(el.tasks.taskEntries);
        List<EntityAITasks.EntityAITaskEntry> targetTasks =
                new ArrayList<EntityAITasks.EntityAITaskEntry>(el.targetTasks.taskEntries);

        List<Class<? extends EntityAIBase>> entries = AIShutdownWhitelist.getWhitelistedAIClasses(el);

        Iterator iterator = el.tasks.taskEntries.iterator();
        while (iterator.hasNext()) {
            Object entry = iterator.next();
            if (entry == null || !(entry instanceof EntityAITasks.EntityAITaskEntry)) continue;
            boolean needsRemoval = true;
            for (Class<? extends EntityAIBase> aiClass : entries) {
                if(aiClass.isAssignableFrom(((EntityAITasks.EntityAITaskEntry) entry).action.getClass())) needsRemoval = false;
            }
            if(needsRemoval) {
                iterator.remove();
            }
        }

        iterator = el.targetTasks.taskEntries.iterator();
        while (iterator.hasNext()) {
            Object entry = iterator.next();
            if (entry == null || !(entry instanceof EntityAITasks.EntityAITaskEntry)) continue;
            boolean needsRemoval = true;
            for (Class<? extends EntityAIBase> aiClass : entries) {
                if(aiClass.isAssignableFrom(((EntityAITasks.EntityAITaskEntry) entry).action.getClass())) needsRemoval = false;
            }
            if(needsRemoval) {
                iterator.remove();
            }
        }
        injEntityLivingBase.setObject(el);
        injEntityLivingBase.setField("ignoreCollisions", true);
        injEntityLivingBase.setObject(null);
        return new AffectedEntity(uu, tasks, targetTasks);
    }

    public int getStoredEssentia() {
        return storedAmount;
    }

    public boolean canAffect(EntityLiving el) {
        ChunkCoordinates cc = getCoords();
        if(!trackedEntities.containsKey(cc)) return false;
        UUID uu = el.getUniqueID();
        for (AffectedEntity ae : trackedEntities.get(cc)) {
            if(ae.eUUID.equals(uu)) return false;
        }
        return true;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        this.storedAmount = compound.getInteger("amount");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("amount", storedAmount);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // IAspectContainer & IEssentiaTransport
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AspectList getAspects() {
        if(storedAmount <= 0) return new AspectList();
        return new AspectList().add(Aspect.ENTROPY, storedAmount);
    }

    @Override
    public void setAspects(AspectList list) {}

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspect == Aspect.ENTROPY;
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        if (amount == 0) {
            return amount;
        }

        if (aspect == null) return 0;

        if (storedAmount < MAX_AMT) {
            int added = Math.min(amount, MAX_AMT - storedAmount);
            storedAmount += added;
            amount -= added;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            markDirty();
        }
        return amount;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    @Deprecated
    public boolean takeFromContainer(AspectList list) {
        return false; //NO-OP
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return false;
    }

    @Override
    @Deprecated
    public boolean doesContainerContain(AspectList list) {
        return false; //NO-OP
    }

    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }

    @Override
    public boolean isConnectable(ForgeDirection direction) {
        return direction != null && direction.equals(ForgeDirection.UP);
    }

    @Override
    public boolean canInputFrom(ForgeDirection direction) {
        return isConnectable(direction);
    }

    @Override
    public boolean canOutputTo(ForgeDirection direction) {
        return isConnectable(direction);
    }

    @Override
    public void setSuction(Aspect aspect, int i) {}

    @Override
    public Aspect getSuctionType(ForgeDirection direction) {
        if(!isConnectable(direction)) return null;
        return Aspect.ENTROPY;
    }

    @Override
    public int getSuctionAmount(ForgeDirection direction) {
        if(!isConnectable(direction)) return 0;
        return getMinimumSuction();
    }

    @Override
    public int takeEssentia(Aspect aspect, int i, ForgeDirection direction) {
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int i, ForgeDirection direction) {
        return canInputFrom(direction) ? addToContainer(aspect, i) : 0;
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection direction) {
        return Aspect.ENTROPY;
    }

    @Override
    public int getEssentiaAmount(ForgeDirection direction) {
        if(!isConnectable(direction)) return 0;
        return storedAmount;
    }

    @Override
    public int getMinimumSuction() {
        return 64;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }

    //Tracker methods

    public static void removeTrackedEntity(EntityLiving entityLiving) {
        for (ChunkCoordinates cc : trackedEntities.keySet()) {
            for (AffectedEntity ae : trackedEntities.get(cc)) {
                if(ae.eUUID.equals(entityLiving.getUniqueID())) {
                    trackedEntities.get(cc).remove(ae);
                    entityLiving.tasks.taskEntries = ae.tasks;
                    entityLiving.targetTasks.taskEntries = ae.targetTasks;
                    injEntityLivingBase.setObject(entityLiving);
                    injEntityLivingBase.setField("ignoreCollisions", false);
                    injEntityLivingBase.setObject(null);
                    return;
                }
            }
        }
    }

    public static void removeTrackedEntities(World world, int x, int y, int z) {
        ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
        if(trackedEntities.containsKey(cc)) {
            for (AffectedEntity ae : trackedEntities.get(cc)) {
                for (Object objE : world.getLoadedEntityList()) {
                    if(objE != null && objE instanceof EntityLiving &&
                            !((EntityLiving) objE).isDead &&
                            ((EntityLiving) objE).getUniqueID().equals(ae.eUUID)) {
                        ((EntityLiving) objE).tasks.taskEntries = ae.tasks;
                        ((EntityLiving) objE).targetTasks.taskEntries = ae.targetTasks;
                        injEntityLivingBase.setObject(objE);
                        injEntityLivingBase.setField("ignoreCollisions", false);
                        injEntityLivingBase.setObject(null);
                    }
                }
            }
            trackedEntities.remove(new ChunkCoordinates(x, y, z));
        }
    }

    public static class AffectedEntity {

        public final UUID eUUID;
        public List<EntityAITasks.EntityAITaskEntry> tasks;
        public List<EntityAITasks.EntityAITaskEntry> targetTasks;

        public AffectedEntity(UUID eUUID,
                              List<EntityAITasks.EntityAITaskEntry> tasks,
                              List<EntityAITasks.EntityAITaskEntry> targetTasks) {
            this.eUUID = eUUID;
            this.tasks = tasks;
            this.targetTasks = targetTasks;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AffectedEntity that = (AffectedEntity) o;
            return !(eUUID != null ? !eUUID.equals(that.eUUID) : that.eUUID != null);
        }

        @Override
        public int hashCode() {
            return eUUID != null ? eUUID.hashCode() : 0;
        }

    }

}
