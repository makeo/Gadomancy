package makeo.gadomancy.common.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 10.10.2015 13:49
 */
public class FilteredSlot extends Slot {
    public FilteredSlot(IInventory inventory, int slotNumber, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotNumber, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return inventory.isItemValidForSlot(slotNumber, stack);
    }
}
