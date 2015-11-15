package makeo.gadomancy.client.gui;

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
    private static final ResourceLocation BACKGROUND = new SimpleResourceLocation("gui/gui_packager.png");

    public ArcanePackagerGui(InventoryPlayer playerInv, IInventory inventory) {
        super(new ContainerArcanePackager(playerInv, inventory));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float mouseX, int mouseZ, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

    }
}
