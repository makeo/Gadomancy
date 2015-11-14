package makeo.gadomancy.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 14.11.2015 18:08
 */
public class ContainerArcanePackager extends Container {
    public final InventoryPlayer playerInv;
    public final IInventory packagerInv;

    public ContainerArcanePackager(InventoryPlayer playerInv, IInventory packagerInv) {
        this.playerInv = playerInv;
        this.packagerInv = packagerInv;

        addSlotToContainer(new FilteredSlot(packagerInv, 0, 80, 32));
        bindPlayerInventory();
    }

    protected void bindPlayerInventory() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(this.playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(this.playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
