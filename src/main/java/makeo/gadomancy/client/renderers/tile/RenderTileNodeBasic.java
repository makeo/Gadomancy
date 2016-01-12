package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.common.events.EventHandlerRedirect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.tiles.TileJarNode;
import thaumcraft.common.tiles.TileNode;

/**
 * This class is NOT part of the Gadomancy Mod
 * This file is copied from Azanors thaumcraft.client.renderers.tile.TileNodeRenderer.java and contains small modifications
 * Thaumcraft: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1292130
 *
 * Created by Azanor
 * Modified by HellFirePvP @ 23.10.2015 20:04
 */
public class RenderTileNodeBasic {

    /*
    Changes overview:
    (Note: Changes are visible by "Gadomancy START" to "Gadomancy END" or by a comment in the end of the line!)

    - Where a node is rendered is now defined of the parameters of renderTileEntityAt() and no longer of the node-tile itself
    - Node rendering takes doubles as params and no longer int values.
    - Redirected renderTileEntityAt to a renderTileEntityAt where a size of the node can be specified

    Changes made by HellFirePvP
     */

    public static final ResourceLocation nodetex = new ResourceLocation("thaumcraft", "textures/misc/nodes.png");

    //Gadomancy: changed params x, y, z from int to double
    public static void renderNode(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, double x, double y, double z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        long nt = System.nanoTime();

        UtilsFX.bindTexture(nodetex);
        int frames = 32;
        if ((aspects.size() > 0) && (visible)) {
            double distance = viewer.getDistance(x, y, z); //Gadomancy: Removed '+ 0.5D' to coords.
            if (distance > viewDistance) {
                return;
            }
            float alpha = (float) ((viewDistance - distance) / viewDistance);
            if (mod != null) {
                switch (mod) {
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
            if (depthIgnore) {
                GL11.glDisable(2929);
            }
            GL11.glDisable(2884);

            long time = nt / 5000000L;
            float bscale = 0.25F;

            GL11.glPushMatrix();
            float rad = 6.283186F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);


            int i = (int) ((nt / 40000000L + x) % frames);

            int count = 0;
            float scale = 0.0F;
            float angle = 0.0F;
            float average = 0.0F;
            for (Aspect aspect : aspects.getAspects()) {
                if (aspect.getBlend() == 771) {
                    alpha = (float) (alpha * 1.5D);
                }
                average += aspects.getAmount(aspect);
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, aspect.getBlend());
                scale = MathHelper.sin(viewer.ticksExisted / (14.0F - count)) * bscale + bscale * 2.0F;
                scale = 0.2F + scale * (aspects.getAmount(aspect) / 50.0F);
                scale *= size;
                angle = (float) (time % (5000 + 500 * count)) / (5000.0F + 500 * count) * rad;
                //Gadomancy (following line): Removed '+ 0.5D' to coords.
                UtilsFX.renderFacingStrip(x, y, z, angle, scale, alpha / Math.max(1.0F, aspects.size() / 2.0F), frames, 0, i, partialTicks, aspect.getColor());
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                count++;
                if (aspect.getBlend() == 771) {
                    alpha = (float) (alpha / 1.5D);
                }
            }
            average /= aspects.size();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            i = (int) ((nt / 40000000L + x) % frames);
            scale = 0.1F + average / 150.0F;
            scale *= size;
            int strip = 1;
            switch (type) {
                case NORMAL:
                    GL11.glBlendFunc(770, 1);
                    break;
                case UNSTABLE:
                    GL11.glBlendFunc(770, 1);
                    strip = 6;
                    angle = 0.0F;
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
            //Gadomancy (following line): Removed '+ 0.5D' to coords.
            UtilsFX.renderFacingStrip(x, y, z, angle, scale, alpha, frames, strip, i, partialTicks, 16777215);
            GL11.glDisable(3042);
            GL11.glPopMatrix();

            GL11.glPopMatrix();

            GL11.glEnable(2884);
            if (depthIgnore) {
                GL11.glEnable(2929);
            }
            GL11.glDepthMask(true);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDepthMask(false);

            int i = (int) ((nt / 40000000L + x) % frames);
            GL11.glColor4f(1.0F, 0.0F, 1.0F, 0.1F);
            //Gadomancy (following line): Removed '+ 0.5D' to coords.
            UtilsFX.renderFacingStrip(x, y, z, 0.0F, 0.5F, 0.1F, frames, 1, i, partialTicks, 16777215);

            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        }
    }

    //Gadomancy START
    public static void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        renderTileEntityAt(tile, x, y, z, partialTicks, 1.0F);
    }
    //Gadomancy END

    //Gadomancy: Added dynamic size
    public static void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, float size) {
        //Gadomancy Added event hook
        EventHandlerRedirect.preNodeRender(tile);

        if (!(tile instanceof INode)) {
            //Gadomancy Added event hook
            EventHandlerRedirect.postNodeRender(tile);
            return;
        }
        INode node = (INode) tile;
        double viewDistance = 64.0D;
        EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
        boolean condition = false;
        boolean depthIgnore = false;
        if ((viewer instanceof EntityPlayer)) {
            if ((tile != null) && ((tile instanceof TileJarNode))) {
                condition = true;
                size = (float) ((size * 7D) / 10D); //Gadomancy: Node size is 70% of the default size in standart TC. calculating the same here.
            } else if ((((EntityPlayer) viewer).inventory.armorItemInSlot(3) != null) && ((((EntityPlayer) viewer).inventory.armorItemInSlot(3).getItem() instanceof IRevealer)) && (((IRevealer) ((EntityPlayer) viewer).inventory.armorItemInSlot(3).getItem()).showNodes(((EntityPlayer) viewer).inventory.armorItemInSlot(3), viewer))) {
                condition = true;
                depthIgnore = true;
            } else if ((((EntityPlayer) viewer).inventory.getCurrentItem() != null) && ((((EntityPlayer) viewer).inventory.getCurrentItem().getItem() instanceof ItemThaumometer))) {
                //Gadomancy: Changed from tile.xCoord, ... to x, y, z to make it dependent from params
                if (UtilsFX.isVisibleTo(0.44F, viewer, x, y, z)) {
                    condition = true;
                    depthIgnore = true;
                    viewDistance = 48.0D;
                }
            }
        }
        //Gadomancy: Changed from tile.xCoord, ... to x, y, z to make it dependent from params
        renderNode(viewer, viewDistance, condition, depthIgnore, size, x, y, z, partialTicks, ((INode) tile).getAspects(), ((INode) tile).getNodeType(), ((INode) tile).getNodeModifier());
        if (((tile instanceof TileNode)) && (((TileNode) tile).drainEntity != null) && (((TileNode) tile).drainCollision != null)) {
            Entity drainEntity = ((TileNode) tile).drainEntity;
            if (((drainEntity instanceof EntityPlayer)) && (!((EntityPlayer) drainEntity).isUsingItem())) {
                ((TileNode) tile).drainEntity = null;
                ((TileNode) tile).drainCollision = null;

                //Gadomancy Added event hook
                EventHandlerRedirect.postNodeRender(tile);
                return;
            }
            MovingObjectPosition drainCollision = ((TileNode) tile).drainCollision;
            GL11.glPushMatrix();

            float f10 = 0.0F;
            int iiud = ((EntityPlayer) drainEntity).getItemInUseDuration();
            if ((drainEntity instanceof EntityPlayer)) {
                f10 = MathHelper.sin(iiud / 10.0F) * 10.0F;
            }
            Vec3 vec3 = Vec3.createVectorHelper(-0.1D, -0.1D, 0.5D);
            vec3.rotateAroundX(-(drainEntity.prevRotationPitch + (drainEntity.rotationPitch - drainEntity.prevRotationPitch) * partialTicks) * 3.141593F / 180.0F);
            vec3.rotateAroundY(-(drainEntity.prevRotationYaw + (drainEntity.rotationYaw - drainEntity.prevRotationYaw) * partialTicks) * 3.141593F / 180.0F);
            vec3.rotateAroundY(-f10 * 0.01F);
            vec3.rotateAroundX(-f10 * 0.015F);
            double d3 = drainEntity.prevPosX + (drainEntity.posX - drainEntity.prevPosX) * partialTicks + vec3.xCoord;
            double d4 = drainEntity.prevPosY + (drainEntity.posY - drainEntity.prevPosY) * partialTicks + vec3.yCoord;
            double d5 = drainEntity.prevPosZ + (drainEntity.posZ - drainEntity.prevPosZ) * partialTicks + vec3.zCoord;
            double d6 = drainEntity == Minecraft.getMinecraft().thePlayer ? 0.0D : drainEntity.getEyeHeight();

            UtilsFX.drawFloatyLine(d3, d4 + d6, d5, drainCollision.blockX + 0.5D, drainCollision.blockY + 0.5D, drainCollision.blockZ + 0.5D, partialTicks, ((TileNode) tile).color.getRGB(), "textures/misc/wispy.png", -0.02F, Math.min(iiud, 10) / 10.0F);

            GL11.glPopMatrix();
        }

        //Gadomancy Added event hook
        EventHandlerRedirect.postNodeRender(tile);
    }

}
