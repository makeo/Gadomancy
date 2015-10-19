package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.tiles.TileJarFillable;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.07.2015 21:05
 */
public class RenderTileStickyJar extends TileEntitySpecialRenderer {
    private static final RenderBlocks RENDER_BLOCKS = new RenderBlocks();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if(tile != null && tile instanceof TileStickyJar && ((TileStickyJar) tile).isValid()) {
            TileStickyJar stickyJar = (TileStickyJar) tile;

            TileJarFillable parent = stickyJar.getParent();
            World world = tile.getWorldObj();
            Block block = stickyJar.getParentBlock();

            GL11.glPushMatrix();

            GL11.glTranslated(x, y, z);
            rotateJar(stickyJar.placedOn, ForgeDirection.getOrientation(stickyJar.facing));

            //TESR
            TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(parent);
            if(renderer != null) {
                stickyJar.syncToParent();

                renderer.renderTileEntityAt(parent, 0, 0, 0, partialTicks);

                stickyJar.syncFromParent();
            }

            //ISimpleBlockHandler
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            bindTexture(TextureMap.locationBlocksTexture);

            RENDER_BLOCKS.blockAccess = world;

            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();

            tess.setNormal(0, -stickyJar.placedOn.offsetY, -Math.abs(stickyJar.placedOn.offsetZ + stickyJar.placedOn.offsetX));

            tess.setTranslation(-tile.xCoord, -tile.yCoord, -tile.zCoord);

            RENDER_BLOCKS.renderBlockByRenderType(block, tile.xCoord, tile.yCoord, tile.zCoord);

            tess.setTranslation(0, 0, 0);
            tess.draw();

            GL11.glPopAttrib();
            GL11.glPopMatrix();

            GL11.glPopMatrix();
        }
    }

    private void rotateJar(ForgeDirection placedOn, ForgeDirection facing) {
        if(placedOn == ForgeDirection.UP) {
            GL11.glTranslatef(Math.abs(facing.offsetZ), 1, Math.abs(facing.offsetX));
            GL11.glRotatef(180, facing.offsetX, 0, facing.offsetZ);
        } else if(placedOn != ForgeDirection.DOWN) {
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            GL11.glRotatef(90, placedOn.offsetZ * -1, 0, placedOn.offsetX);

            switch (placedOn) {
                case NORTH: GL11.glRotatef(0, 0, 1, 0); break;
                case EAST: GL11.glRotatef(-90, 0, 1, 0); break;
                case SOUTH: GL11.glRotatef(180, 0, 1, 0); break;
                case WEST: GL11.glRotatef(90, 0, 1, 0); break;
            }

            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        }
    }
}
