package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.ItemUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.essentia.TileJarFillable;

import java.util.ArrayList;
import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 21.04.2016 / 11:07
 * on Gadomancy_1_8
 * TileArcanePackager
 */
public class TileArcanePackager extends TileJarFillable implements ISidedInventory, ITickable {

    private static final Aspect ASPECT = Aspect.ENERGY;

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
    public void update() {
        super.update();

        if (worldObj.isRemote) {
            if (progress >= 0 && progress < 46) {
                progress++;
            }
        } else {
            if (redstoneState == null) {
                //worldObj.isBlockIndirectlyGettingPowered(getPos());
                redstoneState = worldObj.isBlockPowered(getPos());
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
                BlockPos pos = getPos();
                int xCoord = pos.getX();
                int yCoord = pos.getY();
                int zCoord = pos.getZ();
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
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        redstoneState = compound.getBoolean("");

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

    public void markForUpdate() {
        markDirty();
        getWorld().markBlockForUpdate(getPos());
    }

    @Override
    public int getMinimumSuction() {
        return super.getMinimumSuction() * 2;
    }

    @Override
    public int getSuctionAmount(EnumFacing loc) {
        return super.getSuctionAmount(loc) * 2;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        int[] contents = new int[getSizeInventory()];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = i;
        }
        return contents;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStackIn, EnumFacing direction) {
        return !(useEssentia && (slot == 9 || slot == 10));
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
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
    public ItemStack decrStackSize(int slot, int count) {
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
    public ItemStack removeStackFromSlot(int index) {
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

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

    //TODO uhm..
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
        return "nothing";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(getName());
    }
}
