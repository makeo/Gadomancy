package makeo.gadomancy.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 29.09.2015 21:18
 */
public abstract class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    protected void renderAllFaces(RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon) {
        renderer.renderFaceYNeg(block, x, y, z, icon);
        renderer.renderFaceYPos(block, x, y, z, icon);
        renderer.renderFaceZNeg(block, x, y, z, icon);
        renderer.renderFaceZPos(block, x, y, z, icon);
        renderer.renderFaceXNeg(block, x, y, z, icon);
        renderer.renderFaceXPos(block, x, y, z, icon);
    }

    protected void renderFace(int side, RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon) {
        switch (side) {
            case 0: renderer.renderFaceYNeg(block, x, y, z, icon); break;
            case 1: renderer.renderFaceYPos(block, x, y, z, icon); break;
            case 2: renderer.renderFaceZNeg(block, x, y, z, icon); break;
            case 3: renderer.renderFaceZPos(block, x, y, z, icon); break;
            case 4: renderer.renderFaceXNeg(block, x, y, z, icon); break;
            case 5: renderer.renderFaceXPos(block, x, y, z, icon); break;
        }
    }

    protected void renderFace(ForgeDirection side, RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon) {
        renderFace(side.ordinal(), renderer, block, x, y, z, icon);
    }

    /*protected void renderFaceHollow(int side, RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon,
                                    int startX, int startY, int startZ, int endX, int endY, int endZ) {

    }

    protected void renderFacePartially(int side, RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon,
                                       int startX, int startY, int startZ, int endX, int endY, int endZ) {

    }*/
}
