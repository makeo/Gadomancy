package makeo.gadomancy.client.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
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

    public static void notifyRenderTick(World currentRenderWorld, float partialTicks) {
        renderLock.lock();
        try {
            handleVortexDiggingEffects(currentRenderWorld);
            handleWispyLineEffects(currentRenderWorld, partialTicks);
        } finally {
            renderLock.unlock();
        }
    }

    private static void handleWispyLineEffects(World currentRenderWorld, float partialTicks) {
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
