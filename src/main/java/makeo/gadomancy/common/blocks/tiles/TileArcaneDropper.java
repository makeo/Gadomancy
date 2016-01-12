package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.09.2015 20:13
 */
public class TileArcaneDropper extends TileEntity implements IInventory, ISidedInventory {
    private static final double RADIUS = 1.5;

    private List<EntityItem> items = new ArrayList<EntityItem>();

    private final List<EntityItem> dropQueue = new ArrayList<EntityItem>();

    private int count = 0;

    private int removedCount = 0;
    private EntityItem removedEntity = null;

    @Override
    public void updateEntity() {
        removedCount = 0;
        removedEntity = null;

        if(dropQueue.size() > 0) {
            for(int i = 0; i < dropQueue.size(); i++) {
                EntityItem item = dropQueue.get(i);
                if(!item.isDead && !worldObj.loadedEntityList.contains(item)) {
                    getWorldObj().spawnEntityInWorld(item);
                    dropQueue.remove(i);
                    i--;
                }
            }
        }

        if(count > 20) {
            count = 0;

            World world = getWorldObj();
            if(!world.isRemote) {
                int oldSize = items.size();
                updateInventory();
                if(oldSize != 0 || items.size() != 0) {
                    //Comparator...
                    world.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
                }
            }
        }
        count++;
    }

    private void updateInventory() {
        ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata() & 7);

        double x = this.xCoord + dir.offsetX*2 + 0.5;
        double y = this.yCoord + dir.offsetY*2 + 0.5;
        double z = this.zCoord + dir.offsetZ*2 + 0.5;

        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(RADIUS, RADIUS, RADIUS);
        items = getWorldObj().getEntitiesWithinAABB(EntityItem.class, box);

        //Add 10 empty slots
        for(int i = 0; i < 10; i++) {
            items.add(null);
        }
    }

    private EntityItem dropItem(ItemStack stack) {
        if(removedEntity != null && removedEntity.isDead) {
            for(EntityItem entityItem : items) {
                if(entityItem == removedEntity) {
                    if(InventoryUtils.areItemStacksEqualStrict(removedEntity.getEntityItem(), stack)
                            && removedEntity.getEntityItem().stackSize + removedCount == stack.stackSize) {
                        removedEntity.getEntityItem().stackSize += removedCount;
                        removedEntity.isDead = false;

                        removedCount = 0;
                        removedEntity = null;

                        return entityItem;
                    }
                }
            }
        }

        ForgeDirection side = ForgeDirection.getOrientation(getBlockMetadata() & 7);

        double x = this.xCoord + 0.5 + side.offsetX*0.5;
        double y = this.yCoord + 0.25 + side.offsetY*0.5;
        double z = this.zCoord + 0.5 + side.offsetZ*0.5;

        EntityItem entityItem = new EntityItem(getWorldObj(), x, y, z, stack.copy());//

        entityItem.motionX = side.offsetX*0.1;
        entityItem.motionY = side.offsetY*0.1;
        entityItem.motionZ = side.offsetZ*0.1;

        entityItem.delayBeforeCanPickup = 3;

        //prevent stackOverflows
        dropQueue.add(entityItem);

        return entityItem;
    }

    @Override
    public int getSizeInventory() {
        updateInventory();
        return items.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        updateInventory();

        if(slot >= items.size()) return null; //Get rekt. nothing here on this slot.

        EntityItem entity = items.get(slot);
        return entity == null || entity.isDead ? null : entity.getEntityItem();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = getStackInSlot(slot);

        if(stack.stackSize - amount <= 0) {
            setInventorySlotContents(slot, null);
            return stack.copy();
        }

        ItemStack newStack = stack.copy();
        newStack.stackSize = amount;
        stack.stackSize -= amount;

        removedEntity = items.get(slot);
        removedCount = amount;

        return newStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        EntityItem oldEntity = items.get(slot);

        if(oldEntity != null) {
            oldEntity.setDead();
        }

        if(stack != null && stack.stackSize > 0) {
            if(oldEntity != null && InventoryUtils.areItemStacksEqualStrict(oldEntity.getEntityItem(), stack)) {
                oldEntity.getEntityItem().stackSize = stack.stackSize;
                oldEntity.isDead = false;
            } else {
                items.set(slot, dropItem(stack));
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public String getInventoryName() {
        return RegisteredBlocks.blockArcaneDropper.getLocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        int[] slots = new int[getSizeInventory()];
        for(int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return RegisteredBlocks.blockArcaneDropper
                .isSideSolid(getWorldObj(), this.xCoord, this.yCoord, this.zCoord, ForgeDirection.getOrientation(side));
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if(canInsertItem(slot, stack, side)) {
            EntityItem entityItem = items.get(slot);
            return entityItem == null || entityItem.delayBeforeCanPickup <= 0;
        }
        return false;
    }
}
