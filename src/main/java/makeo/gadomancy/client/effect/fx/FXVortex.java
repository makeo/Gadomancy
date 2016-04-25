package makeo.gadomancy.client.effect.fx;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.events.ClientHandler;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.QuadHelper;
import thaumcraft.client.lib.UtilsFX;

import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 24.04.2016 / 00:45
 * on Gadomancy
 * FXVortex
 */
public class FXVortex {

    public static final ResourceLocation TC_VORTEX_TEXTURE = new ResourceLocation("thaumcraft", "textures/misc/vortex.png");
    private static final float RAD = (float) (Math.PI * 2);

    private long lastUpdateCall;
    private double x, y, z;
    public boolean registered = false;

    public FXVortex(double x, double y, double z) {
        this.lastUpdateCall = System.currentTimeMillis();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void render(Tessellator tessellator, float pTicks) {
        float arX = ActiveRenderInfo.rotationX;
        float arXZ = ActiveRenderInfo.rotationXZ;
        float arZ = ActiveRenderInfo.rotationZ;
        float arYZ = ActiveRenderInfo.rotationYZ;
        float arXY = ActiveRenderInfo.rotationXY;

        GL11.glPushMatrix();
        //GL11.glDepthMask(true);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float agescale = (float) (ClientHandler.ticks % 800) / 400F;
        if(agescale >= 1.0F) agescale = 2 - agescale;
        float size = 0.2F + 0.1F * agescale;

        float anglePerc = (float) (ClientHandler.ticks % 300) / 300F;
        float angle = RAD - RAD * anglePerc;

        Vector3 iV = MiscUtils.interpolateEntityPosition(Minecraft.getMinecraft().renderViewEntity, pTicks);

        GL11.glTranslated(-iV.getX(), -iV.getY(), -iV.getZ());

        UtilsFX.bindTexture(TC_VORTEX_TEXTURE);

        tessellator.startDrawingQuads();
        tessellator.setBrightness(220);
        tessellator.setColorRGBA_F(1F, 1F, 1F, 1F);

        Vec3 v1 = Vec3.createVectorHelper(-arX * size - arYZ * size, -arXZ * size, -arZ * size - arXY * size);
        Vec3 v2 = Vec3.createVectorHelper(-arX * size + arYZ * size, arXZ * size, -arZ * size + arXY * size);
        Vec3 v3 = Vec3.createVectorHelper(arX * size + arYZ * size, arXZ * size, arZ * size + arXY * size);
        Vec3 v4 = Vec3.createVectorHelper(arX * size - arYZ * size, -arXZ * size, arZ * size - arXY * size);
        if (angle != 0.0F) {
            Vec3 pvec = Vec3.createVectorHelper(iV.getX(), iV.getY(), iV.getZ());
            Vec3 tvec = Vec3.createVectorHelper(x, y, z);
            Vec3 qvec = pvec.subtract(tvec).normalize();
            QuadHelper.setAxis(qvec, angle).rotate(v1);
            QuadHelper.setAxis(qvec, angle).rotate(v2);
            QuadHelper.setAxis(qvec, angle).rotate(v3);
            QuadHelper.setAxis(qvec, angle).rotate(v4);
        }
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(x + v1.xCoord, y + v1.yCoord, z + v1.zCoord, 0, 1);
        tessellator.addVertexWithUV(x + v2.xCoord, y + v2.yCoord, z + v2.zCoord, 1, 1);
        tessellator.addVertexWithUV(x + v3.xCoord, y + v3.yCoord, z + v3.zCoord, 1, 0);
        tessellator.addVertexWithUV(x + v4.xCoord, y + v4.yCoord, z + v4.zCoord, 0, 0);
        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        //GL11.glDepthMask(false);
        GL11.glPopMatrix();
    }

    public static void sheduleRender(List<FXVortex> vortexes, Tessellator tessellator, float partialTicks) {
        synchronized (EffectHandler.lockEffects) {
            for (FXVortex vortex : vortexes) {
                GL11.glPushMatrix();
                vortex.render(tessellator, partialTicks);
                GL11.glPopMatrix();
            }
        }
    }

    public static void tickVortexes(List<FXVortex> vortexes) {
        synchronized (EffectHandler.lockEffects) {
            for(FXVortex vortex : vortexes) {
                if((System.currentTimeMillis() - vortex.lastUpdateCall) > 100L) {
                    //System.out.println("tickTimeout");
                    EffectHandler.getInstance().unregisterVortex(vortex);
                }
            }
        }
    }

    public void notify(long timeMillis) {
        this.lastUpdateCall = timeMillis;
    }

}
