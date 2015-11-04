package makeo.gadomancy.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

/**
 * This class is NOT part of the Gadomancy Mod
 * This file is copied from Azanors thaumcraft.client.renderers.tile.ItemNodeRenderer.java and contains small modifications
 * Thaumcraft: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1292130
 *
 * Created by Azanor
 * Modified by HellFirePvP @ 24.10.2015 17:19
 */
public class ItemNodeRenderer implements IItemRenderer {

    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return (item != null) && (item.getItem() == Item.getItemFromBlock(ConfigBlocks.blockAiry)) && ((item.getItemDamage() == 0) || (item.getItemDamage() == 5));
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        if (helper == IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK) {
            return false;
        }
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        //Gadomancy: Redirect.
        renderNodeItem(type, item, data);
    }

    //Gadomancy: Renamed to access it static from another class and added aspects, nodeType and nodeModifier as params
    public static void renderNodeItem(IItemRenderer.ItemRenderType type, ItemStack item, AspectList aspects, NodeType nodeType, NodeModifier nodeModifier, Object... data) {
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
        } else if ((type == IItemRenderer.ItemRenderType.EQUIPPED) && ((data[1] instanceof EntityPlayer))) {
            GL11.glTranslatef(0.0F, 0.0F, -0.5F);
        }
        TileNode tjf = new TileNode();
        tjf.setAspects(aspects);
        tjf.setNodeType(nodeType);
        tjf.blockType = ConfigBlocks.blockAiry;
        tjf.blockMetadata = 0;
        GL11.glPushMatrix();
        GL11.glTranslated(0.5D, 0.5D, 0.5D);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        renderItemNode(tjf);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        renderItemNode(tjf);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        renderItemNode(tjf);
        GL11.glPopMatrix();
        GL11.glEnable(32826);
    }


    public static void renderNodeItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        renderNodeItem(type, item, aspects, NodeType.NORMAL, null, data);
    }

    private static AspectList aspects = new AspectList().add(Aspect.AIR, 40).add(Aspect.FIRE, 40).add(Aspect.EARTH, 40).add(Aspect.WATER, 40);

    public static void renderItemNode(INode node) {
        if (node.getAspects().size() > 0) {
            EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
            float alpha = 0.5F;
            if (node.getNodeModifier() != null) {
                switch (node.getNodeModifier()) {
                    case BRIGHT:
                        alpha *= 1.5F;
                        break;
                    case PALE:
                        alpha *= 0.66F;
                        break;
                    case FADING:
                        alpha *= (MathHelper.sin(viewer.ticksExisted / 3.0F) * 0.25F + 0.33F);
                }
            }
            GL11.glPushMatrix();
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glDepthMask(false);
            GL11.glDisable(2884);
            long nt = System.nanoTime();
            long time = nt / 5000000L;
            float bscale = 0.25F;

            GL11.glPushMatrix();
            float rad = 6.283186F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);

            UtilsFX.bindTexture(TileNodeRenderer.nodetex);
            int frames = 32;
            int i = (int) ((nt / 40000000L + 1L) % frames);

            int count = 0;
            float scale = 0.0F;
            float average = 0.0F;
            for (Aspect aspect : node.getAspects().getAspects()) {
                if (aspect.getBlend() == 771) {
                    alpha = (float) (alpha * 1.5D);
                }
                average += node.getAspects().getAmount(aspect);
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, aspect.getBlend());
                scale = MathHelper.sin(viewer.ticksExisted / (14.0F - count)) * bscale + bscale * 2.0F;
                scale = 0.2F + scale * (node.getAspects().getAmount(aspect) / 50.0F);
                UtilsFX.renderAnimatedQuadStrip(scale, alpha / node.getAspects().size(), frames, 0, i, 0.0F, aspect.getColor());
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                count++;
                if (aspect.getBlend() == 771) {
                    alpha = (float) (alpha / 1.5D);
                }
            }
            average /= node.getAspects().size();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            i = (int) ((nt / 40000000L + 1L) % frames);
            scale = 0.1F + average / 150.0F;
            int strip = 1;
            switch (node.getNodeType()) {
                case NORMAL:
                    GL11.glBlendFunc(770, 1);
                    break;
                case UNSTABLE:
                    GL11.glBlendFunc(770, 1);
                    strip = 6;
                    break;
                case DARK:
                    GL11.glBlendFunc(770, 771);
                    strip = 2;
                    break;
                case TAINTED:
                    GL11.glBlendFunc(770, 771);
                    strip = 5;
                    break;
                case PURE:
                    GL11.glBlendFunc(770, 1);
                    strip = 4;
                    break;
                case HUNGRY:
                    scale *= 0.75F;
                    GL11.glBlendFunc(770, 1);
                    strip = 3;
            }
            GL11.glColor4f(1.0F, 0.0F, 1.0F, alpha);
            UtilsFX.renderAnimatedQuadStrip(scale, alpha, frames, strip, i, 0.0F, 16777215);

            GL11.glDisable(3042);
            GL11.glPopMatrix();

            GL11.glPopMatrix();

            GL11.glEnable(2884);
            GL11.glDepthMask(true);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        }
    }

}
