package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.blocks.tiles.TileAIShutdown;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;

import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 09.06.2016 / 12:56
 */
public class RenderTileAIShutdown extends TileEntitySpecialRenderer {

    private static final Random rand = new Random();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float parTicks) {
        if(tileEntity == null || !(tileEntity instanceof TileAIShutdown)) return;
        TileAIShutdown ta = (TileAIShutdown) tileEntity;

        if(ta.getStoredEssentia() > 0) {
            handleOrbital(ta);
        }
    }

    private void handleOrbital(TileAIShutdown ta) {
        if(ta.orbital == null && ta.getWorldObj() != null) {
            ta.orbital = new Orbital(new Vector3(ta.xCoord + 0.5, ta.yCoord + 0.2, ta.zCoord + 0.5), ta.getWorldObj());
        }

        if(ta.orbital != null && !ta.orbital.registered) {
            EffectHandler.getInstance().registerOrbital(ta.orbital);
        }

        if(ta.orbital != null) ta.orbital.lastRenderCall = System.currentTimeMillis();

        if(ta.getStoredEssentia() > 0) {
            if(ta.orbital != null && ta.orbital.orbitalsSize() == 0) {
                fillOrbital(ta.orbital);
            }
        } else {
            if(ta.orbital != null && ta.orbital.orbitalsSize() > 0) {
                ta.orbital.clearOrbitals();
            }
        }
    }

    private void fillOrbital(final Orbital orbital) {
        Orbital.OrbitalRenderProperties prop = new Orbital.OrbitalRenderProperties(Orbital.Axis.Y_AXIS, 1.5);
        prop.setTicksForFullCircle(50).setOffsetTicks(0);
        prop.setParticleSize(0F);
        prop.setRenderRunnable(new Orbital.OrbitalRenderRunnable() {
            @Override
            public void onRender(World world, Vector3 pos, Orbital.OrbitalRenderProperties properties, int orbitalExisted, float partialTicks) {
                if(rand.nextInt(3) == 0) {
                    FXWisp ef = new FXWisp(world,
                            pos.getX() + randomOffset(), pos.getY() + randomOffset(), pos.getZ() + randomOffset(),
                            0.2F, 3);
                    ef.setGravity(-0.08F);
                    ParticleEngine.instance.addEffect(world, ef);
                }
            }
        });
        orbital.addOrbitalPoint(prop);

        prop = new Orbital.OrbitalRenderProperties(Orbital.Axis.Y_AXIS, 1.5);
        prop.setTicksForFullCircle(50).setOffsetTicks(25);
        prop.setParticleSize(0F);
        prop.setRenderRunnable(new Orbital.OrbitalRenderRunnable() {
            @Override
            public void onRender(World world, Vector3 pos, Orbital.OrbitalRenderProperties properties, int orbitalExisted, float partialTicks) {
                if(rand.nextInt(3) == 0) {
                    FXWisp ef = new FXWisp(world,
                            pos.getX() + randomOffset(), pos.getY() + randomOffset(), pos.getZ() + randomOffset(),
                            0.2F, 3);
                    ef.setGravity(-0.08F);
                    ParticleEngine.instance.addEffect(world, ef);
                }
            }
        });
        orbital.addOrbitalPoint(prop);

        prop = new Orbital.OrbitalRenderProperties(Orbital.Axis.Y_AXIS, 3);
        prop.setTicksForFullCircle(90).setOffsetTicks(0);
        prop.setParticleSize(0F);
        prop.setRenderRunnable(new Orbital.OrbitalRenderRunnable() {
            @Override
            public void onRender(World world, Vector3 pos, Orbital.OrbitalRenderProperties properties, int orbitalExisted, float partialTicks) {
                if(rand.nextBoolean()) {
                    FXWisp ef = new FXWisp(world,
                            pos.getX() + randomOffset(), pos.getY() + randomOffset(), pos.getZ() + randomOffset(),
                            0.15F, 5);
                    ef.setGravity(-0.08F);
                    ParticleEngine.instance.addEffect(world, ef);
                }
            }
        });
        orbital.addOrbitalPoint(prop);

        prop = new Orbital.OrbitalRenderProperties(Orbital.Axis.Y_AXIS, 3);
        prop.setTicksForFullCircle(90).setOffsetTicks(30);
        prop.setParticleSize(0F);
        prop.setRenderRunnable(new Orbital.OrbitalRenderRunnable() {
            @Override
            public void onRender(World world, Vector3 pos, Orbital.OrbitalRenderProperties properties, int orbitalExisted, float partialTicks) {
                if(rand.nextBoolean()) {
                    FXWisp ef = new FXWisp(world,
                            pos.getX() + randomOffset(), pos.getY() + randomOffset(), pos.getZ() + randomOffset(),
                            0.15F, 5);
                    ef.setGravity(-0.08F);
                    ParticleEngine.instance.addEffect(world, ef);
                }
            }
        });
        orbital.addOrbitalPoint(prop);

        prop = new Orbital.OrbitalRenderProperties(Orbital.Axis.Y_AXIS, 3);
        prop.setTicksForFullCircle(90).setOffsetTicks(60);
        prop.setParticleSize(0F);
        prop.setRenderRunnable(new Orbital.OrbitalRenderRunnable() {
            @Override
            public void onRender(World world, Vector3 pos, Orbital.OrbitalRenderProperties properties, int orbitalExisted, float partialTicks) {
                if(rand.nextBoolean()) {
                    FXWisp ef = new FXWisp(world,
                            pos.getX() + randomOffset(), pos.getY() + randomOffset(), pos.getZ() + randomOffset(),
                            0.15F, 5);
                    ef.setGravity(-0.08F);
                    ParticleEngine.instance.addEffect(world, ef);
                }
            }
        });
        orbital.addOrbitalPoint(prop);

    }

    private static float randomOffset() {
        return rand.nextFloat() * 0.2F;
    }

}
