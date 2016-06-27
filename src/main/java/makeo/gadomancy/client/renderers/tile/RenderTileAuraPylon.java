package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.client.models.ModelAuraPylon;
import makeo.gadomancy.client.models.ModelAuraPylonBottom;
import makeo.gadomancy.client.models.ModelAuraPylonTop;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXEssentiaTrail;
import thaumcraft.client.fx.particles.FXWisp;

import java.awt.*;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 12.11.2015 21:33
 */
public class RenderTileAuraPylon extends TileEntitySpecialRenderer {

    public static final ModelBase MODEL_AURA_PYLON = new ModelAuraPylon();
    public static final ModelBase MODEL_AURA_PYLON_TOP = new ModelAuraPylonTop();
    public static final ModelBase MODEL_AURA_PYLON_BOTTOM = new ModelAuraPylonBottom();
    public static final SimpleResourceLocation PYLON_TEXTURE_TOP = new SimpleResourceLocation("models/aura_pylon_peak.png");
    public static final SimpleResourceLocation PYLON_TEXTURE = new SimpleResourceLocation("models/aura_pylon.png");
    public static final SimpleResourceLocation PYLON_TEXTURE_BOTTOM = new SimpleResourceLocation("models/aura_pylon_base.png");

    public void renderTileEntity(TileEntity tile, double x, double y, double z, float partTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y - 0.5, z + 0.5);

        GL11.glPushMatrix();
        ResourceLocation textureToBind;
        if(tile instanceof TileAuraPylonTop) {
            textureToBind = PYLON_TEXTURE_TOP;
        } else if(((TileAuraPylon) tile).isInputTile() || ((TileAuraPylon) tile).isLowestTile()) {
            textureToBind = PYLON_TEXTURE_BOTTOM;
        } else {
            textureToBind = PYLON_TEXTURE;
        }
        bindTexture(textureToBind);
        for(int i = 0; i < 4; i++) {
            if(tile instanceof TileAuraPylonTop) {
                GL11.glPushMatrix();
                GL11.glRotatef(180, 1, 0, 0);
                GL11.glTranslatef(0, -2F, 0);
                //bindTexture(PYLON_TEXTURE_TOP);
                MODEL_AURA_PYLON_TOP.render(null, 0, 0, 0, 0, 0, 0.0625f);
                GL11.glPopMatrix();
            } else {
                if(((TileAuraPylon) tile).isInputTile() || ((TileAuraPylon) tile).isLowestTile()) {
                    GL11.glPushMatrix();
                    GL11.glRotatef(180, 1, 0, 0);
                    GL11.glTranslatef(0, -2F, 0);
                    //bindTexture(PYLON_TEXTURE_BOTTOM);
                    MODEL_AURA_PYLON_BOTTOM.render(null, 0, 0, 0, 0, 0, 0.0625f);
                    GL11.glPopMatrix();
                } else {
                    //bindTexture(PYLON_TEXTURE);
                    MODEL_AURA_PYLON.render(null, 0, 0, 0, 0, 0, 0.0625f);
                }
            }

            GL11.glRotatef(90, 0, 1, 0);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        if(tile instanceof TileAuraPylonTop) {
            TileAuraPylonTop ta = (TileAuraPylonTop) tile;
            if(ta.orbital == null && tile.getWorldObj() != null) {
                ta.orbital = new Orbital(new Vector3(tile.xCoord + 0.5, tile.yCoord + 0.7, tile.zCoord + 0.5), tile.getWorldObj());
            }

            if(ta.orbital != null && !ta.orbital.registered) {
                EffectHandler.getInstance().registerOrbital(ta.orbital);
            }

            if(ta.shouldRenderEffect()) {
                Aspect a = ta.getAspect();
                if(a == null) a = Aspect.WEATHER;
                Color c = new Color(a.getColor());
                if(tile.getWorldObj().rand.nextInt(12) == 0) {
                    if(a == Aspect.ENTROPY || a == Aspect.DARKNESS || a == Aspect.UNDEAD) {
                        spawnWispParticles((TileAuraPylonTop) tile, 5, 0.5F, false);
                    } else {
                        spawnWispParticles((TileAuraPylonTop) tile, c, 0.5F, false);
                    }
                }
                c = c.darker().darker();
                if(tile.getWorldObj().rand.nextInt(20) == 0) {
                    if(a == Aspect.ENTROPY || a == Aspect.DARKNESS || a == Aspect.UNDEAD) {
                        spawnWispParticles((TileAuraPylonTop) tile, 5, 0.25F, false);
                    } else {
                        spawnWispParticles((TileAuraPylonTop) tile, c, 0.25F, false);
                    }
                }
            }

            if(ta.orbital != null) ta.orbital.lastRenderCall = System.currentTimeMillis();

            if(ta.shouldRenderAuraEffect() && ta.getAspect() != null) {
                if(ta.orbital != null && ta.orbital.orbitalsSize() == 0) {
                    Aspect a = ta.getAspect();
                    if(a != null) {
                        int col = a.getColor();
                        col |= (0x55 << 24);
                        Color c = new Color(col, true);
                        Random rand = tile.getWorldObj().rand;
                        addNewOrbitalPoint(ta.orbital, rand, c);
                        addNewOrbitalPoint(ta.orbital, rand, c);
                        addNewOrbitalPoint(ta.orbital, rand, c);
                    }
                }
            } else {
                if(ta.orbital != null && ta.orbital.orbitalsSize() > 0) {
                    ta.orbital.clearOrbitals();
                }
            }
        }
    }

    private void addNewOrbitalPoint(Orbital orbital, Random rand, Color color) {
        Orbital.OrbitalRenderProperties properties = new Orbital.OrbitalRenderProperties(Orbital.Axis.persisentRandomAxis(), rand.nextDouble() + 2D);
        properties.setColor(color).setTicksForFullCircle(60 + rand.nextInt(40)).setOffsetTicks(rand.nextInt(80));
        properties.setSubParticleColor(color.brighter().brighter());
        orbital.addOrbitalPoint(properties);
    }

    private void spawnWispParticles(TileAuraPylonTop tile, int type, float size, boolean inner) {
        World worldObj = tile.getWorldObj();
        int xCoord = tile.xCoord;
        int yCoord = tile.yCoord;
        int zCoord = tile.zCoord;
        float offset1 = inner ? 0.4F : 0.3F;
        float offset2 = inner ? 0.2F : 0.4F;
        FXWisp ef = new FXWisp(worldObj, xCoord + 0.55F, yCoord + 0.7F, zCoord + 0.55F, xCoord + offset1 + worldObj.rand.nextFloat() * offset2, yCoord + 0.7F, zCoord + offset1 + worldObj.rand.nextFloat() * offset2, size, type);
        ef.setGravity(-0.04F);
        ef.shrink = true;
        ParticleEngine.instance.addEffect(worldObj, ef);
    }

    private void spawnWispParticles(TileAuraPylonTop tile, Color c, float size, boolean inner) {
        World worldObj = tile.getWorldObj();
        int xCoord = tile.xCoord;
        int yCoord = tile.yCoord;
        int zCoord = tile.zCoord;
        float offset1 = inner ? 0.4F : 0.3F;
        float offset2 = inner ? 0.2F : 0.4F;
        float red = c.getRed() / 255F;
        float green = c.getGreen() / 255F;
        float blue = c.getBlue() / 255F;
        FXWisp ef = new FXWisp(worldObj, xCoord + 0.55F, yCoord + 0.7F, zCoord + 0.55F, xCoord + offset1 + worldObj.rand.nextFloat() * offset2, yCoord + 0.7F, zCoord + offset1 + worldObj.rand.nextFloat() * offset2, size, red, green, blue);
        ef.setGravity(-0.04F);
        ef.shrink = true;
        ParticleEngine.instance.addEffect(worldObj, ef);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partTicks) {
        renderTileEntity(tileEntity, x, y, z, partTicks);
    }

}
