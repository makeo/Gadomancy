package makeo.gadomancy.client.effect.fx;

import makeo.gadomancy.common.data.ModConfig;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;

/**
 * HellFirePvP@Admin
 * Date: 01.07.2015 / 09:22
 * on SoulSorcery
 * EntityFXFlow
 */
public class EntityFXFlow extends EntityThrowable {

    private Color color;
    private Color fadingColor;

    private int policyCounter = 0;
    private EntityFXFlowPolicy policy;

    private Vector3 target = null;

    private int livingTicks = -1;

    private double motionBufferX, motionBufferY, motionBufferZ;
    private float motionMultiplier = 1;
    private float mainParticleSize = 0.2F;
    private float surroundingParticleSize = 0.1F;
    private float surroundingDistance = 0.8F;
    private double unmodMotionBufX, unmodMotionBufY, unmodMotionBufZ;

    public EntityFXFlow(World world) {
        super(world);
        setSize(0.0F, 0.0F); //Not visible
        this.policy = EntityFXFlowPolicy.Policies.DEFAULT.getPolicy();
    }

    public EntityFXFlow applyTarget(Vector3 target) {
        this.target = target;
        return this;
    }

    public EntityFXFlow setSurroundingDistance(float surroundingDistance) {
        this.surroundingDistance = (1 / surroundingDistance);
        return this;
    }

    public EntityFXFlow setMainParticleSize(float newSize) {
        this.mainParticleSize = newSize;
        return this;
    }

    public EntityFXFlow setSurroundingParticleSize(float newSize) {
        this.surroundingParticleSize = newSize;
        return this;
    }

    public EntityFXFlow setLivingTicks(int livingTicks) {
        this.livingTicks = livingTicks;
        return this;
    }

    public EntityFXFlow setColor(Color newColor) {
        this.fadingColor = this.color;
        this.color = newColor;
        return this;
    }

    public void setMotionMultiplier(float motionMultiplier) {
        this.motionMultiplier = motionMultiplier;
    }

    public EntityFXFlow setMotion(double motionX, double motionY, double motionZ) {
        this.unmodMotionBufX = motionX;
        this.unmodMotionBufY = motionY;
        this.unmodMotionBufZ = motionZ;
        motionX *= motionMultiplier;
        motionY *= motionMultiplier;
        motionZ *= motionMultiplier;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.motionBufferX = motionX;
        this.motionBufferY = motionY;
        this.motionBufferZ = motionZ;
        return this;
    }

    @Override
    public void onUpdate() {

        this.calculateVelocity();

        setVelocity(motionBufferX, motionBufferY, motionBufferZ);

        if(target == null) {
            livingTicks--;
            if (livingTicks <= 0) setDead();
        } else {
            if(target.distanceSquared(getPositionVector()) < 1) {
                setDead();
            }
        }

        super.onUpdate();
        this.inGround = false;

        if (!isDead && worldObj.isRemote) {
            policyCounter += 1;
            doParticles();
        }
    }

    private void calculateVelocity() {
        if(target == null) return;

        Vector3 pos = getPositionVector();
        double motDirX = target.getX() - pos.getX();
        double motDirY = target.getY() - pos.getY();
        double motDirZ = target.getZ() - pos.getZ();
        Vector3 mot = new Vector3(motDirX, motDirY, motDirZ).normalize().divide(4);
        setMotion(mot.getX(), mot.getY(), mot.getZ());
    }

    private Vector3 getPositionVector() {
        return new Vector3(posX, posY, posZ);
    }

    public void setPolicy(EntityFXFlowPolicy policy) {
        this.policy = policy;
    }

    public void setPolicy(EntityFXFlowPolicy.Policies policy) {
        this.policy = policy.getPolicy();
    }

    private void doParticles() {
        FXFlowBase flow = new FXFlowBase(worldObj, posX, posY, posZ, color, mainParticleSize, 9, 240);
        Minecraft.getMinecraft().effectRenderer.addEffect(flow); //Initial position.
        double lastPosX = posX - (posX - lastTickPosX) / 2.0D;
        double lastPosY = posY - (posY - lastTickPosY) / 2.0D;
        double lastPosZ = posZ - (posZ - lastTickPosZ) / 2.0D;
        FXFlowBase flow2 = new FXFlowBase(worldObj, lastPosX, lastPosY, lastPosZ, color, (float) (mainParticleSize * 0.8), 8, 240);
        Minecraft.getMinecraft().effectRenderer.addEffect(flow2); //Consistency to last position

        if(policy != null)
            policy.doSubParticles(this, policyCounter, posX, posY, posZ, lastPosX, lastPosY, lastPosZ);
    }

    @Override
    public float getGravityVelocity() {
        return 0.0F;
    }

    public Color getColor() {
        return color;
    }

    public Color getFadingColor() {
        return fadingColor;
    }

    public float getSurroundingDistance() {
        return surroundingDistance;
    }

    public float getSurroundingParticleSize() {
        return surroundingParticleSize;
    }

    public float getMainParticleSize() {
        return mainParticleSize;
    }

    public float getMotionMultiplier() {
        return motionMultiplier;
    }

    public Random getRand() {
        return rand;
    }

    public boolean isMovementVectorNullLength() {
        return new Vector3(motionBufferX, motionBufferY, motionBufferZ).lengthSquared() == 0;
    }

    public Vector3 getTarget() {
        return target;
    }

    public Vector3 getMovementVector() {
        Vector3 motion = new Vector3(motionBufferX, motionBufferY, motionBufferZ);
        if(motion.lengthSquared() == 0) return new Vector3(unmodMotionBufX, unmodMotionBufY, unmodMotionBufZ);
        return motion;
    }

    @Override
    public void onImpact(MovingObjectPosition obj) {}

    public void applyProperties(EntityFlowProperties properties) {
        if(properties != null) {
            if(properties.hasTarget) {
                applyTarget(properties.target);
            }
            if(properties.color != null) {
                if(properties.fading != null) {
                    setColor(properties.fading);
                    setColor(properties.color);
                } else {
                    setColor(properties.color);
                }
            } else {
                if(properties.fading != null) {
                    setColor(properties.fading);
                }
            }
            if(properties.livingTicks != -1)
                setLivingTicks(properties.livingTicks);
            if(properties.mainParticleSize != -1)
                setMainParticleSize(properties.mainParticleSize);
            if(properties.surroundingDistance != -1)
                setSurroundingDistance(properties.surroundingDistance);
            if(properties.surroundingParticleSize != -1)
                setSurroundingParticleSize(properties.surroundingParticleSize);
            if(properties.motionMultiplier != Float.MAX_VALUE) {
                setMotionMultiplier(properties.motionMultiplier);
            }
            if(properties.policy != null) {
                setPolicy(properties.policy);
            }
        }
    }

    public static class EntityFlowProperties {

        protected boolean hasTarget;
        protected Vector3 target;
        protected Color color, fading;
        protected EntityFXFlowPolicy policy;
        protected float mainParticleSize = -1, surroundingParticleSize = -1, surroundingDistance = -1;
        protected int livingTicks = -1;
        protected float motionMultiplier = Float.MAX_VALUE;

        public EntityFlowProperties() {}

        public EntityFlowProperties setTarget(Vector3 target) {
            this.hasTarget = target != null;
            this.target = target;
            return this;
        }

        public EntityFlowProperties setPolicy(EntityFXFlowPolicy policy) {
            this.policy = policy;
            return this;
        }

        public EntityFlowProperties setPolicy(EntityFXFlowPolicy.Policies policy) {
            this.policy = policy.getPolicy();
            return this;
        }

        public EntityFlowProperties setColor(Color color) {
            this.color = color;
            return this;
        }

        public EntityFlowProperties setMotionMultiplier(float motionMultiplier) {
            this.motionMultiplier = motionMultiplier;
            return this;
        }

        public EntityFlowProperties setFading(Color fading) {
            this.fading = fading;
            return this;
        }

        public EntityFlowProperties setMainParticleSize(float mainParticleSize) {
            this.mainParticleSize = mainParticleSize;
            return this;
        }

        public EntityFlowProperties setSurroundingParticleSize(float surroundingParticleSize) {
            this.surroundingParticleSize = surroundingParticleSize;
            return this;
        }

        public EntityFlowProperties setSurroundingDistance(float surroundingDistance) {
            this.surroundingDistance = surroundingDistance;
            return this;
        }

        public EntityFlowProperties setLivingTicks(int livingTicks) {
            this.livingTicks = livingTicks;
            return this;
        }

    }

    public static class FXFlowBase extends EntityFX {

        private static Queue<FXFlowBase> fxQueue = new ArrayDeque<FXFlowBase>();

        private int partBlue, partGreen, partRed, partAlpha;

        //Queue variables
        private float partialTicks;
        private float rendArg1, rendArg2, rendArg3, rendArg4, rendArg5;
        private int rendBrightness;

        private ResourceLocation texture = new SimpleResourceLocation("effect/flow_large.png");
        private float buffQuadLife, buffParticleScale;

        public FXFlowBase(World world, double x, double y, double z, Color color, float size, int multiplier, int brightness) {
            super(world, x, y, z);
            if(color != null) {
                this.partBlue = color.getBlue();
                this.partGreen = color.getGreen();
                this.partRed = color.getRed();
                this.partAlpha = color.getAlpha();
            }
            this.motionX = this.motionY = this.motionZ = 0;
            this.particleScale *= size;
            this.particleMaxAge = 3 * multiplier;
            this.noClip = false;
            this.setSize(0.01F, 0.01F);
            this.particleAge = 0;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.particleTextureIndexX = 0;
            this.particleTextureIndexY = 0;
            this.buffQuadLife = (this.particleMaxAge / 2);
            this.buffParticleScale = this.particleScale;
            this.rendBrightness = brightness;
            this.noClip = true;
        }

        public static void sheduleRender(Tessellator tessellator) {
            boolean isLightingEnabled = GL11.glGetBoolean(GL11.GL_LIGHTING);

            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            if(isLightingEnabled)
                GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

            for(FXFlowBase fx : fxQueue) {
                tessellator.startDrawingQuads();
                fx.renderEssenceBase(tessellator);
                tessellator.draw();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

            if(isLightingEnabled)
                GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);

            fxQueue.clear();
        }

        private void renderEssenceBase(Tessellator tessellator) {
            tessellator.setBrightness(rendBrightness);
            GL11.glColor4f(partRed, partGreen, partBlue, partAlpha);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            if(particleAge >= particleMaxAge) return;
            float agescale = (float) particleAge / buffQuadLife;
            if(agescale >= 1.0F) agescale = 4 - agescale;
            particleScale = buffParticleScale * agescale;
            float f10 = 0.5F * particleScale;
            float f11 = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
            float f12 = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
            float f13 = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

            tessellator.addVertexWithUV(f11 - rendArg1 * f10 - rendArg4 * f10, f12 - rendArg2 * f10, f13 - rendArg3 * f10 - rendArg5 * f10, 0, 1);
            tessellator.addVertexWithUV(f11 - rendArg1 * f10 + rendArg4 * f10, f12 + rendArg2 * f10, f13 - rendArg3 * f10 + rendArg5 * f10, 1, 1);
            tessellator.addVertexWithUV(f11 + rendArg1 * f10 + rendArg4 * f10, f12 + rendArg2 * f10, f13 + rendArg3 * f10 + rendArg5 * f10, 1, 0);
            tessellator.addVertexWithUV(f11 + rendArg1 * f10 - rendArg4 * f10, f12 - rendArg2 * f10, f13 + rendArg3 * f10 - rendArg5 * f10, 0, 0);
        }

        @Override
        public void renderParticle(Tessellator tessellator, float partialTicks, float par3, float par4, float par5, float par6, float par7) {
            if(MiscUtils.getPositionVector(Minecraft.getMinecraft().renderViewEntity).distance(new Vector3(posX, posY, posZ)) > ModConfig.renderParticleDistance) return;

            this.partialTicks = partialTicks;
            this.rendArg1 = par3;
            this.rendArg2 = par4;
            this.rendArg3 = par5;
            this.rendArg4 = par6;
            this.rendArg5 = par7;

            fxQueue.add(this);
        }

    }

}
