package makeo.gadomancy.client.gui;

import makeo.gadomancy.common.containers.ContainerArcanePackager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 14.11.2015 01:17
 */
public class ArcanePackagerGui extends GuiContainer {
    public ArcanePackagerGui(InventoryPlayer playerInv, IInventory inventory) {
        super(new ContainerArcanePackager(playerInv, inventory));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float mouseX, int mouseZ, int par3) {

    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

    }
}
