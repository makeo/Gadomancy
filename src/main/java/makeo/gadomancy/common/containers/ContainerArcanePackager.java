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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new Slot(packagerInv, j + i * 3, 16 + 18 * j, 42 + i * 23));
            }
        }

        addSlotToContainer(new FilteredSlot(packagerInv, 9, 95, 33));
        addSlotToContainer(new FilteredSlot(packagerInv, 10, 113, 33));
        addSlotToContainer(new FilteredSlot(packagerInv, 11, 160, 64));

        bindPlayerInventory();
    }

    protected void bindPlayerInventory() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(this.playerInv, j + i * 9 + 9, 16 + j * 18, 151 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(this.playerInv, i, 16 + i * 18, 209));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
