package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketTCNodeBolt;
import makeo.gadomancy.common.node.ExtendedNodeType;
import makeo.gadomancy.common.node.GrowingNodeBehavior;
import makeo.gadomancy.common.utils.ExplosionHelper;
import makeo.gadomancy.common.utils.ResearchHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.nodes.INode;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.items.ItemCrystalEssence;
import thaumcraft.common.items.ItemManaBean;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.tiles.TileNode;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 21.10.2015 23:39
 */
public class TileExtendedNode extends TileNode implements INode {

    public int ticksExisted = 0;
    private ExtendedNodeType extendedNodeType;
    private GrowingNodeBehavior behavior;

    private boolean doingVortexExplosion = false;
    private ExplosionHelper.VortexExplosion vortexExplosion;


    public TileExtendedNode() {
        this.behavior = new GrowingNodeBehavior(this);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        ticksExisted++;

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

        needUpdate = handleGrowingNodeFirst(false);
        needUpdate = handleGrowingNodeSecond(needUpdate);

        if(!worldObj.isRemote && needUpdate) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }
    }

    private boolean handleGrowingNodeSecond(boolean needUpdate) {
        if(extendedNodeType == null || extendedNodeType != ExtendedNodeType.GROWING) return needUpdate;

        if(worldObj.difficultySetting == EnumDifficulty.PEACEFUL) return needUpdate;
        if(worldObj.isRemote) return needUpdate;
        if(ticksExisted % 8 != 0) return needUpdate;

        List livingEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(6.0D, 6.0D, 6.0D));
        if ((livingEntities != null) && (livingEntities.size() > 0)) {
            for (Object e : livingEntities) {
                EntityLivingBase livingEntity = (EntityLivingBase) e;
                if ((livingEntity.isEntityAlive()) && (!livingEntity.isEntityInvulnerable())) {
                    if(livingEntity instanceof EntityPlayer && ((EntityPlayer) livingEntity).capabilities.isCreativeMode) continue;
                    if(!behavior.mayZapNow()) continue;

                    ResearchHelper.distributeResearch(Gadomancy.MODID.toUpperCase() + ".GROWING_AGGRESSION", worldObj, xCoord, yCoord, zCoord, 6);

                    livingEntity.attackEntityFrom(DamageSource.magic, behavior.getZapDamage());

                    worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "thaumcraft:zap", 0.8F, 1.0F);

                    PacketTCNodeBolt packet = new PacketTCNodeBolt(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, (float) livingEntity.posX, (float) (livingEntity.posY + livingEntity.height), (float) livingEntity.posZ, 0, false);
                    PacketHandler.INSTANCE.sendToAllAround(packet, new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32.0D));
                }
            }
        }

        return needUpdate;
    }

    private boolean handleGrowingNodeFirst(boolean needUpdate) {
        if(extendedNodeType == null || extendedNodeType != ExtendedNodeType.GROWING) return needUpdate;

        needUpdate = doVortex(EntityAspectOrb.class, needUpdate, new GrowingNodeVortexRunnable<EntityAspectOrb>() {
            @Override
            public Aspect getAspect(EntityAspectOrb entity) {
                return entity.getAspect();
            }

            @Override
            public GrowingNodeBehavior.AspectType getAspectType(EntityAspectOrb entity) {
                return GrowingNodeBehavior.AspectType.ASPECT_ORB;
            }
        });
        needUpdate = doVortex(EntityItem.class, needUpdate, new GrowingNodeVortexRunnable<EntityItem>() {
            @Override
            public boolean doesVortex(EntityItem entity) {
                return entity.getEntityItem().getItem() instanceof ItemWispEssence || entity.getEntityItem().getItem() instanceof ItemManaBean || entity.getEntityItem().getItem() instanceof ItemCrystalEssence;
            }

            @Override
            public Aspect getAspect(EntityItem entity) {
                IEssentiaContainerItem container = (IEssentiaContainerItem) entity.getEntityItem().getItem();
                return container.getAspects(entity.getEntityItem()).getAspects().length > 0 ? container.getAspects(entity.getEntityItem()).getAspects()[0] : null;
            }

            @Override
            public GrowingNodeBehavior.AspectType getAspectType(EntityItem entity) {
                return entity.getEntityItem().getItem() instanceof ItemWispEssence ? GrowingNodeBehavior.AspectType.WISP_ESSENCE : entity.getEntityItem().getItem() instanceof ItemCrystalEssence ? GrowingNodeBehavior.AspectType.CRYSTAL_ESSENCE : GrowingNodeBehavior.AspectType.MANA_BEAN;
            }
        });
        needUpdate = doVortex(EntityWisp.class, needUpdate, new GrowingNodeVortexRunnable<EntityWisp>() {
            @Override
            public Aspect getAspect(EntityWisp entity) {
                return Aspect.getAspect(entity.getType());
            }

            @Override
            public GrowingNodeBehavior.AspectType getAspectType(EntityWisp entity) {
                return GrowingNodeBehavior.AspectType.WISP;
            }
        });
        if(worldObj.isRemote) return needUpdate;

        if(behavior.lookingForNode()) {
            int xx = worldObj.rand.nextInt(8) - worldObj.rand.nextInt(8);
            int yy = worldObj.rand.nextInt(8) - worldObj.rand.nextInt(8);
            int zz = worldObj.rand.nextInt(8) - worldObj.rand.nextInt(8);
            TileEntity t = worldObj.getTileEntity(xCoord + xx, yCoord + yy, zCoord + zz);
            if(t != null && t instanceof TileNode && !(xx == 0 && yy == 0 && zz == 0)) {
                TileNode node = (TileNode) t;
                int thisSize = getAspectsBase().visSize();
                int othersSize = node.getAspectsBase().visSize();
                if(thisSize >= othersSize) {
                    behavior.lockOnTo(node);
                    worldObj.markBlockForUpdate(xCoord + xx, yCoord + yy, zCoord + zz);
                    node.markDirty();
                    needUpdate = true;
                }
            }
        } else {
            needUpdate = behavior.updateBehavior(needUpdate);
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

    private <E extends Entity> boolean doVortex(Class<E> entityClass, boolean needUpdate, GrowingNodeVortexRunnable<E> runnable) {
        List entities = this.worldObj.getEntitiesWithinAABB(entityClass, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(15.0D, 15.0D, 15.0D));
        if ((entities != null) && (entities.size() > 0)) {
            for (Object eObj : entities) {
                E entity = (E) eObj;
                if(!runnable.doesVortex(entity)) continue;
                if ((entity.isEntityAlive()) && (!entity.isEntityInvulnerable())) {
                    double d = getDistanceTo(entity.posX, entity.posY, entity.posZ);
                    if (d < 0.5D) { //prev: 2.0D
                        Aspect a = runnable.getAspect(entity);
                        if(a != null) {
                            if (getAspects().getAmount(a) < getNodeVisBase(a)) {
                                addToContainer(a, 1);
                            } else {
                                //Adding it permanently
                                if(!worldObj.isRemote && behavior.doesAccept(a)) {
                                    behavior.addAspect(runnable.getAspectType(entity), a, 1);
                                }
                            }
                        }
                        needUpdate = true;
                        entity.setDead();
                    }
                }
                applyMovementVectors(entity);
            }
        }
        return needUpdate;
    }

    public ExtendedNodeType getExtendedNodeType() {
        return extendedNodeType;
    }

    public void setExtendedNodeType(ExtendedNodeType extendedNodeType) {
        this.extendedNodeType = extendedNodeType;
    }

    public NBTTagCompound getBehaviorSnapshot() {
        if(extendedNodeType != null && extendedNodeType == ExtendedNodeType.GROWING) {
            NBTTagCompound behaviorCompound = new NBTTagCompound();
            behavior.writeToNBT(behaviorCompound);
            return behaviorCompound;
        }
        return null;
    }

    public void readBehaviorSnapshot(NBTTagCompound tagCompound) {
        if(tagCompound != null && extendedNodeType != null && extendedNodeType == ExtendedNodeType.GROWING) {
            behavior.readFromNBT(tagCompound);
        }
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

    private abstract static class GrowingNodeVortexRunnable<E> {

        public boolean doesVortex(E entity) {
            return true;
        }

        public abstract Aspect getAspect(E entity);

        public abstract GrowingNodeBehavior.AspectType getAspectType(E entity);

    }

}
