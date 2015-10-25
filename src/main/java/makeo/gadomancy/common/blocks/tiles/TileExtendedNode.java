package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.utils.ExplosionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.items.ItemManaBean;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.tiles.TileNode;

import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 21.10.2015 23:39
 */
public class TileExtendedNode extends TileNode {

    private ExtendedNodeType extendedNodeType;
    private GrowingNodeBehavior behavior;

    private boolean doingVortexExplosion = false;
    private ExplosionHelper.VortexExplosion vortexExplosion;


    public TileExtendedNode() {
        this.behavior = new GrowingNodeBehavior(this);

        if(new Random().nextBoolean()) {
            this.extendedNodeType = ExtendedNodeType.GROWING;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if(this.doingVortexExplosion) {
            if(this.vortexExplosion == null) {
                this.vortexExplosion = new ExplosionHelper.VortexExplosion(this);
            }
            if(vortexExplosion.isFinished()) {
                this.vortexExplosion = null;
                this.doingVortexExplosion = false;
            } else {
                vortexExplosion.update();
            }
        }

        boolean needUpdate;

        needUpdate = handleGrowingNode(false);

        if(!worldObj.isRemote && needUpdate) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }
    }

    private boolean handleGrowingNode(boolean needUpdate) {
        if(extendedNodeType == null || extendedNodeType != ExtendedNodeType.GROWING) return needUpdate;

        List aspectOrbs = this.worldObj.getEntitiesWithinAABB(EntityAspectOrb.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(15.0D, 15.0D, 15.0D));
        if ((aspectOrbs != null) && (aspectOrbs.size() > 0)) {
            for (Object orb : aspectOrbs) {
                EntityAspectOrb aspectOrb = (EntityAspectOrb) orb;
                if ((aspectOrb.isEntityAlive()) && (!aspectOrb.isEntityInvulnerable())) {
                    double d = getDistanceTo(aspectOrb.posX, aspectOrb.posY, aspectOrb.posZ);
                    if (d < 0.5D) { //prev: 2.0D
                        Aspect aspect = aspectOrb.getAspect();

                        if (getAspects().getAmount(aspect) < getNodeVisBase(aspect)) {
                            addToContainer(aspect, 1);
                        } else {
                            //Adding it permanently
                            if(!worldObj.isRemote && behavior.doesAccept(aspect)) {
                                behavior.addAspect(GrowingNodeBehavior.AspectType.ASPECT_ORB, aspect, 1);
                            }
                        }
                        needUpdate = true;
                        aspectOrb.setDead();
                    }
                }
                applyMovementVectors(aspectOrb);
            }
        }
        List items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(15.0D, 15.0D, 15.0D));
        if ((items != null) && (items.size() > 0)) {
            for (Object item : items) {
                EntityItem entityItem = (EntityItem) item;
                Item it = entityItem.getEntityItem().getItem();
                if(!(it instanceof ItemWispEssence || it instanceof ItemManaBean)) continue;
                if ((entityItem.isEntityAlive()) && (!entityItem.isEntityInvulnerable())) {
                    double d = getDistanceTo(entityItem.posX, entityItem.posY, entityItem.posZ);
                    if (d < 0.5D) { //prev: 2.0D
                        IEssentiaContainerItem container = (IEssentiaContainerItem) it;
                        AspectList list = container.getAspects(entityItem.getEntityItem());
                        if(!(list == null || list.size() == 0)) {
                            Aspect aspect = list.getAspects()[0];
                            if (getAspects().getAmount(aspect) < getNodeVisBase(aspect)) {
                                addToContainer(aspect, 1);
                            } else {
                                //Adding it permanently
                                if(!worldObj.isRemote && behavior.doesAccept(aspect)) {
                                    GrowingNodeBehavior.AspectType type = it instanceof ItemWispEssence ? GrowingNodeBehavior.AspectType.WISP_ESSENCE : GrowingNodeBehavior.AspectType.MANA_BEAN;
                                    behavior.addAspect(type, aspect, 1);
                                }
                            }
                            needUpdate = true;
                        }
                        entityItem.setDead();
                    }
                }
                applyMovementVectors(entityItem);
            }
        }
        List wisps = this.worldObj.getEntitiesWithinAABB(EntityWisp.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(15.0D, 15.0D, 15.0D));
        if ((wisps != null) && (wisps.size() > 0)) {
            for (Object wispObj : wisps) {
                EntityWisp wisp = (EntityWisp) wispObj;
                if ((wisp.isEntityAlive()) && (!wisp.isEntityInvulnerable())) {
                    double d = getDistanceTo(wisp.posX, wisp.posY, wisp.posZ);
                    if (d < 0.5D) { //prev: 2.0D
                        Aspect aspect = Aspect.getAspect(wisp.getType());

                        if(aspect != null) {
                            if (getAspects().getAmount(aspect) < getNodeVisBase(aspect)) {
                                addToContainer(aspect, 1);
                            } else {
                                //Adding it permanently

                                if(!worldObj.isRemote && behavior.doesAccept(aspect)) {
                                    behavior.addAspect(GrowingNodeBehavior.AspectType.WISP, aspect, 1);
                                }
                            }
                            needUpdate = true;
                        }
                        wisp.setDead();
                    }
                }
                applyMovementVectors(wisp);
            }
        }
        return needUpdate;
    }

    private void applyMovementVectors(Entity entity) {
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

    public ExtendedNodeType getExtendedNodeType() {
        return extendedNodeType;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);

        NBTTagCompound compound = nbttagcompound.getCompoundTag("Gadomancy");

        String exName = compound.getString("exNodeType");
        if(exName != null && !exName.isEmpty()) {
            this.extendedNodeType = ExtendedNodeType.valueOf(exName);
        } else {
            this.extendedNodeType = null;
        }

        if(extendedNodeType != null && extendedNodeType == ExtendedNodeType.GROWING) {
            NBTTagCompound growingNodeBehavior = compound.getCompoundTag("NodeBehavior");
            behavior.readFromNBT(growingNodeBehavior);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);

        NBTTagCompound compound = new NBTTagCompound();

        if(extendedNodeType != null) {
            compound.setString("exNodeType", extendedNodeType.name());

            if(extendedNodeType == ExtendedNodeType.GROWING) {
                NBTTagCompound behaviorCompound = new NBTTagCompound();
                behavior.writeToNBT(behaviorCompound);
                compound.setTag("NodeBehavior", behaviorCompound);
            }
        }

        nbttagcompound.setTag("Gadomancy", compound);
    }

    public void triggerVortexExplosion() {
        doingVortexExplosion = true;
    }

}
