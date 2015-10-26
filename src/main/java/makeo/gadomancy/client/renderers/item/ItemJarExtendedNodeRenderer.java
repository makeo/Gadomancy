package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.common.items.ItemExtendedNodeJar;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileJar;
import thaumcraft.common.tiles.TileJarNode;

/**
 * This class is NOT part of the Gadomancy Mod
 * This file is copied from Azanors thaumcraft.client.renderers.tile.ItemJarNodeRenderer.java and contains small modifications
 * Thaumcraft: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1292130
 *
 * Created by Azanor
 * Modified by HellFirePvP @ 26.10.2015 00:06
 */
public class ItemJarExtendedNodeRenderer implements IItemRenderer {

    //Copied and modified to allow compatibility to the ExtendedNodes

    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return (item != null) && (item.getItem() == RegisteredItems.itemExtendedNodeJar);
    }

    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        if (helper == IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK) {
            return false;
        }
        return true;
    }

    RenderBlocks rb = new RenderBlocks();

    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        if (item.getItem() == RegisteredItems.itemExtendedNodeJar) {
            if (type == IItemRenderer.ItemRenderType.ENTITY) {
                GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
            } else if ((type == IItemRenderer.ItemRenderType.EQUIPPED) && ((data[1] instanceof EntityPlayer))) {
                GL11.glTranslatef(0.0F, 0.0F, -0.5F);
            }
            TileJarNode tjf = new TileJarNode();
            if (item.hasTagCompound()) {
                AspectList aspects = ((ItemExtendedNodeJar) item.getItem()).getAspects(item);
                if (aspects != null) {
                    tjf.setAspects(aspects);
                    tjf.setNodeType(((ItemExtendedNodeJar) item.getItem()).getNodeType(item));
                    tjf.setNodeModifier(((ItemExtendedNodeJar) item.getItem()).getNodeModifier(item));
                }
            }
            tjf.blockType = ConfigBlocks.blockJar;
            tjf.blockMetadata = 2;
            GL11.glPushMatrix();
            TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileJar(), 0.0D, 0.0D, 0.0D, 0.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.5D, 0.4D, 0.5D);
            ItemNodeRenderer.renderItemNode(tjf);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            ItemNodeRenderer.renderItemNode(tjf);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            ItemNodeRenderer.renderItemNode(tjf);
            GL11.glPopMatrix();


            GL11.glPushMatrix();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.rb.useInventoryTint = true;
            this.rb.renderBlockAsItem(RegisteredBlocks.blockExtendedNodeJar, item.getItemDamage(), 1.0F);
            GL11.glPopMatrix();


            GL11.glEnable(32826);
        }
    }
}
