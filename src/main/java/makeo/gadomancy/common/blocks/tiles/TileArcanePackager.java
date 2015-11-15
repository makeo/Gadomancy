package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 14.11.2015 12:22
 */
public class TileArcanePackager extends SynchronizedTileEntity implements IInventory, ISidedInventory {
    private ItemStack[] contents = new ItemStack[12];

    //0 - 46
    public int progress = 20;
    public boolean autoStart = false;
    public boolean useEssentia = false;


    @Override
    public int getSizeInventory() {
        return 12;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return contents[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.contents[slot] != null) {
            ItemStack itemstack;

            if (this.contents[slot].stackSize <= amount) {
                itemstack = this.contents[slot];
                this.contents[slot] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.contents[slot].splitStack(amount);
                if (this.contents[slot].stackSize == 0) {
                    this.contents[slot] = null;
                }
                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.contents[slot] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "blub";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(slot == 9) {
            return stack.getItem() == Items.leather;
        } else if(slot == 10) {
            return stack.getItem() == Items.string;
        } else if(slot == 11) {
            return false;
        }
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return true;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return true;
    }
}
