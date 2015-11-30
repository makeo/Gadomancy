package makeo.gadomancy.common.containers;

import makeo.gadomancy.common.blocks.tiles.TileArcanePackager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

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
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int size = packagerInv.getSizeInventory();
            if (slotIndex < size) {
                if (!this.mergeItemStack(itemstack1, size, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else {
                int specialSlot = itemstack1.getItem() == Items.leather ? 9 : itemstack1.getItem() == Items.string ? 10 : -1;
                if(specialSlot != -1 && (packagerInv.getStackInSlot(specialSlot) == null || packagerInv.getStackInSlot(specialSlot).stackSize < 64)) {
                    this.mergeItemStack(itemstack1, specialSlot, specialSlot + 1, false);

                    if(itemstack1.stackSize == 0) {
                        slot.putStack(null);
                    }

                    return null;
                } else if (!this.mergeItemStack(itemstack1, 0, size - 3, false)) {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int id) {
        TileArcanePackager tile = (TileArcanePackager) packagerInv;

        switch (id) {
            case 0:
                tile.useEssentia = true;
                break;
            case 1:
                tile.useEssentia = false;
                break;
            case 2:
                tile.autoStart = true;
                break;
            case 3:
                tile.autoStart = false;
                break;
            case 4:
                tile.disguise = true;
                break;
            case 5:
                tile.disguise = false;
                break;
        }

        tile.markForUpdate();

        return false;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
