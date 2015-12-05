package makeo.gadomancy.client.gui;

import makeo.gadomancy.common.blocks.tiles.TileArcanePackager;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.containers.ContainerArcanePackager;
import makeo.gadomancy.common.utils.ColorHelper;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 14.11.2015 01:17
 */
public class ArcanePackagerGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new SimpleResourceLocation("gui/gui_packager.png");

    private final TileArcanePackager tile;
    private final InventoryPlayer playerInv;

    public ArcanePackagerGui(InventoryPlayer playerInv, IInventory inventory) {
        super(new ContainerArcanePackager(playerInv, inventory));

        this.tile = (TileArcanePackager) inventory;
        this.playerInv = playerInv;

        this.ySize = 234;
        this.xSize = 190;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        if(tile.isInvalid()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        this.mc.renderEngine.bindTexture(TEXTURE);

        GL11.glColor3f(1, 1, 1);

        if(tile.progress > 0) {
            drawTexturedModalRect(89, 67, 210, 0, tile.progress, 9);
        }

        if(tile.useEssentia) {
            drawTexturedModalRect(91, 97, 249, 10, 8, 8);
        }

        if(tile.autoStart) {
            drawTexturedModalRect(91, 110, 249, 10, 8, 8);
        }

        if(tile.disguise) {
            drawTexturedModalRect(91, 123, 249, 10, 8, 8);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float mouseX, int mouseZ, int par3) {
        this.mc.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        drawString(100, 98, "gadomancy.info.ArcanePackager.useEssentia", tile.useEssentia ? Color.WHITE : Color.GRAY);

        drawString(100, 111, "gadomancy.info.ArcanePackager.autoStart", tile.autoStart ? Color.WHITE : Color.GRAY);

        drawString(100, 124, "gadomancy.info.ArcanePackager.disguisePackage", tile.disguise ? Color.WHITE : Color.GRAY);

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawString(int x, int y, String unlocalized, Color color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.guiLeft + x, this.guiTop + y, 0);
        GL11.glScalef(0.5F, 0.5F, 0.0F);
        this.fontRendererObj.drawString(StatCollector.translateToLocal(unlocalized), 0, 0, ColorHelper.toHex(color));
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);

        int baseX = (this.width - this.xSize) / 2;
        int baseY = (this.height - this.ySize) / 2;

        int checkX = x - baseX - 90;
        int checkY = y - baseY - 97;

        if(checkX >= 0 && checkX < 8 && checkY >= 0 && checkY < 8) {
            tile.useEssentia = !tile.useEssentia;
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, tile.useEssentia ? 0 : 1);
        }

        checkY = y - baseY - 110;

        if(checkX >= 0 && checkX < 8 && checkY >= 0 && checkY < 8) {
            tile.autoStart = !tile.autoStart;
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, tile.autoStart ? 2 : 3);
        }

        checkY = y - baseY - 123;

        if(checkX >= 0 && checkX < 8 && checkY >= 0 && checkY < 8) {
            tile.disguise = !tile.disguise;
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, tile.disguise ? 4 : 5);
        }
    }


}
