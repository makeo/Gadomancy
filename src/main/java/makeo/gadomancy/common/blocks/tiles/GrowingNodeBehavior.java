package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 24.10.2015 10:50
 */
public class GrowingNodeBehavior {

    public static final double SATURATION_DIFFICULTY = 10D;
    public static final double SATURATION_CAP = 100D;
    private static final Random rand = new Random();

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

    public void addAspect(AspectType type, AspectList nodeAspects, Aspect aspect, int value) {
        if(isSaturated) return;

        double increasedSaturation = getSaturation(type, aspect);
        addSaturation(aspect, increasedSaturation);
        nodeAspects.add(aspect, value);

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

        //If the saturation is in the upper 85%
        System.out.println(percentSaturation + " - " + overallHappiness);
        if(percentSaturation > satCmp) {
            this.isSaturated = true;
        }
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

    public boolean doesAccept(Aspect aspect) {
        if(isSaturated) {
            handleOverfeed();
            return false;
        }

        if(aspectSaturation.containsKey(aspect)) {
            double percentageToSaturation = aspectSaturation.get(aspect) / SATURATION_CAP; // 0.0 - 1.0
            boolean mayAdd = rand.nextFloat() > percentageToSaturation;
            if(mayAdd) {
                double inc = 1D / (1D - percentageToSaturation);
                if(lastFedAspect != null) {
                    if(aspect.equals(lastFedAspect)) {
                        inc *= evaluateFeedingDenialFunc((lastFedRow > 0 ? lastFedRow : 1));
                    } else {
                        inc *= -10;
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
        return Math.pow(2D, (i / 6D));
    }

    private void handleOverfeed() {
        System.out.println("OVERFED");
        //TODO let node collapse if overfed & still feeding...
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

    public static enum AspectType {

        WISP, WISP_ESSENCE, ASPECT_ORB

    }

}
