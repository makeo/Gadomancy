package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelArcaneDropper;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.tile.TileBellowsRenderer;
import thaumcraft.common.tiles.TileBellows;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.09.2015 20:00
 */
public class RenderTileArcaneDropper extends TileEntitySpecialRenderer {
    private static final ResourceLocation RESOURCE = new SimpleResourceLocation("models/arcane_dropper.png");
    private static final ModelBase MODEL = new ModelArcaneDropper();

    private static final TileBellowsRenderer BELLOWS_RENDERER = new TileBellowsRenderer();
    private static final TileBellows BELLOWS_TILE = new TileBellows();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y - 0.5, z + 0.5);

        int metadata = tile.getBlockMetadata();

        ForgeDirection side = ForgeDirection.getOrientation(metadata & 7);

        if(side == ForgeDirection.UP) {
            GL11.glRotatef(180, 1, 0, 0);
            GL11.glTranslatef(0, -2, 0);
        } else if(side != ForgeDirection.DOWN) {
            GL11.glRotatef(-90, side.offsetZ, 0, -1*side.offsetX);
            GL11.glTranslatef(side.offsetX, -1, side.offsetZ);
        }

        boolean flipped = (metadata & 8) == 8;
        if(side == ForgeDirection.WEST || side == ForgeDirection.EAST) {
            flipped = !flipped;
        }

        if(flipped) {
            GL11.glRotatef(90, 0, 1, 0);
        }

        bindTexture(RESOURCE);
        MODEL.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        renderBellow();
        GL11.glRotatef(180, 0, 1, 0);
        renderBellow();

        GL11.glPopMatrix();
    }

    public void renderBellow() {
        GL11.glPushMatrix();

        GL11.glTranslatef(0, 0.75f, 0.25f);
        GL11.glRotatef(90f, 0, 1, 0);
        GL11.glTranslatef(-0.05f, 0.03f, 0f);
        GL11.glScalef(0.6f, 0.44f, 0.55f);

        BELLOWS_RENDERER.renderEntityAt(BELLOWS_TILE, 0, 0, 0, 0);

        GL11.glPopMatrix();
    }
}
