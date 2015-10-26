package makeo.gadomancy.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.config.ConfigBlocks;

/**
 * This class is NOT part of the Gadomancy Mod
 * This file is copied from Azanors thaumcraft.client.renderers.block.BlockRenderer.java and
 * thaumcraft.client.renderers.block.BlockJarRenderer.java
 * Thaumcraft: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1292130
 *
 * Created by Azanor
 * Modified to create compatibility with ExtendedNodes
 * Modified by HellFirePvP @ 26.10.2015 00:29
 */
public class BlockExtendedNodeJarRenderer implements ISimpleBlockRenderingHandler {

    public static float W1 = 0.0625F;
    public static float W2 = 0.125F;
    public static float W3 = 0.1875F;
    public static float W4 = 0.25F;
    public static float W5 = 0.3125F;
    public static float W6 = 0.375F;
    public static float W7 = 0.4375F;
    public static float W8 = 0.5F;
    public static float W9 = 0.5625F;
    public static float W10 = 0.625F;
    public static float W11 = 0.6875F;
    public static float W12 = 0.75F;
    public static float W13 = 0.8125F;
    public static float W14 = 0.875F;
    public static float W15 = 0.9375F;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        IIcon i1 = ((BlockJar) ConfigBlocks.blockJar).iconJarTop;
        IIcon i2 = ((BlockJar) ConfigBlocks.blockJar).iconJarSide;
        block.setBlockBounds(W3, 0.0F, W3, W13, W12, W13);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockJar) ConfigBlocks.blockJar).iconJarBottom, i1, i2, i2, i2, i2, true);
        block.setBlockBounds(W5, W12, W5, W11, W14, W11);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockJar) ConfigBlocks.blockJar).iconJarBottom, i1, i2, i2, i2, i2, true);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int bb = setBrightness(world, x, y, z, block);
        int metadata = world.getBlockMetadata(x, y, z);
        block.setBlockBounds(W3, 0.0F, W3, W13, W12, W13);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(W5, W12, W5, W11, W14, W11);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.clearOverrideBlockTexture();
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBoundsFromBlock(block);
        return true;
    }

    public static void drawFaces(RenderBlocks renderblocks, Block block, IIcon i1, IIcon i2, IIcon i3, IIcon i4, IIcon i5, IIcon i6, boolean solidtop) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, i1);
        tessellator.draw();
        if (solidtop) {
            GL11.glDisable(3008);
        }
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, i2);
        tessellator.draw();
        if (solidtop) {
            GL11.glEnable(3008);
        }
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, i3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, i4);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, i5);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, i6);
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    private int setBrightness(IBlockAccess blockAccess, int i, int j, int k, Block block) {
        Tessellator tessellator = Tessellator.instance;
        int mb = block.getMixedBrightnessForBlock(blockAccess, i, j, k);
        tessellator.setBrightness(mb);

        float f = 1.0F;

        int l = block.colorMultiplier(blockAccess, i, j, k);
        float f1 = (l >> 16 & 0xFF) / 255.0F;
        float f2 = (l >> 8 & 0xFF) / 255.0F;
        float f3 = (l & 0xFF) / 255.0F;
        if (EntityRenderer.anaglyphEnable) {
            float f6 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f7 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f6;
            f2 = f4;
            f3 = f7;
        }
        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        return mb;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RegisteredBlocks.rendererExtendedNodeJarBlock;
    }

}
