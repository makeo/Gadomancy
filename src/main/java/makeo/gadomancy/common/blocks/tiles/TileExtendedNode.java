package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityWisp;
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

    public boolean growing = new Random().nextBoolean();
    private GrowingNodeBehavior behavior = new GrowingNodeBehavior();

    @Override
    public void updateEntity() {
        super.updateEntity();

        boolean needUpdate;

        needUpdate = handleGrowingNode(false);

        if(!worldObj.isRemote && needUpdate) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
            System.out.println(needUpdate);
        }
    }

    private boolean handleGrowingNode(boolean needUpdate) {
        if(!growing) return needUpdate;
        List aspectOrbs = this.worldObj.getEntitiesWithinAABB(EntityAspectOrb.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(15.0D, 15.0D, 15.0D));
        if ((aspectOrbs != null) && (aspectOrbs.size() > 0)) {
            for (Object orb : aspectOrbs) {
                EntityAspectOrb aspectOrb = (EntityAspectOrb) orb;
                if ((aspectOrb.isEntityAlive()) && (!aspectOrb.isEntityInvulnerable())) {
                    double d = getDistanceTo(aspectOrb.posX, aspectOrb.posY, aspectOrb.posZ);
                    if (d < 1.0D) { //prev: 2.0D
                        Aspect aspect = aspectOrb.getAspect();

                        if (getAspects().getAmount(aspect) < getNodeVisBase(aspect)) {
                            addToContainer(aspect, 1);
                            needUpdate = true;
                        } else {
                            //Adding it permanently
                            if(!worldObj.isRemote && behavior.doesAccept(aspect)) {
                                behavior.addAspect(GrowingNodeBehavior.AspectType.ASPECT_ORB, getAspectsBase(), aspect, 1);
                                needUpdate = true;
                            }
                        }
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
                if(!(entityItem.getEntityItem().getItem() instanceof ItemWispEssence)) continue;
                if ((entityItem.isEntityAlive()) && (!entityItem.isEntityInvulnerable())) {
                    double d = getDistanceTo(entityItem.posX, entityItem.posY, entityItem.posZ);
                    if (d < 1.0D) { //prev: 2.0D
                        ItemWispEssence essence = (ItemWispEssence) entityItem.getEntityItem().getItem();
                        AspectList list = essence.getAspects(entityItem.getEntityItem());
                        if(!(list == null || list.size() == 0)) {
                            Aspect aspect = list.getAspects()[0];
                            if (getAspects().getAmount(aspect) < getNodeVisBase(aspect)) {
                                addToContainer(aspect, 1);
                                needUpdate = true;
                            } else {
                                //Adding it permanently
                                if(!worldObj.isRemote && behavior.doesAccept(aspect)) {
                                    behavior.addAspect(GrowingNodeBehavior.AspectType.WISP_ESSENCE, getAspectsBase(), aspect, 1);
                                    needUpdate = true;
                                }
                            }
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
                    if (d < 1.0D) { //prev: 2.0D
                        Aspect aspect = Aspect.getAspect(wisp.getType());

                        if(aspect != null) {
                            if (getAspects().getAmount(aspect) < getNodeVisBase(aspect)) {
                                addToContainer(aspect, 1);
                                needUpdate = true;
                            } else {
                                //Adding it permanently

                                if(!worldObj.isRemote && behavior.doesAccept(aspect)) {
                                    behavior.addAspect(GrowingNodeBehavior.AspectType.WISP, getAspectsBase(), aspect, 1);
                                    needUpdate = true;
                                }
                            }
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

    @Override
    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);

        NBTTagCompound compound = nbttagcompound.getCompoundTag("Gadomancy");

        this.growing = compound.getBoolean("growing");

        if(growing) {
            NBTTagCompound growingNodeBehavior = compound.getCompoundTag("NodeBehavior");
            behavior.readFromNBT(growingNodeBehavior);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);

        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("growing", growing);

        if(growing) {
            NBTTagCompound behaviorCompound = new NBTTagCompound();
            behavior.writeToNBT(behaviorCompound);
            compound.setTag("NodeBehavior", behaviorCompound);
        }

        nbttagcompound.setTag("Gadomancy", compound);
    }

}
