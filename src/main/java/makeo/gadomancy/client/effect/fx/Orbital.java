package makeo.gadomancy.client.effect.fx;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.common.data.ModConfig;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 01.07.2015 / 19:28
 * on SoulSorcery
 * Orbital
 */
public final class Orbital {

    private Vector3 center;
    private final World world;
    private int orbitalCounter = 0;

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

    public void doRender(float partialTicks) {
        if(MiscUtils.getPositionVector(Minecraft.getMinecraft().renderViewEntity).distance(center) > ModConfig.renderParticleDistance) return;

        for(OrbitalRenderProperties orbitalNode : orbitals) {
            Axis axis = orbitalNode.getAxis();
            int counterOffset = orbitalNode.getOffsetTicks() % orbitalNode.getTicksForFullCircle();

            int currentDividedPolicyTick = (orbitalCounter + counterOffset) % orbitalNode.getTicksForFullCircle();
            float currentDegree = 360F * (((float) currentDividedPolicyTick) / ((float) orbitalNode.getTicksForFullCircle()));
            double currentRad = Math.toRadians(currentDegree);

            Vector3 point = axis.getAxis().clone().perpendicular().normalize().multiply(orbitalNode.getOffset()).rotate(currentRad, axis.getAxis()).add(center);

            if(orbitalNode.getRunnable() != null) {
                orbitalNode.getRunnable().onRender(point, orbitalNode, partialTicks);
            }

            EntityFXFlow.FXFlowBase flow = new EntityFXFlow.FXFlowBase(world, point.getX(), point.getY(), point.getZ(),
                    orbitalNode.getColor(), orbitalNode.getParticleSize(), orbitalNode.getMultiplier(), orbitalNode.getBrightness());
            Minecraft.getMinecraft().effectRenderer.addEffect(flow);
        }
    }

    public static void sheduleRenders(List<Orbital> orbitals, float partialTicks) {
        EffectHandler.orbitalsRWLock.lock();
        try {
            for(Orbital orbital : orbitals) {
                orbital.doRender(partialTicks);
            }
        } finally {
            EffectHandler.orbitalsRWLock.unlock();
        }
    }

    public static void tickOrbitals(List<Orbital> orbitals) {
        EffectHandler.orbitalsRWLock.lock();
        try {
            for(Orbital orbital : orbitals) orbital.orbitalCounter++;
        } finally {
            EffectHandler.orbitalsRWLock.unlock();
        }
    }

    public static class OrbitalRenderProperties {

        private Axis axis;
        private double offset;
        private Color color = Color.WHITE;
        private int ticksForFullCircle = 40;
        private OrbitalRenderRunnable runnable = null;
        private int multiplier = 8;
        private int brightness = 240;
        private float particleSize = 0.2F;
        private int offsetTicks = 0;

        public OrbitalRenderProperties(Axis axis, double offsetLength) {
            this.offset = offsetLength;
            this.axis = axis;
        }

        public OrbitalRenderProperties setColor(Color color) {
            this.color = color;
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

        public int getTicksForFullCircle() {
            return ticksForFullCircle;
        }

        public OrbitalRenderRunnable getRunnable() {
            return runnable;
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

    public static abstract class OrbitalRenderRunnable {
        public abstract void onRender(Vector3 selectedPosition, OrbitalRenderProperties properties, float partialTicks);
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
