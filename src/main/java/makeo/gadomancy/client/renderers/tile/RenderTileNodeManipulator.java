package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelSmallCube;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.tile.TileWandPedestalRenderer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 15:19
 */
public class RenderTileNodeManipulator extends TileWandPedestalRenderer {
    private static final ModelSmallCube MODEL = new ModelSmallCube();
    private static final SimpleResourceLocation TEXTURE = new SimpleResourceLocation("models/small_cube.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partitalTicks) {
        super.renderTileEntityAt(tile, x, y, z, partitalTicks);
    }

    public static void renderColorCubes(float r, float g, float b, float a) {
        GL11.glPushMatrix();
        GL11.glColor4f(r, g, b, a);

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        for(int i = 0; i < 4; i++) {
            GL11.glPushMatrix();

            ForgeDirection dir = ForgeDirection.getOrientation(i + 2);
            GL11.glTranslatef(dir.offsetX * (-6/16f), 0, dir.offsetZ * (-6/16f));

            MODEL.render(null, 0, 0, 0, 0, 0, 0.0625f);
            GL11.glPopMatrix();
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
}
