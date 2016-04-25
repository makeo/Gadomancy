package makeo.gadomancy.client.effect;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.client.effect.fx.FXFlow;
import makeo.gadomancy.client.effect.fx.FXVortex;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public static List<Orbital> orbitals = new LinkedList<Orbital>();
    public static List<FXFlow> fxFlows = new LinkedList<FXFlow>();
    public static List<FXVortex> fxVortexes = new LinkedList<FXVortex>();

    //Object that the EffectHandler locks on.
    public static final Object lockEffects = new Object();

    public static EffectHandler getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        Tessellator tessellator = Tessellator.instance;

        FXFlow.FXFlowBase.sheduleRender(tessellator);
        Orbital.sheduleRenders(orbitals, event.partialTicks);
        FXVortex.sheduleRender(fxVortexes, tessellator, event.partialTicks);
    }

    public FXFlow effectFlow(World world, Vector3 origin, FXFlow.EntityFlowProperties properties) {
        FXFlow flow = new FXFlow(world);
        flow.applyProperties(properties).setPosition(origin);
        registerFlow(flow);
        return flow;
    }

    public void registerVortex(final FXVortex vortex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //System.out.println("register");
                synchronized (lockEffects) {
                    fxVortexes.add(vortex);
                    vortex.registered = true;
                }
            }
        }).start();
    }

    public void unregisterVortex(final FXVortex vortex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //System.out.println("unregister");
                synchronized (lockEffects) {
                    fxVortexes.remove(vortex);
                    vortex.registered = false;
                }
            }
        }).start();
    }

    public void registerFlow(final FXFlow flow) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockEffects) {
                    fxFlows.add(flow);
                }
            }
        }).start();
    }

    public void unregisterFlow(final FXFlow flow) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockEffects) {
                    fxFlows.remove(flow);
                }
            }
        }).start();
    }

    public void registerOrbital(final Orbital orbital) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockEffects) {
                    orbitals.add(orbital);
                    orbital.registered = true;
                }
            }
        }).start();
    }

    public void unregisterOrbital(final Orbital orbital) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockEffects) {
                    orbitals.remove(orbital);
                    orbital.registered = false;
                }
            }
        }).start();
    }

    public void tick() {
        Orbital.tickOrbitals(orbitals);
        FXFlow.tickFlows(fxFlows);
        FXVortex.tickVortexes(fxVortexes);
    }

    public void clear() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockEffects) {
                    orbitals.clear();
                    fxFlows.clear();
                    fxVortexes.clear();
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
