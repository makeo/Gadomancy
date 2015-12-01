package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelArcanePackager;
import makeo.gadomancy.client.models.ModelPackagerPiston;
import makeo.gadomancy.common.blocks.tiles.TileArcanePackager;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.tiles.TileJarFillable;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.11.2015 19:08
 */
public class RenderTileArcanePackager extends TileEntitySpecialRenderer {
    private static final ResourceLocation TEXTURE = new SimpleResourceLocation("/models/arcane_packager.png");
    private static final ModelArcanePackager MODEL = new ModelArcanePackager();

    private static final ResourceLocation TEXTURE_PISTON = new SimpleResourceLocation("/models/packager_piston.png");
    private static final ModelPackagerPiston MODEL_PISTON = new ModelPackagerPiston(false);
    private static final ModelPackagerPiston MODEL_PISTON_CUT = new ModelPackagerPiston(true);

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glRotatef(180 , 1, 0, 0);
        GL11.glTranslatef(0.5f, -1.5f, -0.5f);

        GL11.glRotatef(-90*((TileJarFillable)tile).facing + 270, 0, 1, 0);

        GL11.glPushMatrix();

        float pistonOffset;
        int pixelProgress = ((TileArcanePackager)tile).progress;
        if(pixelProgress <= 40) {
            pistonOffset = (pixelProgress / 40f) * (3/16f);
        } else {
            pixelProgress -= 41;
            float progress = pixelProgress / 5f;
            pistonOffset = (3/16f) - (progress * (3/16f));
        }

        GL11.glTranslatef(0, pistonOffset, 0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        bindTexture(TEXTURE_PISTON);

        if(pistonOffset < 1/16f) {
            MODEL_PISTON_CUT.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        } else {
            MODEL_PISTON.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        }

        GL11.glPopMatrix();

        bindTexture(TEXTURE);
        MODEL.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }
}
