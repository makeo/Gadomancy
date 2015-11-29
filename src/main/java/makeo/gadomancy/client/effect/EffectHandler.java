package makeo.gadomancy.client.effect;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.client.effect.fx.EntityFXFlow;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 17.11.2015 18:39
 */
public class EffectHandler {

    public static final EffectHandler instance = new EffectHandler();

    public static List<Orbital> orbitals = new ArrayList<Orbital>();

    public static Lock orbitalsRWLock = new NonReentrantReentrantLock();

    public static EffectHandler getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        Tessellator tessellator = Tessellator.instance;

        EntityFXFlow.FXFlowBase.sheduleRender(tessellator);
        Orbital.sheduleRenders(orbitals, event.partialTicks);
    }

    public EntityFXFlow effectFlow(World world, Vector3 origin, EntityFXFlow.EntityFlowProperties properties) {
        EntityFXFlow flow = new EntityFXFlow(world);
        flow.applyProperties(properties);
        flow.setPosition(origin.getX(), origin.getY(), origin.getZ());
        world.spawnEntityInWorld(flow);
        return flow;
    }

    public void registerOrbital(final Orbital orbital) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                orbitalsRWLock.lock();
                try {
                    orbitals.add(orbital);
                    orbital.registered = true;
                } finally {
                    orbitalsRWLock.unlock();
                }
            }
        }).start();
    }

    public void unregisterOrbital(final Orbital orbital) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                orbitalsRWLock.lock();
                try {
                    orbitals.remove(orbital);
                    orbital.registered = false;
                } finally {
                    orbitalsRWLock.unlock();
                }
            }
        }).start();
    }

    public void tick() {
        Orbital.tickOrbitals(orbitals);
    }

    public void clear() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                orbitalsRWLock.lock();
                try {
                    orbitals.clear();
                } finally {
                    orbitalsRWLock.unlock();
                }
            }
        }).start();
    }

    public static class NonReentrantReentrantLock extends ReentrantLock {

        @Override
        public boolean isHeldByCurrentThread() {
            return false;
        }

        @Override
        public void lock() {
            super.lock();
        }

        @Override
        public void unlock() {
            super.unlock();
        }

    }

}
