package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelBlockProtector;
import makeo.gadomancy.common.blocks.tiles.TileBlockProtector;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.ModelJar;
import thaumcraft.client.renderers.tile.TileJarRenderer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 11.11.2015 16:28
 */
public class RenderTileBlockProtector extends TileJarRenderer {
    private static final ResourceLocation TEXTURE_OFF = new SimpleResourceLocation("models/block_protector_off.png");
    private static final ResourceLocation TEXTURE_ON = new SimpleResourceLocation("models/block_protector_on.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        renderTileEntityAt((TileBlockProtector)tile, x, y, z, partialTicks);
    }

    public void renderTileEntityAt(TileBlockProtector tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        GL11.glTranslatef(0.5f, 0, 0.5f);
        GL11.glRotatef(90*tile.facing, 0, 1, 0);
        GL11.glTranslatef(-0.5f, 0, -0.5f);

        GL11.glPushMatrix();
        GL11.glTranslatef(0, -2/16f - 0.03f, 0);
        GL11.glScalef(0.4f, 0.4f, 0.4f);
        GL11.glTranslatef(0.75f, 0.75f, 0.75f);

        int oldFacing = tile.facing;
        tile.facing = 0;
        super.renderTileEntityAt(tile, 0, 0, 0, partialTicks);
        tile.facing = oldFacing;

        GL11.glPopMatrix();


        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 1.5f, 0.5f);
        GL11.glRotatef(180, 1, 0, 0);

        bindTexture(tile.getPowerLevel() > 0 ? TEXTURE_ON : TEXTURE_OFF);
        new ModelBlockProtector().render(null, 0, 0, 0, 0, 0, 0.0625f);
        GL11.glPopMatrix();


        GL11.glTranslatef(0, -2/16f - 0.03f, 0);
        GL11.glScalef(0.4f, 0.4f, 0.4f);
        GL11.glTranslatef(0.75f, 0.75f, 0.75f);

        GL11.glRotatef(180, 0, 0, 1);
        GL11.glTranslatef(-0.5f, 0, 0.5f);

        bindTexture(new ResourceLocation("thaumcraft", "textures/models/jar.png"));
        new ModelJar().renderAll();

        GL11.glPopMatrix();
    }
}
