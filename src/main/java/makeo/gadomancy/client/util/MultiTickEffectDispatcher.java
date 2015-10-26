package makeo.gadomancy.client.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;
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

    public static void notifyRenderTick(World currentRenderWorld) {
        renderLock.lock();
        try {
            handleVortexDiggingEffects(currentRenderWorld);
        } finally {
            renderLock.unlock();
        }
    }

    private static void handleVortexDiggingEffects(World currentRenderWorld) {
        Iterator<VortexDigInfo> it = vortexDigInfos.iterator();
        while(it.hasNext()) {
            VortexDigInfo info = it.next();
            if(info.dimId != currentRenderWorld.provider.dimensionId) it.remove();
            info.renderTicks++;
            if(info.renderTicks > 7) it.remove();

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

    public static class VortexDigInfo {

        private int dimId;
        private int oX, oY, oZ;
        private int tX, tY, tZ;
        private Block blockInstance;
        private int meta;
        private int renderTicks = 0;

        public VortexDigInfo(int dimId, int oX, int oY, int oZ, int tX, int tY, int tZ, Block blockInstance, int meta) {
            this.dimId = dimId;
            this.oX = oX;
            this.oY = oY;
            this.oZ = oZ;
            this.tX = tX;
            this.tY = tY;
            this.tZ = tZ;
            this.blockInstance = blockInstance;
            this.meta = meta;
        }
    }

}
