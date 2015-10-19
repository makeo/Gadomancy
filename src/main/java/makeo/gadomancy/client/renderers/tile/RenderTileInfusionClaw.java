package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.models.ModelInfusionClawPart;
import makeo.gadomancy.client.models.ModelWandPart;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.utils.Injector;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.entity.RenderPrimalOrb;
import thaumcraft.client.renderers.item.ItemWandRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.projectile.EntityPrimalOrb;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 05.10.2015 21:39
 */
public class RenderTileInfusionClaw extends TileEntitySpecialRenderer {
    private static final ResourceLocation RESOURCE = new SimpleResourceLocation("models/infusion_claw.png");
    private static final ModelInfusionClawPart[] MODELS;
    private static final Random RANDOM = new Random();
    private static final ItemWandRenderer WAND_RENDERER = new ItemWandRenderer();
    private static final ItemWandRenderer STAFF_RENDERER = new ItemWandRenderer();
    private static final RenderPrimalOrb RENDER_PRIMAL_ORB = new RenderPrimalOrb();

    static {
        MODELS = new ModelInfusionClawPart[8];
        for (int i = 0; i < MODELS.length; i++) {
            MODELS[i] = new ModelInfusionClawPart(i);
        }

        new Injector(WAND_RENDERER).setField("model", new ModelWandPart(7, 4));
        new Injector(STAFF_RENDERER).setField("model", new ModelWandPart(7, 4));

        RENDER_PRIMAL_ORB.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        render((TileInfusionClaw) tile, x, y, z, partialTicks);
    }

    private void render(TileInfusionClaw tile, double x, double y, double z, float partialTicks) {
        float ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;
        float elapsed = ticks - tile.lastRenderTick;
        tile.lastRenderTick = ticks;

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        renderPart(tile, elapsed, ticks);

        renderPrimalOrb(tile, elapsed);

        GL11.glPopMatrix();
    }

    private void renderPart(TileInfusionClaw tile, float elapsed, float ticks) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);

        World world = tile.getWorldObj();
        bindTexture(RESOURCE);

        renderSides(tile, world, tile.xCoord, tile.yCoord, tile.zCoord, elapsed);

        renderCircle(tile, elapsed);

        renderWand(tile.getStackInSlot(0), ticks);

        GL11.glPopMatrix();
    }

    private void renderCircle(TileInfusionClaw tile, float elapsed) {
        GL11.glPushMatrix();

        float speed = (float) Math.pow(1.1, Math.abs(tile.animationStates[9]));
        if (speed > 50) {
            speed = 50;
        }

        if (tile.animationStates[9] > 0) {
            tile.animationStates[9] += 0.5 * elapsed;

            if (tile.animationStates[9] > 20 * 5f) {
                tile.animationStates[9] = -tile.animationStates[9];

                ItemStack wandStack = tile.getStackInSlot(0);
                if(wandStack != null) {
                    tile.animationStates[11] = ((ItemWandCasting)wandStack.getItem()).isStaff(wandStack) ? 0.5f : 0.1f;
                }
            }
        } else if (tile.animationStates[9] < 0) {
            tile.animationStates[9] += elapsed * 0.5;

            if (tile.animationStates[9] > 0) {
                tile.animationStates[9] = 0;
            }
        }


        for (int i = 0; i < 4; i++) {
            GL11.glPushMatrix();

            tile.animationStates[10] += speed * (elapsed * 0.001 * Math.PI);

            float angle = (float) (tile.animationStates[10] + (i * 0.5 * Math.PI));
            float x = (float) Math.sin(angle);
            float y = (float) Math.cos(angle);
            float radius = 0.25f;

            GL11.glTranslated(0, (1f / 16f) * Math.sin(angle / 1.4) + (1f / 16f), 0);
            GL11.glTranslated(radius * x, -0.27, radius * y);
            GL11.glRotated(45, Math.cos(angle), 0, -Math.sin(angle));
            GL11.glScalef(0.75f, 0.75f, 0.75f);

            renderPart(i);

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private void renderWand(ItemStack wandStack, float ticks) {
        GL11.glRotatef(((ticks/3) / 20.0F) * (180F / (float)Math.PI), 0, 1f, 0);

        renderTopPart();

        if (wandStack != null && wandStack.stackSize > 0) {
            GL11.glPushMatrix();

            ItemWandCasting item = (ItemWandCasting) ConfigItems.itemWandCasting;
            ItemStack wandFocusStack = wandStack.copy();
            item.setFocus(wandFocusStack, new ItemStack(ConfigItems.itemFocusPrimal));

            GL11.glRotatef(180, 1, 0, 0);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GL11.glTranslatef(0, MathHelper.sin((ticks / 3) / 10.0F) * 0.08F + 0.08F, 0);

            GL11.glTranslatef(0, -1.4924f, 0);
            if(item.isStaff(wandFocusStack)) {
                GL11.glTranslatef(0, -0.5f, 0);

                STAFF_RENDERER.renderItem(IItemRenderer.ItemRenderType.ENTITY, wandFocusStack);
            } else {
                WAND_RENDERER.renderItem(IItemRenderer.ItemRenderType.ENTITY, wandFocusStack);
            }
            GL11.glPopMatrix();
        }
    }

    private void renderTopPart() {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, (0.5f - (1f / 16f * 0.75f)), 0);
        GL11.glScalef(0.75f, 0.75f, 0.75f);

        renderPart(4);

        GL11.glPopMatrix();
    }

    private void renderPrimalOrb(TileInfusionClaw tile, float elapsed) {
        if(tile.animationStates[11] > 0) {
            tile.animationStates[11] += elapsed / 70f;

            GL11.glPushMatrix();

            GL11.glTranslatef(0.5f, 0.75f - tile.animationStates[11], 0.5f);
            GL11.glScalef(0.35f, 0.35f, 0.35f);

            EntityPrimalOrb orb = new EntityPrimalOrb(tile.getWorldObj(), Minecraft.getMinecraft().thePlayer, true);
            orb.ticksExisted = Minecraft.getMinecraft().renderViewEntity.ticksExisted;

            RENDER_PRIMAL_ORB.renderEntityAt(orb, 0, 0, 0, 0, 0);

            GL11.glPopMatrix();

            if(0.75f - tile.animationStates[11] < -0.5f) {
                tile.animationStates[11] = 0;
            }
        }
    }

    private void createSideZap(TileInfusionClaw tile) {
        for(int i = 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            createZap(tile, 0.5f + 0.5f*(float)dir.offsetX, 0.6f, 0.5f + 0.5f*(float)dir.offsetZ, 0.5f + dir.offsetX*0.2f, 0.2f, 0.5f + dir.offsetZ*0.2f);
            playZapSound(tile.xCoord, tile.yCoord, tile.zCoord);
        }
    }

    private void createZap(TileEntity tile, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Thaumcraft.proxy.nodeBolt(Thaumcraft.proxy.getClientWorld(), tile.xCoord + startX, tile.yCoord + startY, tile.zCoord + startZ,
                tile.xCoord + endX, tile.yCoord + endY, tile.zCoord + endZ);
    }

    private void playZapSound(float x, float y, float z) {
        Thaumcraft.proxy.getClientWorld().playSound(x, y, z, "thaumcraft:zap", 0.1F, 1.0F + Thaumcraft.proxy.getClientWorld().rand.nextFloat() * 0.2F, false);
    }

    private void renderSides(TileInfusionClaw tile, World world, int x, int y, int z, float elapsed) {
        GL11.glPushMatrix();

        float speed = (float) Math.pow(1.1, Math.abs(tile.animationStates[8]));
        if (tile.animationStates[8] > 0) {
            tile.animationStates[8] += elapsed;

            if (tile.animationStates[8] > 20 * 2.5f) {
                tile.animationStates[8] = -tile.animationStates[8];
                tile.animationStates[9] = 1;

                createSideZap(tile);
            }
        } else if (tile.animationStates[8] < 0) {
            tile.animationStates[8] += elapsed * 0.5;

            if (tile.animationStates[8] > 0) {
                tile.animationStates[8] = 0;
            }
        }

        for (int i = 0; i < 4; i++) {
            float heightMove = getNextMoveOffset(tile, i, speed, elapsed);
            float widthMove = getNextMoveOffset(tile, i + 4, speed, elapsed);

            ForgeDirection dir = ForgeDirection.getOrientation(i + 2);
            boolean powered = world != null && world.getIndirectPowerLevelTo(x + dir.offsetX, y, z + dir.offsetZ, dir.ordinal()) > 0;

            GL11.glPushMatrix();

            GL11.glTranslatef(-dir.offsetZ * widthMove, heightMove, -dir.offsetX * widthMove);
            GL11.glTranslated((0.5 - (1f / 16f * 0.75)) * dir.offsetX, 0, (0.5 - (1f / 16f * 0.75)) * dir.offsetZ);
            GL11.glRotatef(90, -1 * dir.offsetZ, 0, dir.offsetX);

            GL11.glScalef(0.75f, 0.75f, 0.75f);

            renderRedstonePart(powered);

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private float getNextMoveOffset(TileInfusionClaw tile, int id, float speed, float elapsed) {
        tile.animationStates[id] += RANDOM.nextDouble() * 2 * elapsed * speed;
        return (float) ((1d / 16d) * Math.sin(tile.animationStates[id] / 30));
    }

    private void renderRedstonePart(boolean powered) {
        renderPart(powered ? 6 : 5);
    }

    private void renderPart(int num) {
        MODELS[num].render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f);
    }
}
