package makeo.gadomancy.common.node;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketAnimationAbsorb;
import makeo.gadomancy.common.network.packets.PacketTCNodeBolt;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.ResearchHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.tiles.TileNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 24.10.2015 10:50
 */
public class GrowingNodeBehavior {

    public static final double SATURATION_DIFFICULTY = 15D;
    public static final double SATURATION_CAP = 110D;
    public static final int HAPPINESS_CAP = 400;

    //The node the growing node attacks.
    private TileNode fixedNode;

    //The node the behavior belongs to
    private final TileExtendedNode owningNode;

    //The lower the 'happier'
    private double overallHappiness = 0.0D;

    //Maps saturation to aspects. higher saturation may stop the node from accepting an aspect.
    //Primal aspects increase saturation even more for those aspects than compound ones.
    //At a defined point the node will stop accepting new aspects overall and chances are it will collapse into
    //a even more deadly hungry node..
    private Map<Aspect, Double> aspectSaturation = new LinkedHashMap<Aspect, Double>();

    //Ensures that the node gets fed up faster if you feed it the same aspect over and over again.
    private Aspect lastFedAspect = null;
    private int lastFedRow = 0;

    //If this is true, the node stops growing.
    private boolean isSaturated = false;

    public GrowingNodeBehavior(TileExtendedNode owningNode) {
        this.owningNode = owningNode;
    }

    public void addAspect(AspectType type, Aspect aspect, int value) {
        if(isSaturated) return;

        double increasedSaturation = getSaturation(type, aspect);
        addSaturation(aspect, increasedSaturation);
        owningNode.getAspectsBase().add(aspect, value);

        ResearchHelper.distributeResearch(Gadomancy.MODID.toUpperCase() + ".GROWING_GROWTH", owningNode.getWorldObj(), owningNode.xCoord, owningNode.yCoord, owningNode.zCoord, 6);

        computeOverallSaturation();
    }

    //Computes the saturation of all current aspects and calculates the overall happiness as well as if the
    //Node will stop growing.
    private void computeOverallSaturation() {
        if(isSaturated) return;

        double completeSaturation = 0D;
        int aspects = aspectSaturation.keySet().size();
        for(Aspect a : aspectSaturation.keySet()) {
            completeSaturation += aspectSaturation.get(a);
        }

        //Not just 0.0-1.0!
        //Values vary from 0.0 to SATURATION_CAP (or a bit higher)
        double percentSaturation = completeSaturation / ((double) aspects);

        double satCmp = SATURATION_CAP;

        satCmp *= 8.5;
        satCmp /=  10;

        if(overallHappiness > HAPPINESS_CAP) {
            overallHappiness /= 10;
            owningNode.triggerVortexExplosion();
        }

        //If the saturation is in the upper 85%
        if(percentSaturation > satCmp) {
            this.isSaturated = true;
        }
    }

    public boolean mayZapNow() {
        return overallHappiness > owningNode.getWorldObj().rand.nextInt(HAPPINESS_CAP * HAPPINESS_CAP);
    }

    public float getZapDamage() {
        float happinessPerc = ((float) overallHappiness) / ((float) HAPPINESS_CAP);
        return happinessPerc > 0.3 ? happinessPerc > 0.6 ? happinessPerc > 0.9 ? 12 : 8 : 5 : 2;
    }

    private void addSaturation(Aspect aspect, double increasedSaturation) {
        double overallReduction = 1D / SATURATION_DIFFICULTY;
        if(aspectSaturation.containsKey(aspect)) {
            aspectSaturation.put(aspect, aspectSaturation.get(aspect) + increasedSaturation);
        } else {
            aspectSaturation.put(aspect, increasedSaturation);
        }
        for(Aspect a : aspectSaturation.keySet()) {
            if(a.equals(aspect)) continue;
            double newSaturation = aspectSaturation.get(a) - overallReduction;
            newSaturation = newSaturation < 0 ? 0 : newSaturation;
            aspectSaturation.put(a, newSaturation);
        }
    }

    private double getSaturation(AspectType type, Aspect aspect) {
        double mult = 1D;
        switch (type) {
            case WISP:
                mult -= 0.2D; //Growing node doesn't get saturated by wisps that fast.
                break;
            case WISP_ESSENCE:
                mult += 0.2D; //Growing node doesn't like essences all the time..
                break;
            case ASPECT_ORB:
                mult -= 0.3D; //Growing node prefers aspects in their most natural form.
                break;
            case MANA_BEAN:
            case CRYSTAL_ESSENCE:
                mult += 0.4D; //Mana beans are hard to breed but easy to multiply.. thus growing node doesn't like. Same goes for crystallized essentia
        }
        if(lastFedAspect != null) {
            if(lastFedAspect.equals(aspect)) {
                mult += 0.4D;
                lastFedRow++;
            }
        }
        lastFedAspect = aspect;
        double sat = aspect.isPrimal() ? 1.4D : 1D;
        return sat * mult;
    }

    public boolean updateBehavior(boolean needUpdate) {
        if(fixedNode != null && owningNode.ticksExisted % 3 == 0) {
            if(owningNode.getWorldObj().getBlock(fixedNode.xCoord, fixedNode.yCoord, fixedNode.zCoord) != RegisteredBlocks.blockNode ||
                    owningNode.getWorldObj().getTileEntity(fixedNode.xCoord, fixedNode.yCoord, fixedNode.zCoord) == null ||
                    fixedNode.isInvalid()) {
                fixedNode = null;
                return needUpdate;
            }
            AspectList currentAspects = fixedNode.getAspects();
            AspectList baseAspects = fixedNode.getAspectsBase();
            if(baseAspects.getAspects().length == 0) {
                int x = fixedNode.xCoord;
                int y = fixedNode.yCoord;
                int z = fixedNode.zCoord;
                removeFixedNode(x, y, z);
                return needUpdate;
            }
            Aspect a = baseAspects.getAspects()[owningNode.getWorldObj().rand.nextInt(baseAspects.getAspects().length)];
            if(baseAspects.getAmount(a) > 0) {
                if(baseAspects.reduce(a, 1)) {
                    World world = owningNode.getWorldObj();
                    int fx = fixedNode.xCoord;
                    int fy = fixedNode.yCoord;
                    int fz = fixedNode.zCoord;
                    int ox = owningNode.xCoord;
                    int oy = owningNode.yCoord;
                    int oz = owningNode.zCoord;
                    currentAspects.reduce(a, 1);

                    ResearchHelper.distributeResearch(Gadomancy.MODID.toUpperCase() + ".GROWING_ATTACK", owningNode.getWorldObj(), owningNode.xCoord, owningNode.yCoord, owningNode.zCoord, 16);

                    EntityAspectOrb aspectOrb = new EntityAspectOrb(world, fx + 0.5D, fy + 0.5D, fz + 0.5D, a, 1);
                    Vec3 dir = Vec3.createVectorHelper(fx + 0.5D, fy + 0.5D, fz + 0.5D).subtract(Vec3.createVectorHelper(ox + 0.5D, oy + 0.5D, oz + 0.5D)).normalize();
                    dir.addVector(randOffset(), randOffset(), randOffset()).normalize();
                    aspectOrb.motionX = dir.xCoord;
                    aspectOrb.motionY = dir.yCoord;
                    aspectOrb.motionZ = dir.zCoord;
                    fixedNode.getWorldObj().spawnEntityInWorld(aspectOrb);

                    NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(world.provider.dimensionId, ox + 0.5F, oy + 0.5F, oz + 0.5F, 32);
                    PacketTCNodeBolt bolt = new PacketTCNodeBolt(ox + 0.5F, oy + 0.5F, oz + 0.5F, fx + 0.5F, fy + 0.5F, fz + 0.5F, 0, false);
                    PacketHandler.INSTANCE.sendToAllAround(bolt, point);

                    PacketAnimationAbsorb packet = new PacketAnimationAbsorb(ox, oy, oz, fx, fy, fz, 7);
                    PacketHandler.INSTANCE.sendToAllAround(packet, point);

                    world.markBlockForUpdate(fx, fy, fz);
                    fixedNode.markDirty();
                    needUpdate = true;
                } else {
                    if(baseAspects.size() <= 1) {
                        int x = fixedNode.xCoord;
                        int y = fixedNode.yCoord;
                        int z = fixedNode.zCoord;
                        removeFixedNode(x, y, z);
                        needUpdate = true;
                    }
                    baseAspects.remove(a);
                    currentAspects.remove(a);
                    return needUpdate;
                }
            } else {
                if(baseAspects.size() <= 1) {
                    int x = fixedNode.xCoord;
                    int y = fixedNode.yCoord;
                    int z = fixedNode.zCoord;
                    removeFixedNode(x, y, z);
                    needUpdate = true;
                }
                baseAspects.remove(a);
                currentAspects.remove(a);
                return needUpdate;
            }
        }
        return needUpdate;
    }

    private void removeFixedNode(int x, int y, int z) {
        owningNode.getWorldObj().setBlockToAir(x, y, z);
        owningNode.getWorldObj().removeTileEntity(x, y, z);
        owningNode.getWorldObj().markBlockForUpdate(x, y, z);
        this.fixedNode = null;
    }

    private float randOffset() {
        return owningNode.getWorldObj().rand.nextFloat() / 3;
    }

    public boolean doesAccept(Aspect aspect) {
        if(isSaturated) {
            handleOverfeed();
            return false;
        }

        if(aspectSaturation.containsKey(aspect)) {
            double percentageToSaturation = aspectSaturation.get(aspect) / SATURATION_CAP; // 0.0 - 1.0
            boolean mayAdd = owningNode.getWorldObj().rand.nextFloat() > percentageToSaturation;
            if(mayAdd) {
                double inc = 1D / (1D - percentageToSaturation);
                if(lastFedAspect != null) {
                    if(aspect.equals(lastFedAspect)) {
                        inc *= evaluateFeedingDenialFunc((lastFedRow > 0 ? lastFedRow : 1));
                    } else {
                        inc *= -10;
                        lastFedRow = 0;
                    }
                }
                this.overallHappiness += inc; //The node doesn't like to be forced to eat the same every time.
                if(this.overallHappiness < 0) this.overallHappiness = 0;
            }
            return mayAdd;
        } else {
            //The node does not know that aspect yet/hasn't created an immunity to it yet.
            //The node gets to know a new aspect, thus the happiness increases.
            this.overallHappiness = overallHappiness / SATURATION_DIFFICULTY;
            return true;
        }
    }

    private double evaluateFeedingDenialFunc(double i) {
        return (Math.log(i) / 6) + 1;
    }

    private void handleOverfeed() {
        //TODO handle transformation into STARVING
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        this.isSaturated = nbtTagCompound.getBoolean("overallSaturated");
        this.overallHappiness = nbtTagCompound.getDouble("overallHappiness");

        if(nbtTagCompound.hasKey("lastFed")) {
            this.lastFedAspect = Aspect.getAspect(nbtTagCompound.getString("lastFedAspect"));
            this.lastFedRow = nbtTagCompound.getInteger("lastFedRow");
        }

        NBTTagList list = nbtTagCompound.getTagList("saturationValues", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound aspectCompound = list.getCompoundTagAt(i);
            String name = aspectCompound.getString("aspectName");
            double saturation = aspectCompound.getDouble("aspectSaturation");
            Aspect a = Aspect.getAspect(name);
            if(a != null) {
                aspectSaturation.put(a, saturation);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setBoolean("overallSaturated", isSaturated);
        nbtTagCompound.setDouble("overallHappiness", overallHappiness);
        if(lastFedAspect != null) {
            nbtTagCompound.setString("lastFedAspect", lastFedAspect.getTag());
            nbtTagCompound.setInteger("lastFedRow", lastFedRow);
        }

        NBTTagList list = new NBTTagList();
        for(Aspect a : aspectSaturation.keySet()) {
            double saturation = aspectSaturation.get(a);
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("aspectName", a.getTag());
            compound.setDouble("aspectSaturation", saturation);
            list.appendTag(compound);
        }
        nbtTagCompound.setTag("saturationValues", list);
    }

    public boolean lookingForNode() {
        return fixedNode == null;
    }

    public void lockOnTo(TileNode node) {
        if(this.fixedNode == null)
            this.fixedNode = node;
    }

    public static enum AspectType {

        WISP, WISP_ESSENCE, ASPECT_ORB, MANA_BEAN, CRYSTAL_ESSENCE

    }

}
