package makeo.gadomancy.client.renderers.entity;

import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.entities.ModelGolem;
import thaumcraft.common.entities.golems.EntityGolemBase;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 07.10.2015 13:18
 */
public class RenderGolemHelper {
    private RenderGolemHelper() {}

    public static void renderCore(EntityGolemBase golem, AdditionalGolemCore core) {
        GL11.glPushMatrix();

        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0875F, -0.96F, 0.15F + (golem.getGolemDecoration().contains("P") ? 0.03F : 0.0F));
        GL11.glScaled(0.175D, 0.175D, 0.175D);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

        ItemStack coreItem = core.getItem();
        IIcon icon = coreItem.getItem().getIcon(coreItem, 0);
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMinU();
        float f4 = icon.getMaxV();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f3, f4, icon.getIconWidth(), icon.getIconHeight(), 0.2F);

        GL11.glPopMatrix();
    }

    public static boolean requiresRenderFix(EntityGolemBase golem) {
        ItemStack item = golem.getCarriedForDisplay();
        byte core = golem.getCore();
        return item != null && item.getItem().requiresMultipleRenderPasses() && core != 5 && core != 6 && core != 11;
    }

    public static void renderCarriedItemsFix(EntityGolemBase golem) {
        GL11.glPushMatrix();

        GL11.glScaled(0.4D, 0.4D, 0.4D);

        GL11.glTranslatef(-0.5F, 2.5F, -1.25F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

        ItemStack item = golem.getCarriedForDisplay();

        int renderPass = 0;
        do {
            IIcon icon = item.getItem().getIcon(item, renderPass);
            if (icon != null) {
                Color color = new Color(item.getItem().getColorFromItemStack(item, renderPass));
                GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());

                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(),
                        icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);

                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }
            renderPass++;
        } while (renderPass < item.getItem().getRenderPasses(item.getItemDamage()));

        GL11.glPopMatrix();
    }

    public static void renderToolItem(EntityGolemBase golem, ItemStack itemstack, ModelBase mainModel, RenderManager renderManager) {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        float angle = (float) (0.195 * Math.PI / -1 * ((ModelGolem) mainModel).golemRightArm.rotateAngleX);

        float y = (float) (Math.cos(angle) * 1.15);
        float z = (float) (Math.sin(angle) * 1.15);

        GL11.glTranslatef(-0.25F, y, z * -0.5f);

        GL11.glRotated(angle / Math.PI * 180f, -1, 0, 0);

        float fs = 0.66F;
        GL11.glScalef(fs, fs, fs);

        net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
        boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

        Item item = itemstack.getItem();
        float f1;

        if (item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())))
        {
            f1 = 0.5F;
            GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
            f1 *= 0.75F;
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
        }
        else if (item == Items.bow)
        {
            f1 = 0.625F;
            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(f1, -f1, f1);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        }
        else if (item.isFull3D())
        {
            f1 = 0.625F;

            if (item.shouldRotateAroundWhenRendering())
            {
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0.0F, -0.125F, 0.0F);
            }

            GL11.glTranslatef(0.0F, 0.1875F, 0.0F);

            GL11.glScalef(f1, -f1, f1);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            f1 = 0.375F;
            GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
            GL11.glScalef(f1, f1, f1);
            GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
        }

        float f2;
        int i;
        float f5;

        if (itemstack.getItem().requiresMultipleRenderPasses())
        {
            for (i = 0; i < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++i)
            {
                int j = itemstack.getItem().getColorFromItemStack(itemstack, i);
                f5 = (float)(j >> 16 & 255) / 255.0F;
                f2 = (float)(j >> 8 & 255) / 255.0F;
                float f3 = (float)(j & 255) / 255.0F;
                GL11.glColor4f(f5, f2, f3, 1.0F);

                renderManager.itemRenderer.renderItem(golem, itemstack, i);
            }
        }
        else
        {
            i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
            float f4 = (float)(i >> 16 & 255) / 255.0F;
            f5 = (float)(i >> 8 & 255) / 255.0F;
            f2 = (float)(i & 255) / 255.0F;
            GL11.glColor4f(f4, f5, f2, 1.0F);

            renderManager.itemRenderer.renderItem(golem, itemstack, 0);
        }


        GL11.glScaled(1.0D, 1.0D, 1.0D);

        GL11.glPopMatrix();
    }
}
