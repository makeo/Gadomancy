package makeo.gadomancy.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockStoneDeviceRenderer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 13:46
 */
public class BlockNodeManipulatorRenderer implements ISimpleBlockRenderingHandler {
    public static final BlockStoneDeviceRenderer PARENT_RENDERER = new BlockStoneDeviceRenderer();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, -0.1f, 0);
        GL11.glScalef(0.85f, 0.85f, 0.85f);

        PARENT_RENDERER.renderInventoryBlock(block, 5, modelId, renderer);

        GL11.glTranslatef(0, 1f, 0);
        PARENT_RENDERER.renderInventoryBlock(block, 8, modelId, renderer);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return PARENT_RENDERER.renderWorldBlock(world, x, y, z, block, modelId, renderer);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RegisteredBlocks.rendererNodeManipulator;
    }
}
