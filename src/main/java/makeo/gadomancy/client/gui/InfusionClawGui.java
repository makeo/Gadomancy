package makeo.gadomancy.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.containers.ContainerInfusionClaw;
import makeo.gadomancy.common.utils.ColorHelper;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 09.10.2015 20:21
 */
@SideOnly(Side.CLIENT)
public class InfusionClawGui extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new SimpleResourceLocation("/gui/gui_infusion_claw.png");

    private ContainerInfusionClaw container;

    public InfusionClawGui(InventoryPlayer playerInv, IInventory clawInv) {
        super(new ContainerInfusionClaw(playerInv, clawInv));
        container = (ContainerInfusionClaw) super.inventorySlots;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        if(((TileInfusionClaw)container.clawInv).isInvalid()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }

        ItemStack cursorStack = container.playerInv.getItemStack();
        if(((TileInfusionClaw)container.clawInv).isRunning() || (cursorStack != null && cursorStack.stackSize > 0 && !container.clawInv.isItemValidForSlot(0, cursorStack))) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1, 1, 1, 0.5f);

            this.mc.renderEngine.bindTexture(BACKGROUND);
            float t = this.zLevel;
            this.zLevel = 200.0F;
            GL11.glEnable(3042);
            drawTexturedModalRect(80, 32, 240, 0, 16, 16);
            GL11.glDisable(3042);
            this.zLevel = t;

            GL11.glPopAttrib();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glEnable(GL11.GL_BLEND);

        this.mc.renderEngine.bindTexture(BACKGROUND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        int baseX = this.guiLeft;
        int baseY = this.guiTop;

        drawTexturedModalRect(baseX + 114, baseY + 36, 244, 19, 8, 8);

        boolean isLocked = ((TileInfusionClaw)container.clawInv).isLocked();
        if(isLocked) {
            drawTexturedModalRect(baseX + 114, baseY + 36, 244, 27, 8, 8);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(baseX + 124, baseY + 38, 0);
        GL11.glScalef(0.5F, 0.5F, 0.0F);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("gadomancy.info.InfusionClaw.lock"), 0, 0, ColorHelper.toHex(isLocked ? Color.WHITE : Color.GRAY));
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void mouseClicked(int x, int y, int par3) {
        super.mouseClicked(x, y, par3);

        int baseX = (this.width - this.xSize) / 2;
        int baseY = (this.height - this.ySize) / 2;

        int checkX = x - baseX - 114;
        int checkY = y - baseY - 36;

        if(checkX >= 0 && checkX < 8 && checkY >= 0 && checkY < 8) {
            TileInfusionClaw tile = ((TileInfusionClaw)container.clawInv);
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, tile.isLocked() ? 0 : 1);
            tile.setIsLocked(!tile.isLocked());
        }
    }
}
