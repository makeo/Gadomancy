package makeo.gadomancy.client.models;

import makeo.gadomancy.common.utils.Injector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.gear.ModelWand;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.lang.reflect.Method;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 19.10.2015 19:09
 */
public class ModelWandPart extends ModelWand {
    private static final Method DRAW_RUNE = Injector.getMethod("drawRune", ModelWand.class, double.class, double.class, double.class, int.class, EntityPlayer.class);

    private Injector injector;
    private int runeCount;

    public ModelWandPart(int height, int runeCount) {
        this.injector = new Injector(this, ModelWand.class);

        this.runeCount = runeCount;

        Injector injector = new Injector(this, ModelWand.class);

        ModelRenderer capBottom = injector.getField("CapBottom");
        capBottom.cubeList.clear();

        ModelRenderer rod = new ModelRenderer(this, 0, 8);
        rod.addBox(-1.0F, -1.0F, -1.0F, 2, height, 2);
        rod.setRotationPoint(0.0F, 2.0F, 0.0F);
        rod.setTextureSize(64, 32);
        rod.mirror = true;
        rod.rotateAngleX = 0;
        rod.rotateAngleY = 0;
        rod.rotateAngleZ = 0;
        injector.setField("Rod", rod);
    }

    @Override
    public void render(ItemStack stack) {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if(wand.hasRunes(stack)) {
            ItemStack stackCopy = stack.copy();
            stackCopy.func_150996_a(new ItemCustomWandCasting());

            super.render(stackCopy);

            GL11.glPushMatrix();
            if(wand.isStaff(stack)) {
                GL11.glTranslatef(0, 0.2f, 0);
            }

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 1);

            for (int side = 0; side < 4; side++) {
                GL11.glRotatef(90, 0, 1, 0);
                for (int a = 0; a < runeCount; a++) {
                    injector.invokeMethod(DRAW_RUNE, 0.36 + a * 0.14, -0.01, -0.08,
                            (a + side * 3) % 16, Minecraft.getMinecraft().thePlayer);
                }
            }

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glPopMatrix();
        } else {
            super.render(stack);
        }
    }

    private static class ItemCustomWandCasting extends ItemWandCasting {
        @Override
        public boolean hasRunes(ItemStack stack) {
            return false;
        }
    }
}
