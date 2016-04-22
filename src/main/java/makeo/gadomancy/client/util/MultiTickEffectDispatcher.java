package makeo.gadomancy.client.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXBubble;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 25.10.2015 18:54
 */
public class MultiTickEffectDispatcher {

    private static ReentrantLock renderLock = new ReentrantLock();
    private static List<VortexDigInfo> vortexDigInfos = new ArrayList<VortexDigInfo>();
    private static List<FloatyLineInfo> floatyLineInfos = new ArrayList<FloatyLineInfo>();
    private static List<BubbleFXInfo> bubbleInfos = new ArrayList<BubbleFXInfo>();

    public static void notifyRenderTick(World currentRenderWorld, float partialTicks) {
        renderLock.lock();
        try {
            handleVortexDiggingEffects(currentRenderWorld);
            handleWispyLineEffects(currentRenderWorld, partialTicks);
            handleBubbleSpawnings(currentRenderWorld);
        } finally {
            renderLock.unlock();
        }
    }

    private static void handleBubbleSpawnings(World world) {
        Iterator<BubbleFXInfo> it = bubbleInfos.iterator();
        while(it.hasNext()) {
            BubbleFXInfo info = it.next();
            if(info.dimId != world.provider.dimensionId) {
                it.remove();
                continue;
            }
            info.renderTicks++;
            if(info.renderTicks > info.overallTicks) {
                it.remove();
                continue;
            }

            if(world.rand.nextInt(3) == 0) {
                float x = info.posX + (world.rand.nextBoolean() ? 1 : -1) * (world.rand.nextFloat() * info.rangeAroundItem);
                float y = info.posY + (world.rand.nextBoolean() ? 1 : -1) * (world.rand.nextFloat() * info.rangeAroundItem);
                float z = info.posZ + (world.rand.nextBoolean() ? 1 : -1) * (world.rand.nextFloat() * info.rangeAroundItem);
                FXBubble fb = new FXBubble(world, x, y, z, 0.0D, 0.0D, 0.0D, 0);
                fb.setAlphaF(0.25F);
                fb.setRGB(1.0F, 1.0F, 1.0F);
                ParticleEngine.instance.addEffect(world, fb);
            }
        }
    }

    private static void handleWispyLineEffects(World currentRenderWorld, float partialTicks) {
        Tessellator tessellator;
        Iterator<FloatyLineInfo> it = floatyLineInfos.iterator();
        while(it.hasNext()) {
            FloatyLineInfo info = it.next();
            if(info.dimId != currentRenderWorld.provider.dimensionId) {
                it.remove();
                continue;
            }
            info.renderTicks++;
            if(info.renderTicks > info.tickCap) {
                it.remove();
                continue;
            }

            if(info.randomOffset == -1) {
                info.randomOffset = currentRenderWorld.rand.nextInt(20);
            }

            float ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted + info.randomOffset + partialTicks;
            UtilsFX.drawFloatyLine(info.pedestalX, info.pedestalY, info.pedestalZ, info.originX, info.originY, info.originZ, partialTicks, info.colorAsInt, "textures/misc/wispy.png", -0.02F, Math.min(ticks, 10.0F) / 10.0F);
        }
    }

    private static void handleVortexDiggingEffects(World currentRenderWorld) {
        Iterator<VortexDigInfo> it = vortexDigInfos.iterator();
        while(it.hasNext()) {
            VortexDigInfo info = it.next();
            if(info.dimId != currentRenderWorld.provider.dimensionId) {
                it.remove();
                continue;
            }
            info.renderTicks++;
            if(info.renderTicks > info.tickCap) {
                it.remove();
                continue;
            }

            Thaumcraft.proxy.boreDigFx(currentRenderWorld, info.tX, info.tY, info.tZ, info.oX, info.oY, info.oZ, info.blockInstance, info.meta);
        }
    }

    public static void registerBubbles(BubbleFXInfo info) {
        renderLock.lock();
        try {
            bubbleInfos.add(info);
        } finally {
            renderLock.unlock();
        }
    }

    public static void registerVortexDig(VortexDigInfo info) {
        renderLock.lock();
        try {
            vortexDigInfos.add(info);
        } finally {
            renderLock.unlock();
        }
    }

    public static void registerFloatyLine(FloatyLineInfo info) {
        renderLock.lock();
        try {
            floatyLineInfos.add(info);
        } finally {
            renderLock.unlock();
        }
    }

    public static class FloatyLineInfo {

        private int dimId;
        private double pedestalX, pedestalY, pedestalZ;
        private double originX, originY, originZ;
        private int renderTicks = 0;
        private int tickCap;
        private int colorAsInt;
        private int randomOffset = -1;

        public FloatyLineInfo(int dimId, double pedestalX, double pedestalY, double pedestalZ, double originX, double originY, double originZ, int tickCap, int colorAsInt) {
            this.dimId = dimId;
            this.pedestalX = pedestalX;
            this.pedestalY = pedestalY;
            this.pedestalZ = pedestalZ;
            this.originX = originX;
            this.originY = originY;
            this.originZ = originZ;
            this.tickCap = tickCap;
            this.colorAsInt = colorAsInt;
        }
    }

    public static class BubbleFXInfo {

        private int dimId;
        private float posX, posY, posZ;
        private int renderTicks, overallTicks;
        private float rangeAroundItem;

        public BubbleFXInfo(int dimId, float posX, float posY, float posZ, int overallTicks, float rangeAroundItem) {
            this.dimId = dimId;
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.overallTicks = overallTicks;
            this.rangeAroundItem = rangeAroundItem;
        }
    }

    public static class VortexDigInfo {

        private int dimId;
        private int oX, oY, oZ;
        private int tX, tY, tZ;
        private Block blockInstance;
        private int meta;
        private int renderTicks = 0;
        private int tickCap;

        public VortexDigInfo(int dimId, int oX, int oY, int oZ, int tX, int tY, int tZ, Block blockInstance, int meta, int tickDuration) {
            this.dimId = dimId;
            this.oX = oX;
            this.oY = oY;
            this.oZ = oZ;
            this.tX = tX;
            this.tY = tY;
            this.tZ = tZ;
            this.blockInstance = blockInstance;
            this.meta = meta;
            this.tickCap = tickDuration;
        }
    }

}
