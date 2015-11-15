package makeo.gadomancy.client.gui;

import makeo.gadomancy.common.blocks.tiles.TileArcanePackager;
import makeo.gadomancy.common.containers.ContainerArcanePackager;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

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

    public ArcanePackagerGui(InventoryPlayer playerInv, IInventory inventory) {
        super(new ContainerArcanePackager(playerInv, inventory));

        this.tile = (TileArcanePackager) inventory;

        this.ySize = 234;
        this.xSize = 190;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        GL11.glEnable(GL11.GL_BLEND);

        int baseX = this.guiLeft;
        int baseY = this.guiTop;

        drawTexturedModalRect(baseX + 89, baseY + 67, 210, 0, tile.progress, 9);

        if(tile.autoStart) {
            drawTexturedModalRect(baseX + 89, baseY + 67, 210, 0, 8, 8);
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
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
    }
}
