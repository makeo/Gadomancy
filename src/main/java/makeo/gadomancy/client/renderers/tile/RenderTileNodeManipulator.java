package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.renderers.block.BlockNodeManipulatorRenderer;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
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
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partitalTicks) {
        super.renderTileEntityAt(tile, x, y, z, partitalTicks);

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(0.5f, 1.5f, 0.5f);

        bindTexture(TextureMap.locationBlocksTexture);
        BlockNodeManipulatorRenderer.PARENT_RENDERER.renderInventoryBlock(RegisteredBlocks.blockNodeManipulator, 8, 0, RenderBlocks.getInstance());

        GL11.glPopMatrix();
    }
}
