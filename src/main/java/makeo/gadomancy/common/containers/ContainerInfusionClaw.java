package makeo.gadomancy.common.containers;

import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 09.10.2015 20:34
 */
public class ContainerInfusionClaw extends Container {
    public final InventoryPlayer playerInv;
    public final IInventory clawInv;

    public ContainerInfusionClaw(InventoryPlayer playerInv, IInventory clawInv) {
        this.playerInv = playerInv;
        this.clawInv = clawInv;

        addSlotToContainer(new FilteredSlot(clawInv, 0, 80, 32));
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
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        if(((TileInfusionClaw)clawInv).isRunning()) {
            return null;
        }

        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == 0) {
                if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else {
                if(!((Slot)inventorySlots.get(0)).isItemValid(itemstack1)) {
                    return null;
                }

                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
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
    public boolean enchantItem(EntityPlayer player, int value) {
        ((TileInfusionClaw)clawInv).setIsLocked(value == 1);
        return false;
    }

    /*public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) this.inventorySlots.get(slot);

        if(slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();

            if (!mergeItemStack(stackInSlot, 0, 1, false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }
        }
        return stack;
    }*/

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return clawInv.isUseableByPlayer(player);
    }
}
