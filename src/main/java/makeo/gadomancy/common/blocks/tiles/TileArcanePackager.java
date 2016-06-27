package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 14.11.2015 12:22
 */
public class TileArcanePackager extends TileJarFillable implements IInventory, ISidedInventory {
    private static final Aspect ASPECT = Aspect.CLOTH;

    private ItemStack[] contents = new ItemStack[12];

    //0 - 46
    public byte progress = -1;
    public boolean autoStart = false;
    public boolean useEssentia = false;
    public boolean disguise = false;
    private Boolean redstoneState = null;

    private int count = 0;

    public TileArcanePackager() {
        aspectFilter = ASPECT;
        maxAmount = 8;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        contents = new ItemStack[getSizeInventory()];
        NBTTagList list = compound.getTagList("Items", 10);

        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound slot = list.getCompoundTagAt(i);
            int j = slot.getByte("Slot") & 255;

            if (j >= 0 && j < contents.length) {
                contents[j] = ItemStack.loadItemStackFromNBT(slot);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < contents.length; ++i) {
            if (contents[i] != null) {
                NBTTagCompound slot = new NBTTagCompound();
                slot.setByte("Slot", (byte) i);
                contents[i].writeToNBT(slot);
                list.appendTag(slot);
            }
        }
        compound.setTag("Items", list);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            if (progress >= 0 && progress < 46) {
                progress++;
            }
        } else {
            if (redstoneState == null) {
                redstoneState = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
            }

            if (progress >= 47) {
                doPack();
                progress = -1;
                markForUpdate();
                count = 1;
            }

            if (progress >= 0) {
                progress++;
            } else if (count++ % 5 == 0) {
                if (autoStart && canPack()) {
                    progress = 0;
                    markForUpdate();
                }
            }
        }
    }

    public void updateRedstone(boolean state) {
        if(redstoneState != null && state && !redstoneState) {
            if (canPack()) {
                progress = 0;
                markForUpdate();
            }
        }
        redstoneState = state;
    }

    private boolean canPack() {
        if (getStackInSlot(11) != null) {
            return false;
        }

        boolean check = false;
        for (int i = 0; i < 9; i++) {
            if (getStackInSlot(i) != null) {
                check = true;
                break;
            }
        }

        if (!check) {
            return false;
        }

        if (useEssentia) {
            if (amount < 4) {
                return false;
            }
        } else {
            ItemStack leather = getStackInSlot(9);
            if (leather == null || leather.stackSize < 1 || leather.getItem() != Items.leather) {
                return false;
            }

            ItemStack string = getStackInSlot(10);
            if (string == null || string.stackSize < 1 || string.getItem() != Items.string) {
                return false;
            }
        }

        return true;
    }

    private void doPack() {
        if (canPack()) {
            List<ItemStack> contents = new ArrayList<ItemStack>();
            for (int i = 0; i < 9; i++) {
                ItemStack stack = getStackInSlot(i);
                if (stack != null) {
                    contents.add(stack);
                }
            }

            ItemStack pack = new ItemStack(disguise ? RegisteredItems.itemFakeLootbag : RegisteredItems.itemPackage, 1, useEssentia ? 1 : 0);
            boolean success = RegisteredItems.itemPackage.setContents(pack, contents);

            if (success) {
                if (useEssentia) {
                    amount -= 4;
                } else {
                    decrStackSize(9, 1);
                    decrStackSize(10, 1);
                }
                setInventorySlotContents(11, pack);

                for (int i = 0; i < 9; i++) {
                    setInventorySlotContents(i, null);
                }
            } else {
                worldObj.newExplosion(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 1, false, false);

                for(int i = 0; i < 9; i++) {
                    ItemStack stack = this.contents[i];
                    if(stack != null) {
                        EntityItem entityItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + (13/16f), zCoord + 0.5, stack);
                        ItemUtils.applyRandomDropOffset(entityItem, worldObj.rand);
                        worldObj.spawnEntityInWorld(entityItem);
                        this.contents[i] = null;
                    }
                }

                progress = -1;
                markForUpdate();
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        aspectFilter = ASPECT;

        progress = compound.getByte("progress");

        byte settings = compound.getByte("settings");
        autoStart = (settings & 1) == 1;
        useEssentia = (settings & 2) == 2;
        disguise = (settings & 4) == 4;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.removeTag("AspectFilter");

        compound.setByte("progress", progress);

        byte settings = (byte) (autoStart ? 1 : 0);
        settings |= useEssentia ? 2 : 0;
        settings |= disguise ? 4 : 0;
        compound.setByte("settings", settings);
    }

    public void markForUpdate() {
        markDirty();
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

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
    public int getMinimumSuction() {
        return super.getMinimumSuction() * 2;
    }

    @Override
    public int getSuctionAmount(ForgeDirection loc) {
        return super.getSuctionAmount(loc) * 2;
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
        if (slot == 9) {
            return stack.getItem() == Items.leather;
        } else if (slot == 10) {
            return stack.getItem() == Items.string;
        } else if (slot == 11) {
            return false;
        }
        return true;
    }

    private static final int[] ORIENTATION_MAPPING = new int[]{-1, -1, 0, 2, 1, 3};

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0) {
            return new int[]{11};
        }

        if (ORIENTATION_MAPPING[side] == super.facing) {
            return new int[]{9, 10, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        }

        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return !(useEssentia && (slot == 9 || slot == 10));
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return true;
    }
}
