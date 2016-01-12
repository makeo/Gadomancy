package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.entities.fake.AdvancedFakePlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 16.12.2015 11:50
 */
public class TileArcaneHand extends SynchronizedTileEntity implements IInventory, ISidedInventory {
    private GuiFakePlayer fakePlayer;

    private ItemInWorldManager im = null;
    private Container container = null;
    private List<Slot> slots = null;

    @Override
    public void updateEntity() {
        if(im == null) {
            updateConnection();
        }
    }

    public void updateConnection() {
        container = null;
        slots = null;

        if (im == null) {
            im = new ItemInWorldManager(worldObj);
        } else {
            im.setWorld((WorldServer) worldObj);
        }

        TileEntity tile = getWorldObj().getTileEntity(xCoord, yCoord - 1, zCoord);
        if (tile != null && worldObj instanceof WorldServer) {
            if(fakePlayer == null) {
                fakePlayer = new GuiFakePlayer(this);
            }

            fakePlayer.posX = xCoord;
            fakePlayer.posY = yCoord;
            fakePlayer.posZ = zCoord;

            this.im.activateBlockOrUseItem(fakePlayer, worldObj, null, xCoord, yCoord - 1, zCoord, ForgeDirection.UP.ordinal(), 0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if(container == null) {
            return new int[0];
        }

        int[] slots = new int[this.slots.size()];
        for(int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slots.get(slot).isItemValid(stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slots.get(slot).canTakeStack(fakePlayer);
    }

    private static class GuiFakePlayer extends AdvancedFakePlayer {
        private TileArcaneHand tile;

        public GuiFakePlayer(TileArcaneHand tile) {
            super((WorldServer) tile.worldObj, UUID.randomUUID(), "ArcaneHand");
            this.tile = tile;
        }

        public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
            ModContainer mc = FMLCommonHandler.instance().findContainerFor(mod);
            tile.container = NetworkRegistry.INSTANCE.getRemoteGuiContainer(mc, this, modGuiId, world, x, y, z);
            if (tile.container != null) {
                tile.slots = new ArrayList<Slot>();
                for (Slot slot : (List<Slot>) tile.container.inventorySlots) {
                    if(!inventory.equals(slot.inventory)) {
                        tile.slots.add(slot);
                    }
                }
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
    }

    @Override
    public int getSizeInventory() {
        return slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slots.get(slot).getStack();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        Slot s = slots.get(slot);

        if (s.getStack() != null) {
            ItemStack itemstack;
            if (s.getStack().stackSize <= amount) {
                itemstack = s.getStack();
                s.putStack(null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = s.getStack().splitStack(amount);

                if (s.getStack().stackSize == 0) {
                    s.putStack(null);
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        slots.get(slot).putStack(stack);
    }

    @Override
    public String getInventoryName() {
        return "";
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
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
