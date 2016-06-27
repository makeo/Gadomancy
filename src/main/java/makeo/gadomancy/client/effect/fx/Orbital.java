package makeo.gadomancy.client.effect.fx;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 17.11.2015 18:41
 */
public final class Orbital {

    private Vector3 center;
    private final World world;
    private int orbitalCounter = 0;
    public boolean registered = false;
    //INFO: Needs to be updated when its "owner" gets rendered.
    public long lastRenderCall = System.currentTimeMillis();

    private List<OrbitalRenderProperties> orbitals = new ArrayList<OrbitalRenderProperties>();

    public Orbital(Vector3 center, World world) {
        this.center = center;
        this.world = world;
    }

    public void updateCenter(Vector3 center) {
        this.center = center;
    }

    public void addOrbitalPoint(OrbitalRenderProperties properties) {
        if(!orbitals.contains(properties)) {
            orbitals.add(properties);
        }
    }

    public int orbitalsSize() {
        return orbitals.size();
    }

    public void clearOrbitals() {
        orbitals.clear();
    }

    public void doRender(float partialTicks) {
        if(MiscUtils.getPositionVector(Minecraft.getMinecraft().renderViewEntity).distance(center) > ModConfig.renderParticleDistance) return;
        if(Minecraft.getMinecraft().isGamePaused()) return;

        for(OrbitalRenderProperties orbitalNode : orbitals) {
            Axis axis = orbitalNode.getAxis();
            int counterOffset = orbitalNode.getOffsetTicks() % orbitalNode.getTicksForFullCircle();

            int currentDividedPolicyTick = (orbitalCounter + counterOffset) % orbitalNode.getTicksForFullCircle();
            float currentDegree = 360F * (((float) currentDividedPolicyTick) / ((float) orbitalNode.getTicksForFullCircle()));
            double currentRad = Math.toRadians(currentDegree);

            Vector3 point = axis.getAxis().clone().perpendicular().normalize().multiply(orbitalNode.getOffset()).rotate(currentRad, axis.getAxis()).add(center);

            if(orbitalNode.getRunnable() != null) {
                orbitalNode.getRunnable().onRender(world, point, orbitalNode, orbitalCounter, partialTicks);
            }

            if(orbitalNode.getParticleSize() <= 0) continue;

            FXFlow.FXFlowBase flow = new FXFlow.FXFlowBase(world, point.getX(), point.getY(), point.getZ(),
                    orbitalNode.getColor(), orbitalNode.getParticleSize(), orbitalNode.getMultiplier(), orbitalNode.getBrightness());

            if(orbitalNode.getSubParticleColor() != null && world.rand.nextInt(3) == 0) {
                Vector3 subOffset = genSubOffset(world.rand, 0.8F);
                Color c = (world.rand.nextBoolean()) ? orbitalNode.getSubParticleColor() : orbitalNode.getColor();
                FXFlow.FXFlowBase flow2 = new FXFlow.FXFlowBase(world,
                        point.getX() + subOffset.getX(), point.getY() + subOffset.getY(), point.getZ() + subOffset.getZ(),
                        c, orbitalNode.getSubSizeRunnable().getSubParticleSize(world.rand, orbitalCounter), 6, 240);

                Minecraft.getMinecraft().effectRenderer.addEffect(flow2);
            }

            Minecraft.getMinecraft().effectRenderer.addEffect(flow);
        }
    }

    private Vector3 genSubOffset(Random rand, float surroundingDistance) {
        float x = ((rand.nextFloat() / 4F) * surroundingDistance) * (rand.nextBoolean() ? 1 : -1);
        float y = ((rand.nextFloat() / 4F) * surroundingDistance) * (rand.nextBoolean() ? 1 : -1);
        float z = ((rand.nextFloat() / 4F) * surroundingDistance) * (rand.nextBoolean() ? 1 : -1);
        return new Vector3(x, y, z);
    }

    public void reduceAllOffsets(float percent) {
        for(OrbitalRenderProperties node : orbitals) {
            node.reduceOffset(percent);
        }
    }

    public Vector3[] getOrbitalStartPoints(OrbitalRenderProperties... properties) {
        Vector3[] arr = new Vector3[properties.length];
        for (int i = 0; i < properties.length; i++) {
            OrbitalRenderProperties property = properties[i];
            if(property == null) {
                arr[i] = null;
                continue;
            }

            Axis axis = property.getAxis();
            int counterOffset = property.getOffsetTicks() % property.getTicksForFullCircle();

            int currentDividedPolicyTick = (orbitalCounter + counterOffset) % property.getTicksForFullCircle();
            float currentDegree = 360F * (((float) currentDividedPolicyTick) / ((float) property.getTicksForFullCircle()));
            double currentRad = Math.toRadians(currentDegree);

            arr[i] = axis.getAxis().clone().perpendicular().normalize().multiply(property.getOffset()).rotate(currentRad, axis.getAxis()).add(center);
        }
        return arr;
    }

    public static void sheduleRenders(List<Orbital> orbitals, float partialTicks) {
        for(Orbital orbital : orbitals) {
            orbital.doRender(partialTicks);
        }
    }

    public static void tickOrbitals(List<Orbital> orbitals) {
        for(Orbital orbital : orbitals) {
            if((System.currentTimeMillis() - orbital.lastRenderCall) > 1000L) {
                orbital.clearOrbitals();
                EffectHandler.getInstance().unregisterOrbital(orbital);
            } else {
                orbital.orbitalCounter++;
            }
        }
    }

    public static class OrbitalRenderProperties {

        private static final OrbitalSubSizeRunnable subSizeRunnableStatic = new OrbitalSubSizeRunnable() {
            @Override
            public float getSubParticleSize(Random rand, int orbitalExisted) {
                return 0.1F + (rand.nextBoolean() ? 0.0F : 0.1F);
            }
        };

        private Axis axis;
        private double originalOffset, offset;
        private Color color = Color.WHITE;
        private int ticksForFullCircle = 40;
        private OrbitalRenderRunnable runnable = null;
        private int multiplier = 8;
        private int brightness = 240;
        private float particleSize = 0.2F;
        private int offsetTicks = 0;

        private Color subParticleColor = null;
        private OrbitalSubSizeRunnable subSizeRunnable;

        public OrbitalRenderProperties(Axis axis, double offsetLength) {
            this.offset = this.originalOffset = offsetLength;
            this.axis = axis;
            this.subSizeRunnable = subSizeRunnableStatic;
        }

        public OrbitalRenderProperties setColor(Color color) {
            this.color = color;
            return this;
        }

        public OrbitalRenderProperties setSubSizeRunnable(OrbitalSubSizeRunnable subSizeRunnable) {
            if(subSizeRunnable == null) return this;
            this.subSizeRunnable = subSizeRunnable;
            return this;
        }

        public OrbitalRenderProperties setTicksForFullCircle(int ticks) {
            this.ticksForFullCircle = ticks;
            return this;
        }

        public OrbitalRenderProperties setRenderRunnable(OrbitalRenderRunnable runnable) {
            this.runnable = runnable;
            return this;
        }

        public OrbitalRenderProperties setBrightness(int brightness) {
            this.brightness = brightness;
            return this;
        }

        public OrbitalRenderProperties setMultiplier(int multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public OrbitalRenderProperties setOffsetTicks(int offsetTicks) {
            this.offsetTicks = offsetTicks;
            return this;
        }

        public OrbitalRenderProperties setParticleSize(float particleSize) {
            this.particleSize = particleSize;
            return this;
        }

        public OrbitalRenderProperties setSubParticleColor(Color subParticleColor) {
            this.subParticleColor = subParticleColor;
            return this;
        }

        //Percent from 0.0F to 1.0F
        public void reduceOffset(float percent) {
            this.offset = this.originalOffset * percent;
        }

        public float getParticleSize() {
            return particleSize;
        }

        public Color getColor() {
            return color;
        }

        public Axis getAxis() {
            return axis;
        }

        public double getOffset() {
            return offset;
        }

        public Color getSubParticleColor() {
            return subParticleColor;
        }

        public int getTicksForFullCircle() {
            return ticksForFullCircle;
        }

        public OrbitalRenderRunnable getRunnable() {
            return runnable;
        }

        public OrbitalSubSizeRunnable getSubSizeRunnable() {
            return subSizeRunnable;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public int getBrightness() {
            return brightness;
        }

        public int getOffsetTicks() {
            return offsetTicks;
        }
    }

    public static abstract class OrbitalSubSizeRunnable {
        public abstract float getSubParticleSize(Random rand, int orbitalExisted);
    }

    public static abstract class OrbitalRenderRunnable {
        public abstract void onRender(World world, Vector3 selectedPosition, OrbitalRenderProperties properties, int orbitalExisted, float partialTicks);
    }

    public static class Axis {

        public static final Axis X_AXIS = new Axis(new Vector3(1, 0, 0));
        public static final Axis Y_AXIS = new Axis(new Vector3(0, 1, 0));
        public static final Axis Z_AXIS = new Axis(new Vector3(0, 0, 1));

        private Vector3 axis;

        public Axis(Vector3 axis) {
            this.axis = axis;
        }

        public static Axis persisentRandomAxis() {
            //Actually quite important to use only Y-positive here since if we
            //would use negative y, the axis may turn counter-
            //clockwise, what's intended to be set in another variable, which
            //may cause bugs if we want to use only clockwise axis'.
            return new Axis(Vector3.positiveYRandom());
        }

        public Vector3 getAxis() {
            return axis.clone();
        }
    }

}
