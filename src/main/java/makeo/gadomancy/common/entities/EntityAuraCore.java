package makeo.gadomancy.common.entities;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.EntityFXFlowPolicy;
import makeo.gadomancy.client.effect.fx.FXFlow;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.data.ModConfig;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.PrimalAspectList;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 16.11.2015 14:42
 */
public class EntityAuraCore extends EntityItem {

    private static final String SPLIT = ";";

    private static final int PRE_GATHER_EFFECT_LENGTH = 50;
    private static final int GATHER_EFFECT_LENGTH = 600; //20 + 430
    private static final int GATHER_RANGE = 8;
    private static final int CLUSTER_WEIGHT = 10; //Counts as 10 'blocks' 'scanned' with the given aspect.
    public static final int CLUSTER_RANGE = 10; //Defines how close the clicked cluster has to be.

    private int ticksExisted = 0;
    public PrimalAspectList internalAuraList = new PrimalAspectList();
    public Orbital auraOrbital = null;

    //Effect stuff ResidentSleeper
    private Aspect[] effectAspects = new Aspect[6];
    private Orbital.OrbitalRenderProperties[] effectProperties = new Orbital.OrbitalRenderProperties[6];
    private FXFlow[] flows = new FXFlow[6];

    private String oldAspectDataSent = null;
    public ChunkCoordinates activationLocation = null;

    public EntityAuraCore(World world) {
        super(world);
    }

    public EntityAuraCore(World world, double x, double y, double z, ItemStack stack, ChunkCoordinates startingCoords, Aspect[] aspects) {
        super(world, x, y, z, stack);
        this.activationLocation = startingCoords;
        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherCoordId, startingCoords);
        if(aspects.length == 1) {
            this.internalAuraList.add(aspects[0], CLUSTER_WEIGHT * 6);
        } else {
            for(Aspect a : aspects) {
                if(a == null) continue;
                this.internalAuraList.add(a, CLUSTER_WEIGHT);
            }
        }
        updateAndSendAspectData();
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        getDataWatcher().addObjectByDataType(ModConfig.entityAuraCoreDatawatcherTickId, 2);
        getDataWatcher().addObjectByDataType(ModConfig.entityAuraCoreDatawatcherAspectsId, 4);
        getDataWatcher().addObjectByDataType(ModConfig.entityAuraCoreDatawatcherCoordId, 6);

        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherAspectsId, "");
        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherCoordId, new ChunkCoordinates(0, -1, 0));
        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherTickId, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(auraOrbital == null && !MiscUtils.getPositionVector(this).equals(Vector3.ZERO)
                && worldObj.isRemote) {
            auraOrbital = new Orbital(MiscUtils.getPositionVector(this), worldObj);
        }
        if(auraOrbital != null && worldObj.isRemote) {
            if(!auraOrbital.registered) {
                EffectHandler.getInstance().registerOrbital(auraOrbital);
            }
            auraOrbital.updateCenter(new Vector3(posX, posY, posZ));
            if(ticksExisted > PRE_GATHER_EFFECT_LENGTH) {
                int part = ticksExisted - PRE_GATHER_EFFECT_LENGTH;
                float perc = ((float) GATHER_EFFECT_LENGTH - part) / ((float) GATHER_EFFECT_LENGTH);
                auraOrbital.reduceAllOffsets(perc);
            }
        }

        ticksExisted++;

        if (this.age + 5 >= this.lifespan) {
            this.age = 0;
        }

        if(!worldObj.isRemote) {
            getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherTickId, ticksExisted);
            if(ticksExisted > GATHER_EFFECT_LENGTH) {
                finishCore();
            } else if(ticksExisted > PRE_GATHER_EFFECT_LENGTH) {
                if(auraGatherCycle())
                    updateAndSendAspectData();
            }
        } else {
            ticksExisted = getDataWatcher().getWatchableObjectInt(ModConfig.entityAuraCoreDatawatcherTickId);
            boolean changed = recieveAspectData();
            for (int i = 0; i < effectProperties.length; i++) {
                Orbital.OrbitalRenderProperties node = effectProperties[i];
                if(node == null) {
                    if(effectAspects[i] == null) {
                        continue;
                    }

                    node = new Orbital.OrbitalRenderProperties(Orbital.Axis.persisentRandomAxis(), rand.nextDouble() + 2D);
                    node.setColor(new Color(effectAspects[i].getColor())).setTicksForFullCircle(60 + rand.nextInt(40));
                    node.setOffsetTicks(rand.nextInt(80));
                    Color c = getSubParticleColor(effectAspects[i]);
                    node.setSubParticleColor(c);
                    effectProperties[i] = node;
                }

                if(flows[i] == null && ticksExisted < PRE_GATHER_EFFECT_LENGTH) {
                    ChunkCoordinates origin = (ChunkCoordinates) getDataWatcher().getWatchedObject(ModConfig.entityAuraCoreDatawatcherCoordId).getObject();
                    Vector3 v = new Vector3(origin.posX + 0.5D, origin.posY + 0.5D, origin.posZ + 0.5D);
                    flows[i] = EffectHandler.getInstance().effectFlow(worldObj,
                            v, new FXFlow.EntityFlowProperties().setPolicy(EntityFXFlowPolicy.Policies.DEFAULT)
                                    .setTarget(auraOrbital.getOrbitalStartPoints(node)[0])
                                    .setColor(new Color(effectAspects[i].getColor())).setFading(getSubParticleColor(effectAspects[i])));
                    flows[i].setLivingTicks(PRE_GATHER_EFFECT_LENGTH - ticksExisted);
                }

                if(flows[i] != null) flows[i].applyTarget(auraOrbital.getOrbitalStartPoints(node)[0]);

                if(changed) {
                    if(effectProperties[i] != null) {
                        effectProperties[i].setColor(new Color(effectAspects[i].getColor()));
                        Color c = getSubParticleColor(effectAspects[i]);
                        node.setSubParticleColor(c);
                    }

                    if(flows[i] != null) {
                        flows[i].setColor(getSubParticleColor(effectAspects[i]));
                        flows[i].setColor(new Color(effectAspects[i].getColor()));
                    }
                }
            }
            if(ticksExisted >= (PRE_GATHER_EFFECT_LENGTH - 1)) {
                if(auraOrbital.orbitalsSize() == 0) {
                    for (int i = 0; i < 6; i++) {
                        Orbital.OrbitalRenderProperties node = effectProperties[i];
                        auraOrbital.addOrbitalPoint(node);
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    if(flows[i] != null) {
                        flows[i].lastUpdateCall = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    private void finishCore() {
        double avg = ((double) this.internalAuraList.visSize()) / ((double) this.internalAuraList.size());
        Aspect[] sortedHtL = this.internalAuraList.getAspectsSortedAmount();
        AspectList al = new AspectList();
        for(Aspect a : sortedHtL) {
            if(a == null) return;
            int am = this.internalAuraList.getAmount(a);
            if(am >= avg) {
                al.add(a, am);
            }
        }

        List<AspectWRItem> rand = new ArrayList<AspectWRItem>();
        for(Aspect a : al.getAspects()) {
            if(a == null) continue;
            rand.add(new AspectWRItem(al.getAmount(a), a));
        }

        Aspect aura = ((AspectWRItem) WeightedRandom.getRandomItem(worldObj.rand, rand)).getAspect();

        ItemStack auraCore = new ItemStack(RegisteredItems.itemAuraCore, 1, 0);

        if(aura.equals(Aspect.AIR)) {
            RegisteredItems.itemAuraCore.setCoreType(auraCore, ItemAuraCore.AuraCoreType.AIR);
        } else if(aura.equals(Aspect.WATER)) {
            RegisteredItems.itemAuraCore.setCoreType(auraCore, ItemAuraCore.AuraCoreType.WATER);
        } else if(aura.equals(Aspect.EARTH)) {
            RegisteredItems.itemAuraCore.setCoreType(auraCore, ItemAuraCore.AuraCoreType.EARTH);
        } else if(aura.equals(Aspect.FIRE)) {
            RegisteredItems.itemAuraCore.setCoreType(auraCore, ItemAuraCore.AuraCoreType.FIRE);
        } else if(aura.equals(Aspect.ORDER)) {
            RegisteredItems.itemAuraCore.setCoreType(auraCore, ItemAuraCore.AuraCoreType.ORDER);
        } else if(aura.equals(Aspect.ENTROPY)) {
            RegisteredItems.itemAuraCore.setCoreType(auraCore, ItemAuraCore.AuraCoreType.ENTROPY);
        }

        //TODO finishing effects...?

        EntityItem ei = new EntityItem(worldObj, posX, posY, posZ, auraCore);
        ei.motionX = 0;
        ei.motionY = 0;
        ei.motionZ = 0;
        worldObj.spawnEntityInWorld(ei);
        setDead();
    }

    //True, if something was gathered, false if not.
    private boolean auraGatherCycle() {
        //TODO gather surrounding aura
        return false;
    }

    private Color getSubParticleColor(Aspect a) {
        Color c = null;
        if(a.equals(Aspect.AIR)) {
            c = new Color(0xFEFFC9);
        } else if(a.equals(Aspect.FIRE)) {
            c = new Color(0xFFAA3C);
        } else if(a.equals(Aspect.EARTH)) {
            c = new Color(0x7EE35F);
        } else if(a.equals(Aspect.WATER)) {
            c = new Color(0x78CFCC);
        } else if(a.equals(Aspect.ORDER)) {
            c = new Color(0xFFFFFF);
        } else if(a.equals(Aspect.ENTROPY)) {
            c = new Color(0x000000);
        }
        return c;
    }

    //Client side only.
    private boolean recieveAspectData() {
        String rec = getDataWatcher().getWatchableObjectString(ModConfig.entityAuraCoreDatawatcherAspectsId);
        if(oldAspectDataSent == null || !this.oldAspectDataSent.equals(rec)) {
            oldAspectDataSent = rec;
        } else {
            return false;
        }

        if(rec.equals("")) return false;

        String[] arr = rec.split(SPLIT);
        if(arr.length != 6) throw new IllegalStateException("Server sent wrong Aura Data! '"
                + rec + "' Please report this error to the mod authors!");
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            Aspect a = Aspect.getAspect(s);
            effectAspects[i] = a;
        }
        return true;
    }

    private void updateAndSendAspectData() {
        double avg = ((double) this.internalAuraList.visSize()) / ((double) this.internalAuraList.size());
        Aspect[] sortedHtL = this.internalAuraList.getAspectsSortedAmount();
        AspectList al = new AspectList();
        for(Aspect a : sortedHtL) {
            if(a == null) return;
            int am = this.internalAuraList.getAmount(a);
            if(am >= avg) {
                al.add(a, am);
            }
        }

        double perc = ((double) al.visSize()) / 6D;
        Aspect[] arr = new Aspect[6];
        sortedHtL = al.getAspectsSortedAmount();
        //TODO find sth. better...?
        //TODO find sth. that doesn't crash xD
        int pt = 0;
        for(Aspect a : sortedHtL) {
            while(al.getAmount(a) - perc >= 0) {
                if(pt == 6) break;
                arr[pt] = a;
                pt++;
                if(!al.reduce(a, (int) perc)) {
                    al.remove(a);
                }
            }
            if(pt == 6) break;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if(sb.length() > 0) {
                sb.append(SPLIT);
            }
            sb.append(arr[i].getTag());
        }
        String toSend = sb.toString();

        if(this.oldAspectDataSent == null || !this.oldAspectDataSent.equals(toSend)) {
            this.oldAspectDataSent = toSend;
            getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherAspectsId, toSend);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        this.internalAuraList = new PrimalAspectList();

        ticksExisted = compound.getInteger("ticksExisted");
        NBTTagList list = compound.getTagList("auraList", compound.getId());
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound cmp = list.getCompoundTagAt(i);
            if(cmp.hasKey("tag") && cmp.hasKey("amt")) {
                internalAuraList.add(Aspect.getAspect(cmp.getString("tag")), cmp.getInteger("amt"));
            }
        }
        if(compound.hasKey("activationVecX") && compound.hasKey("activationVecY") && compound.hasKey("activationVecZ")) {
            int x = compound.getInteger("activationVecX");
            int y = compound.getInteger("activationVecY");
            int z = compound.getInteger("activationVecZ");
            activationLocation = new ChunkCoordinates(x, y, z);
        }

        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherCoordId,
                this.activationLocation == null ? new ChunkCoordinates(0, -1, 0) : this.activationLocation);
        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherTickId, ticksExisted);
        updateAndSendAspectData();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setInteger("ticksExisted", ticksExisted);
        NBTTagList list = new NBTTagList();
        for(Aspect a : internalAuraList.getAspects()) {
            if(a == null) continue;
            NBTTagCompound aspectCompound = new NBTTagCompound();
            aspectCompound.setString("tag", a.getTag());
            aspectCompound.setInteger("amt", internalAuraList.getAmount(a));
            list.appendTag(aspectCompound);
        }
        compound.setTag("auraList", list);
        if(activationLocation != null) {
            compound.setInteger("activationVecX", activationLocation.posX);
            compound.setInteger("activationVecY", activationLocation.posY);
            compound.setInteger("activationVecZ", activationLocation.posZ);
        }
    }

    public static final class AspectWRItem extends WeightedRandom.Item {

        private final Aspect aspect;

        public AspectWRItem(int weight, Aspect aspect) {
            super(weight);
            this.aspect = aspect;
        }

        public Aspect getAspect() {
            return aspect;
        }
    }

}
