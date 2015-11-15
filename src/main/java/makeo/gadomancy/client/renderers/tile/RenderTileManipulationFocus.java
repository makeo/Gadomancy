package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelFocusPortalPart;
import makeo.gadomancy.client.models.ModelManipulatorPart;
import makeo.gadomancy.common.blocks.tiles.TileManipulationFocus;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.10.2015 18:00
 */
public class RenderTileManipulationFocus extends TileEntitySpecialRenderer {
    private static final ModelManipulatorPart RANDOM_FOCUS = new ModelManipulatorPart();
    private static final ModelFocusPortalPart PORTAL_FOCUS = new ModelFocusPortalPart();

    public void renderTileEntityAt(TileManipulationFocus tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        bindTexture(new SimpleResourceLocation("models/manipulation_focus_" + tile.getBlockMetadata() + ".png"));

        GL11.glRotatef(180, 1, 0, 0);
        GL11.glTranslatef(0.5f, -1.5f + (2/16f), -0.5f);

        if(tile.getBlockMetadata() == 0 || tile.getBlockMetadata() == 3) {
            GL11.glPushMatrix();
            for(int i = 0; i < 4; i++) {
                if(tile.getBlockMetadata() == 0) {
                    RANDOM_FOCUS.render(null, 0, 0, 0, 0, 0, 0.0625f);
                } else {
                    PORTAL_FOCUS.render(null, 0, 0, 0, 0, 0, 0.0625f);
                }

                GL11.glRotatef(90, 0, 1, 0);
            }
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        renderTileEntityAt((TileManipulationFocus)tile, x, y, z, partialTicks);
    }
}
