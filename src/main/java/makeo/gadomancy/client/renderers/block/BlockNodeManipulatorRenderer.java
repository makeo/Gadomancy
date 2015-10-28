package makeo.gadomancy.client.renderers.block;

import makeo.gadomancy.client.renderers.tile.RenderTileNodeManipulator;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockStoneDeviceRenderer;
import thaumcraft.common.blocks.BlockStoneDevice;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 13:46
 */
public class BlockNodeManipulatorRenderer extends BlockStoneDeviceRenderer {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, -0.1f, 0);
        GL11.glScalef(0.85f, 0.85f, 0.85f);

        super.renderInventoryBlock(block, 5, modelId, renderer);

        GL11.glTranslatef(0, 1f, 0);

        renderWandPedestalFocus(block, renderer);
        RenderTileNodeManipulator.renderColorCubes(1, 1, 1, 1);

        GL11.glPopMatrix();
    }

    public static void renderWandPedestalFocus(Block block, RenderBlocks renderer) {
        block.setBlockBounds(W5, 0.0F, W5, W11, W1, W11);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W1, 0.0F, W7, W5, W1, W9);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W11, 0.0F, W7, W15, W1, W9);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W7, 0.0F, W1, W9, W1, W5);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W7, 0.0F, W11, W9, W1, W15);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        float height = W7 - (2/16f);

        block.setBlockBounds(W1, W1, W7, W3, height, W9);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W7, W1, W1, W9, height, W3);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W13, W1, W7, W15, height, W9);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);

        block.setBlockBounds(W7, W1, W13, W9, height, W15);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockStoneDevice) block).iconWandPedestalFocus[2], ((BlockStoneDevice) block).iconWandPedestalFocus[1], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], ((BlockStoneDevice) block).iconWandPedestalFocus[0], true);
    }

    @Override
    public int getRenderId() {
        return RegisteredBlocks.rendererNodeManipulator;
    }
}
