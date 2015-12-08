package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.fake.AdvancedFakePlayer;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusPrimal;
import thaumcraft.common.lib.research.ResearchManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 05.10.2015 22:32
 */
public class TileInfusionClaw extends SynchronizedTileEntity implements IInventory, ISidedInventory {
    private static final UUID FAKE_UUID = UUID.fromString("b23c8c3f-d7bd-49b3-970a-8e86728bab82");
    private static final Random RANDOM = new Random();

    private static final ItemWandCasting WAND_ITEM = (ItemWandCasting) ConfigItems.itemWandCasting;
    private static final ItemFocusPrimal WAND_FOCUS = (ItemFocusPrimal) ConfigItems.itemFocusPrimal;
    private static final AspectList MAX_WAND_COST = new AspectList().add(Aspect.WATER, 250).add(Aspect.AIR, 250).add(Aspect.EARTH, 250).add(Aspect.FIRE, 250).add(Aspect.ORDER, 250).add(Aspect.ENTROPY, 250);

    private ItemInWorldManager im = null;
    private Boolean redstoneState = null;

    private int count = 0;

    private String player = null;
    private ItemStack wandStack = null;
    private boolean isLocked = false;

    private int cooldown;

    @SideOnly(Side.CLIENT)
    public float lastRenderTick;

    /**
     * 0-3: heightMov sides
     * 4-7: widthMov sides
     * 8: sides exp. speed
     * 9: center exp. speed
     * 10: rotation center
     * 11: primal orb offset
     */
    @SideOnly(Side.CLIENT)
    public float[] animationStates;

    public TileInfusionClaw() {
        if(Gadomancy.proxy.getSide() == Side.CLIENT) {
            animationStates = new float[12];
            EntityLivingBase entity = Minecraft.getMinecraft().renderViewEntity;
            lastRenderTick = entity == null ? 0 : entity.ticksExisted;
        }
    }

    @Override
    public void updateEntity() {
        World world = getWorldObj();

        if(!world.isRemote) {
            if(redstoneState == null) {
                redstoneState = world.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
            }

            if(cooldown > 0) {
                cooldown--;
                if(cooldown == (int)(7.5f*20)) {
                    performClickBlock();
                }
            }

            if(count > 20) {
                count = 0;
                if(yCoord > 0) {
                    //Comparator...
                    world.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
                }
            }
            count++;
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        if(compound.hasKey("player")) {
            player = compound.getString("player");
        }
        wandStack = NBTHelper.getStack(compound, "wandStack");
        isLocked = compound.getBoolean("isLocked");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        if(hasOwner()) {
            compound.setString("player", player);
        }
        if(wandStack != null) {
            NBTHelper.setStack(compound, "wandStack", wandStack);
        }
        compound.setBoolean("isLocked", isLocked);
    }

    private void markForUpdate() {
        markDirty();
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void updateRedstone(boolean state) {
        if(redstoneState != null && state && !redstoneState) {
            startClickBlock();
        }
        redstoneState = state;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
        if(!getWorldObj().isRemote) {
            markForUpdate();
        }
    }

    public boolean setOwner(EntityPlayer player) {
        World world = getWorldObj();
        if(!world.isRemote && isValidOwner(player)) {
            this.player = player.getCommandSenderName();

            markForUpdate();

            return true;
        }
        return false;
    }

    public boolean isValidOwner(EntityPlayer player) {
        return !AdvancedFakePlayer.isFakePlayer(player);
    }

    private ArrayList<String> research = null;

    private void loadResearch(EntityPlayer fakePlayer) {
        boolean online = false;
        for(String username : MinecraftServer.getServer().getAllUsernames()) {
            if(username.equals(player)) {
                online = true;
                break;
            }
        }

        if(online) {
            this.research = ResearchManager.getResearchForPlayer(player);
        } else {
            if(research == null) {
                Thaumcraft.proxy.getCompletedResearch().put(fakePlayer.getCommandSenderName(), new ArrayList<String>());

                IPlayerFileData playerNBTManagerObj = MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler().getSaveHandler();
                SaveHandler sh = (SaveHandler)playerNBTManagerObj;
                File dir = ObfuscationReflectionHelper.getPrivateValue(SaveHandler.class, sh, "playersDirectory", "field_75771_c");
                File file1 = new File(dir, player + ".thaum");
                File file2 = new File(dir, player + ".thaumbak");
                ResearchManager.loadPlayerData(fakePlayer, file1, file2, false);

                this.research = ResearchManager.getResearchForPlayerSafe(fakePlayer.getCommandSenderName());
            }
        }

        Thaumcraft.proxy.getCompletedResearch().put(fakePlayer.getCommandSenderName(), research == null ? new ArrayList<String>() : research);
    }

    public String getOwner() {
        return player;
    }

    public boolean hasOwner() {
        return player != null;
    }

    private void startClickBlock() {
        if(!isRunning()) {
            ClickBehavior behavior = getClickBehavior(getWorldObj(), xCoord, yCoord-1, zCoord);
            if(behavior != null && (!behavior.hasVisCost() || hasSufficientVis())) {
                startRunning();
            }
        }
    }

    private void performClickBlock() {
        World world = getWorldObj();
        int x = xCoord;
        int y = yCoord - 1;
        int z = zCoord;

        ClickBehavior behavior = getClickBehavior(world, x, y, z);
        if(behavior != null) {
            AdvancedFakePlayer fakePlayer = new AdvancedFakePlayer((WorldServer) world, FAKE_UUID);
            loadResearch(fakePlayer);

            if(behavior.hasVisCost()) {
                if(hasSufficientVis()) {
                    consumeVis(fakePlayer);
                } else {
                    return;
                }
            }

            if(im == null) {
                im = new ItemInWorldManager(world);
            } else {
                im.setWorld((WorldServer) world);
            }

            fakePlayer.setHeldItem(wandStack);
            this.im.activateBlockOrUseItem(fakePlayer, world, wandStack, x, y, z, ForgeDirection.UP.ordinal(), 0.5F, 0.5F, 0.5F);
            addInstability(behavior);
        }
    }

    private ClickBehavior getClickBehavior(World world, int x, int y, int z) {
        if(y >= 0 && !world.isRemote && world instanceof WorldServer && hasOwner()
                && !world.isAirBlock(x, y, z) && wandStack != null && wandStack.stackSize > 0) {
            return RegisteredBlocks.getClawClickBehavior(world, x, y, z);
        }
        return null;
    }

    public boolean isRunning() {
        if(getWorldObj().isRemote) {
            return animationStates[8] + animationStates[9] + animationStates[11] != 0;
        }
        return cooldown > 0;
    }

    private void startRunning() {
        PacketHandler.INSTANCE.sendToAllAround(new PacketStartAnimation(PacketStartAnimation.ID_INFUSIONCLAW, xCoord, yCoord, zCoord),
                new NetworkRegistry.TargetPoint(getWorldObj().provider.dimensionId, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 48));
        cooldown = 22*20;
    }

    private void addInstability(ClickBehavior behavior) {
        int instability = 23;
        int maxVis = WAND_ITEM.getMaxVis(wandStack);

        instability += maxVis < 100 ? 3 : -2;

        instability -= Math.floor((maxVis > 300 ? 300 : maxVis) / 300f * 10f);
        instability -= WAND_ITEM.isStaff(wandStack) ? 6 : -1;

        instability += Math.floor((WAND_ITEM.getCap(wandStack).getBaseCostModifier() - 0.4) * 3);

        instability -= WAND_ITEM.getCap(wandStack) == ConfigItems.WAND_CAP_VOID ? 3 : 0;
        instability -= WAND_ITEM.getRod(wandStack) == ConfigItems.STAFF_ROD_PRIMAL ? 6 : 0;

        behavior.addInstability(instability);
    }

    private boolean hasSufficientVis() {
        return wandStack != null && wandStack.stackSize > 0 && WAND_ITEM.consumeAllVis(wandStack, null, MAX_WAND_COST, false, false);
    }

    private void consumeVis(EntityPlayer player) {
        WAND_ITEM.consumeAllVis(wandStack, player, WAND_FOCUS.getVisCost(wandStack), true, false);
        markForUpdate();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return wandStack;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(isRunning()) {
            return null;
        }

        if(amount > 0) {
            ItemStack result = wandStack.copy();
            wandStack = null;
            markForUpdate();
            return result;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        wandStack = stack;
        markForUpdate();
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
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
        if(isRunning()) {
            return false;
        }

        if(stack.getItem() == WAND_ITEM) {
            return !WAND_ITEM.isSceptre(stack) && WAND_ITEM.getFocus(stack) == null;
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return !isRunning() && side > 0 && side < 6;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return (!isLocked() || !hasSufficientVis()) && canInsertItem(slot, stack, side);
    }
}
