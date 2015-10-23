package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 22.10.2015 19:44
 */
public class RenderTileExtendedNode extends TileNodeRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if(!(tile instanceof TileExtendedNode)) return;
        TileExtendedNode node = (TileExtendedNode) tile;
        EntityLivingBase renderView = Minecraft.getMinecraft().renderViewEntity;
        renderView.posX += node.xMoved;
        renderView.posY += node.yMoved;
        renderView.posZ += node.zMoved;
        super.renderTileEntityAt(tile, 0, 0, 0, partialTicks);
        renderView.posX -= node.xMoved;
        renderView.posY -= node.yMoved;
        renderView.posZ -= node.zMoved;
    }
}
