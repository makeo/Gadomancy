package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 21.04.2016 / 10:47
 * on Gadomancy_1_8
 * TileArcaneDropper
 */
public class TileArcaneDropper extends TileEntity implements ISidedInventory, ITickable {

    private static final double RADIUS = 1.5;

    private List<EntityItem> items = new ArrayList<EntityItem>();

    private final List<EntityItem> dropQueue = new ArrayList<EntityItem>();

    private int count = 0;

    private int removedCount = 0;
    private EntityItem removedEntity = null;

    @Override
    public void update() {
        removedCount = 0;
        removedEntity = null;

        if(dropQueue.size() > 0) {
            for(int i = 0; i < dropQueue.size(); i++) {
                EntityItem item = dropQueue.get(i);
                if(!item.isDead && !worldObj.loadedEntityList.contains(item)) {
                    getWorld().spawnEntityInWorld(item);
                    dropQueue.remove(i);
                    i--;
                }
            }
        }

        if(count > 20) {
            count = 0;

            World world = getWorld();
            if(!world.isRemote) {
                int oldSize = items.size();
                updateInventory();
                if(oldSize != 0 || items.size() != 0) {
                    //Comparator...
                    world.updateComparatorOutputLevel(getPos(), getBlockType());
                }
            }
        }
        count++;
    }

    private void updateInventory() {
        EnumFacing side = EnumFacing.getFront(getBlockMetadata() & 7);

        double x = getPos().getX() + side.getFrontOffsetX() * 2 + 0.5;
        double y = getPos().getY() + side.getFrontOffsetY() * 2 + 0.5;
        double z = getPos().getZ() + side.getFrontOffsetZ() * 2 + 0.5;

        AxisAlignedBB box = AxisAlignedBB.fromBounds(x, y, z, x, y, z).expand(RADIUS, RADIUS, RADIUS);
        items = getWorld().getEntitiesWithinAABB(EntityItem.class, box);

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

        EnumFacing side = EnumFacing.getFront(getBlockMetadata() & 7);
        //ForgeDirection side = ForgeDirection.getOrientation(getBlockMetadata() & 7);

        double x = getPos().getX() + 0.5  + side.getFrontOffsetX() * 0.5;
        double y = getPos().getY() + 0.25 + side.getFrontOffsetY() * 0.5;
        double z = getPos().getZ() + 0.5  + side.getFrontOffsetZ() * 0.5;

        EntityItem entityItem = new EntityItem(getWorld(), x, y, z, stack.copy());//

        entityItem.motionX = side.getFrontOffsetX() * 0.1;
        entityItem.motionY = side.getFrontOffsetY() * 0.1;
        entityItem.motionZ = side.getFrontOffsetZ() * 0.1;

        entityItem.setPickupDelay(3);

        //prevent stackOverflows
        dropQueue.add(entityItem);

        return entityItem;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if(!canInsertAtSide(side)) return new int[0];

        int[] arr = new int[getSizeInventory()]; //Build array 0-size which is filled with the same id's.
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        return arr;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return canInsertAtSide(direction);
    }

    private boolean canInsertAtSide(EnumFacing facing) {
        return RegisteredBlocks.blockArcaneDropper.isSideSolid(getWorld(), getPos(), facing);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
        if(canInsertItem(slot, stack, direction)) {
            EntityItem entityItem = items.get(slot);
            return entityItem == null;/* || entityItemdelayBeforeCanPickup <= 0;*/ //TODO MAKE FCKING GETTER!
        }
        return false;
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
    public ItemStack removeStackFromSlot(int index) {
        return null;
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    //TODO new things?
    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return "inventory.arcaneDropper"; //TODO NO.
        //return RegisteredBlocks.blockArcaneDropper.getLocalizedName();
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(getName());
    }
}
