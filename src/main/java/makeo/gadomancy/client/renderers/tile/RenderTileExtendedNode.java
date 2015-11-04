package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.node.ExtendedNodeType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 22.10.2015 19:44
 */
public class RenderTileExtendedNode extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileExtendedNode)) return;
        TileExtendedNode node = (TileExtendedNode) tile;
        double rX = node.xCoord + 0.5;
        double rY = node.yCoord + 0.5;
        double rZ = node.zCoord + 0.5;
        float size = 1.0F;
        if(node.getExtendedNodeType() != null && node.getExtendedNodeType() == ExtendedNodeType.GROWING) {
            size *= 1.5F;
        }
        RenderTileNodeBasic.renderTileEntityAt(tile, rX, rY, rZ, partialTicks, size);
    }
}
