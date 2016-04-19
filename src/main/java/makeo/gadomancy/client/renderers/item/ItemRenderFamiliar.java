package makeo.gadomancy.client.renderers.item;

import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.common.items.baubles.ItemEtherealFamiliar;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.world.fake.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.entity.RenderWisp;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityWisp;

import java.awt.*;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 01.11.2015 12:55
 */
public class ItemRenderFamiliar implements IItemRenderer {

    private static EntityWisp ENTITY_WISP = null;
    private float[] renderInfo = new float[5];

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return !(item == null || !(item.getItem() instanceof ItemEtherealFamiliar));
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.ENTITY_ROTATION;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(item == null || !(item.getItem() instanceof ItemEtherealFamiliar)) return;
        GL11.glPushMatrix();
        if(type == ItemRenderType.EQUIPPED) {
            GL11.glTranslatef(0.5F, 0.5F, 0.7F);
        } else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0, 1F, 0.8F);
        } else if(type == ItemRenderType.INVENTORY) {
            GL11.glTranslatef(0, -0.45F, 0);
        }

        try {
            cleanActiveRenderInfo(type);
            GL11.glScalef(1.3F, 1.3F, 1.3F);
            if(ItemEtherealFamiliar.hasFamiliarAspect(item)) {
                if(ENTITY_WISP == null) ENTITY_WISP = new EntityWisp(new FakeWorld());
                ENTITY_WISP.ticksExisted = FamiliarHandlerClient.PartialEntityFamiliar.DUMMY_FAMILIAR.ticksExisted;
                ENTITY_WISP.setType(ItemEtherealFamiliar.getFamiliarAspect(item).getTag());
                renderEntityWispFor(null, ENTITY_WISP, 0, 0, 0, 0, 0);
            }
        } finally {
            restoreActiveRenderInfo();
        }
        GL11.glPopMatrix();
    }

    private void cleanActiveRenderInfo(ItemRenderType renderType) {
        renderInfo[0] = ActiveRenderInfo.rotationX;
        renderInfo[1] = ActiveRenderInfo.rotationXZ;
        renderInfo[2] = ActiveRenderInfo.rotationZ;
        renderInfo[3] = ActiveRenderInfo.rotationYZ;
        renderInfo[4] = ActiveRenderInfo.rotationXY;
        switch (renderType) {
            case ENTITY:
                break;
            case EQUIPPED:
                ActiveRenderInfo.rotationX = 0.85535365F;
                ActiveRenderInfo.rotationXZ = 0.9868404F;
                ActiveRenderInfo.rotationZ = -0.51804453F;
                ActiveRenderInfo.rotationYZ = 0.083717324F;
                ActiveRenderInfo.rotationXY = 0.13822734F;
                break;
            case EQUIPPED_FIRST_PERSON:
                ActiveRenderInfo.rotationX = -0.4186075F;
                ActiveRenderInfo.rotationXZ = 0.99932945F;
                ActiveRenderInfo.rotationZ = -0.90816724F;
                ActiveRenderInfo.rotationYZ = 0.033253096F;
                ActiveRenderInfo.rotationXY = -0.015327567F;
                break;
            case INVENTORY:
                ActiveRenderInfo.rotationX = -0.71445745F;
                ActiveRenderInfo.rotationXZ = 0.9573291F;
                ActiveRenderInfo.rotationZ = 0.69967884F;
                ActiveRenderInfo.rotationYZ = -0.20220716F;
                ActiveRenderInfo.rotationXY = -0.20647818F;
                break;
            case FIRST_PERSON_MAP:
                break;
        }
    }

    private void restoreActiveRenderInfo() {
        if(renderInfo[0] != -1) ActiveRenderInfo.rotationX = renderInfo[0];
        if(renderInfo[1] != -1) ActiveRenderInfo.rotationXZ = renderInfo[1];
        if(renderInfo[2] != -1) ActiveRenderInfo.rotationZ = renderInfo[2];
        if(renderInfo[3] != -1) ActiveRenderInfo.rotationYZ = renderInfo[3];
        if(renderInfo[4] != -1) ActiveRenderInfo.rotationXY = renderInfo[4];
        for (int i = 0; i < renderInfo.length; i++) {
            renderInfo[i] = -1;
        }
    }

    private static int size1 = 0;
    public static void renderEntityWispFor(EntityPlayer owner, EntityWisp wisp, double x, double y, double z, float fq, float pticks) {
        if (size1 == 0) {
            size1 = UtilsFX.getTextureSize("textures/misc/wisp.png", 64);
        }

        float f1 = ActiveRenderInfo.rotationX;
        float f2 = ActiveRenderInfo.rotationXZ;
        float f3 = ActiveRenderInfo.rotationZ;
        float f4 = ActiveRenderInfo.rotationYZ;
        float f5 = ActiveRenderInfo.rotationXY;
        float f10 = 1.0F;
        float f11 = (float)x;
        float f12 = (float)y + 0.45F;
        float f13 = (float)z;

        Tessellator tessellator = Tessellator.instance;

        boolean priv = owner != null && MiscUtils.isPrivilegedUser(owner);

        Color color = new Color(0);
        if(priv) {
            color = new Color(Color.HSBtoRGB((System.currentTimeMillis() % 3240) / 3240F, 1F, 1F));
            if(owner.worldObj.rand.nextInt(3) == 0) {
                EntityPlayer thisPlayer = Minecraft.getMinecraft().thePlayer;
                boolean isThisOwner = thisPlayer.equals(owner);
                World world = owner.worldObj;
                Random rand = world.rand;
                double oX, oY, oZ;
                if(isThisOwner) {
                    oX = owner.posX + f11;
                    oY = owner.posY + f12;
                    oZ = owner.posZ + f13;
                } else {
                    oX = thisPlayer.posX + (f11);
                    oY = thisPlayer.posY + (f12);
                    oZ = thisPlayer.posZ + (f13);
                }
                Thaumcraft.proxy.wispFX(world, oX + (rand.nextFloat() - rand.nextFloat()) * 0.3F,
                                               oY + (rand.nextFloat() - rand.nextFloat()) * 0.3F,
                                               oZ + (rand.nextFloat() - rand.nextFloat()) * 0.3F,
                        0.1F, color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
            }
        } else {
            if (Aspect.getAspect(wisp.getType()) != null) {
                color = new Color(Aspect.getAspect(wisp.getType()).getColor());
            }
        }

        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);

        UtilsFX.bindTexture("textures/misc/wisp.png");

        int i = wisp.ticksExisted % 16;

        float size4 = size1 * 4;
        float float_sizeMinus0_01 = size1 - 0.01F;

        float x0 = (i % 4 * size1 + 0.0F) / size4;
        float x1 = (i % 4 * size1 + float_sizeMinus0_01) / size4;
        float x2 = (i / 4 * size1 + 0.0F) / size4;
        float x3 = (i / 4 * size1 + float_sizeMinus0_01) / size4;

        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);
        tessellator.setColorRGBA_F(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
        tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, x1, x3);
        tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, x1, x2);
        tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, x0, x2);
        tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, x0, x3);
        tessellator.draw();

        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glAlphaFunc(516, 0.003921569F);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);

        UtilsFX.bindTexture(ParticleEngine.particleTexture);

        int qq = wisp.ticksExisted % 16;


        float size8 = 16.0F;
        x0 = qq / size8;
        x1 = (qq + 1) / size8;
        x2 = 5.0F / size8;
        x3 = 6.0F / size8;

        float var11 = MathHelper.sin((wisp.ticksExisted + pticks) / 10.0F) * 0.1F;

        f10 = 0.4F + var11;

        tessellator.startDrawingQuads();
        tessellator.setBrightness(240);

        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, x1, x3);
        tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, x1, x2);
        tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, x0, x2);
        tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, x0, x3);
        tessellator.draw();

        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glPopMatrix();
    }

}
