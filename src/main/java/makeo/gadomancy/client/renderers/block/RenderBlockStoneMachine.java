package makeo.gadomancy.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import makeo.gadomancy.common.blocks.BlockStoneMachine;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.client.renderers.block.BlockStoneDeviceRenderer;
import thaumcraft.common.blocks.BlockStoneDevice;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 03.11.2015 22:09
 */
public class RenderBlockStoneMachine implements ISimpleBlockRenderingHandler {
    private static final BlockStoneDeviceRenderer DEVICE_RENDERER = new BlockStoneDeviceRenderer();
    private static final BlockStoneDevice DEVICE_BLOCK = new BlockStoneDevice();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(metadata == 1) {
            BlockStoneMachine blockStoneMachine = (BlockStoneMachine)block;
            DEVICE_BLOCK.iconPedestal[0] = blockStoneMachine.pedestalSideIcon;
            DEVICE_BLOCK.iconPedestal[1] = blockStoneMachine.pedestalTopIcon;
            DEVICE_RENDERER.renderInventoryBlock(DEVICE_BLOCK, 1, modelId, renderer);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int metadata = world.getBlockMetadata(x, y, z);
        if(metadata == 1) {
            DEVICE_RENDERER.renderWorldBlock(world, x, y, z, block, modelId, renderer);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RegisteredBlocks.rendererBlockStoneMachine;
    }
}
