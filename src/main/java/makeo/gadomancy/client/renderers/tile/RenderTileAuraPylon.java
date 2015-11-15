package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelAuraPylon;
import makeo.gadomancy.client.models.ModelAuraPylonBottom;
import makeo.gadomancy.client.models.ModelAuraPylonTop;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXEssentiaTrail;
import thaumcraft.client.fx.particles.FXWisp;

import java.awt.*;

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
    public static final SimpleResourceLocation PYLON_TEXTURE = new SimpleResourceLocation("models/aurapylon.png");

    public void renderTileEntity(TileEntity tile, double x, double y, double z, float partTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y - 0.5, z + 0.5);

        bindTexture(PYLON_TEXTURE);

        GL11.glPushMatrix();
        for(int i = 0; i < 4; i++) {
            if(tile instanceof TileAuraPylonTop) {
                GL11.glPushMatrix();
                GL11.glRotatef(180, 1, 0, 0);
                GL11.glTranslatef(0, -2F, 0);
                MODEL_AURA_PYLON_TOP.render(null, 0, 0, 0, 0, 0, 0.0625f);
                GL11.glPopMatrix();
            } else {
                if(((TileAuraPylon) tile).isInputTile() || ((TileAuraPylon) tile).isLowestTile()) {
                    GL11.glPushMatrix();
                    GL11.glRotatef(180, 1, 0, 0);
                    GL11.glTranslatef(0, -2F, 0);
                    MODEL_AURA_PYLON_BOTTOM.render(null, 0, 0, 0, 0, 0, 0.0625f);
                    GL11.glPopMatrix();
                } else {
                    MODEL_AURA_PYLON.render(null, 0, 0, 0, 0, 0, 0.0625f);
                }
            }

            GL11.glRotatef(90, 0, 1, 0);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        if(tile instanceof TileAuraPylonTop && ((TileAuraPylonTop) tile).shouldRenderEffect()) {
            Aspect a = ((TileAuraPylonTop) tile).getAspect();
            if(a == null) a = Aspect.WEATHER;
            Color c = new Color(a.getColor());
            if(tile.getWorldObj().rand.nextInt(9) == 0) spawnWispParticles((TileAuraPylonTop) tile, c, 0.5F, false);
            c = c.darker().darker();
            if(tile.getWorldObj().rand.nextInt(15) == 0) spawnWispParticles((TileAuraPylonTop) tile, c, 0.25F, true);
        }

        if(tile instanceof TileAuraPylon && ((TileAuraPylon) tile).isInputTile() && ((TileAuraPylon) tile).isPartOfMultiblock() && ((TileAuraPylon) tile).getAspectType() != null) {
            Aspect a = ((TileAuraPylon) tile).getAspectType();
            TileAuraPylon master = ((TileAuraPylon) tile).getMasterTile();
            if(master == null) return;
            if(a == null) return;
            FXEssentiaTrail essentiaTrail = new FXEssentiaTrail(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, master.xCoord, master.yCoord, master.zCoord, 1, a.getColor(), 0.2F);
            essentiaTrail.noClip = true;
            ParticleEngine.instance.addEffect(tile.getWorldObj(), essentiaTrail);
        }

    }

    private void spawnWispParticles(TileAuraPylonTop tile, Color c, float size, boolean inner) {
        World worldObj = tile.getWorldObj();
        int xCoord = tile.xCoord;
        int yCoord = tile.yCoord;
        int zCoord = tile.zCoord;
        float offset1 = inner ? 0.4F : 0.3F;
        float offset2 = inner ? 0.2F : 0.4F;
        FXWisp ef = new FXWisp(worldObj, xCoord + 0.55F, yCoord + 0.7F, zCoord + 0.55F, xCoord + offset1 + worldObj.rand.nextFloat() * offset2, yCoord + 0.7F, zCoord + offset1 + worldObj.rand.nextFloat() * offset2, size, c.getRed(), c.getGreen(), c.getBlue());
        ef.setGravity(-0.04F);
        ef.shrink = true;
        ParticleEngine.instance.addEffect(worldObj, ef);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partTicks) {
        renderTileEntity(tileEntity, x, y, z, partTicks);
    }

}
