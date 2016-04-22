package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.fake.AdvancedFakePlayer;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWand;
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
public class TileInfusionClaw extends SynchronizedTileEntity implements IInventory, ISidedInventory, ITickable {

    private static final UUID FAKE_UUID = UUID.fromString("b23c8c3f-d7bd-49b3-970a-8e86728bab82");
    private static final Random RANDOM = new Random();

    private static final ItemWand WAND_ITEM = (ItemWand) ItemsTC.wand;
    private static final ItemFocusPrimal WAND_FOCUS = (ItemFocusPrimal) ItemsTC.focusPrimal;
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
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            lastRenderTick = entity == null ? 0 : entity.ticksExisted;
        }
    }

    @Override
    public void update() {
        World world = getWorld();

        if(!world.isRemote) {
            if(redstoneState == null) {
                redstoneState = world.isBlockPowered(getPos());
                //redstoneState = world.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
            }

            if(cooldown > 0) {
                cooldown--;
                if(cooldown == (int)(7.5f*20)) {
                    performClickBlock();
                }
            }

            if(count > 20) {
                count = 0;
                if(getPos().getY() > 0) {
                    //Comparator...
                    world.updateComparatorOutputLevel(getPos(), getBlockType());
                    //world.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
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
        getWorld().markBlockForUpdate(getPos());
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
        if(!getWorld().isRemote) {
            markForUpdate();
        }
    }

    public boolean setOwner(EntityPlayer player) {
        World world = getWorld();
        if(!world.isRemote && isValidOwner(player)) {
            this.player = player.getName();

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
                Thaumcraft.proxy.getCompletedResearch().put(fakePlayer.getName(), new ArrayList<String>());

                IPlayerFileData playerNBTManagerObj = MinecraftServer.getServer().worldServerForDimension(0).getSaveHandler().getPlayerNBTManager();
                SaveHandler sh = (SaveHandler)playerNBTManagerObj;
                File dir = ObfuscationReflectionHelper.getPrivateValue(SaveHandler.class, sh, "playersDirectory", "field_75771_c");
                File file1 = new File(dir, player + ".thaum");
                File file2 = new File(dir, player + ".thaumbak");
                ResearchManager.loadPlayerData(fakePlayer.getName(), file1, file2);

                this.research = ResearchManager.getResearchForPlayerSafe(fakePlayer.getName());
            }
        }

        Thaumcraft.proxy.getCompletedResearch().put(fakePlayer.getName(), research == null ? new ArrayList<String>() : research);
    }

    public String getOwner() {
        return player;
    }

    public boolean hasOwner() {
        return player != null;
    }

    private void startClickBlock() {
        if(!isRunning()) {
            ClickBehavior behavior = getClickBehavior(getWorld(), getPos().add(0, -1, 0));
            if(behavior != null && (!behavior.hasVisCost() || hasSufficientVis())) {
                startRunning();
            }
        }
    }

    private void performClickBlock() {
        World world = getWorld();

        ClickBehavior behavior = getClickBehavior(world, getPos().add(0, -1, 0));
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
            this.im.activateBlockOrUseItem(fakePlayer, world, wandStack, getPos(), EnumFacing.UP, 0.5F, 0.5F, 0.5F);
            //this.im.activateBlockOrUseItem(fakePlayer, world, wandStack, x, y, z, EnumFacing.UP.ordinal(), 0.5F, 0.5F, 0.5F);
            addInstability(behavior);
        }
    }

    private ClickBehavior getClickBehavior(World world, BlockPos pos) {
        if(pos.getY() >= 0 && !world.isRemote && world instanceof WorldServer && hasOwner()
                && !world.isAirBlock(pos) && wandStack != null && wandStack.stackSize > 0) {
            return RegisteredBlocks.getClawClickBehavior(world, pos);
        }
        return null;
    }

    public boolean isRunning() {
        if(getWorld().isRemote) {
            return animationStates[8] + animationStates[9] + animationStates[11] != 0;
        }
        return cooldown > 0;
    }

    private void startRunning() {
        double xCoord = getPos().getX();
        double yCoord = getPos().getY();
        double zCoord = getPos().getZ();
        PacketHandler.INSTANCE.sendToAllAround(new PacketStartAnimation(PacketStartAnimation.ID_INFUSIONCLAW, getPos()),
                new NetworkRegistry.TargetPoint(getWorld().provider.getDimensionId(), xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 48));
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
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return !isRunning()/* && side > 0 && side < 6*/;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return (!isLocked() || !hasSufficientVis()) && canInsertItem(index, stack, direction);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return wandStack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(isRunning()) {
            return null;
        }

        if(count > 0) {
            ItemStack result = wandStack.copy();
            wandStack = null;
            markForUpdate();
            return result;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        wandStack = stack;
        markForUpdate();
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
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(isRunning()) {
            return false;
        }

        if(stack.getItem() == WAND_ITEM) {
            return !WAND_ITEM.isSceptre(stack) && WAND_ITEM.getFocus(stack) == null;
        }
        return false;
    }

    //TODO uhm
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
