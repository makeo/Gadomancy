package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.network.packets.PacketTCNodeBolt;
import makeo.gadomancy.common.network.packets.PacketTCWispyLine;
import makeo.gadomancy.common.node.NodeManipulatorResult;
import makeo.gadomancy.common.node.NodeManipulatorResultHandler;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredMultiblocks;
import makeo.gadomancy.common.utils.MultiblockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileWandPedestal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 26.10.2015 20:16
 */
public class TileNodeManipulator extends TileWandPedestal implements IAspectContainer, IWandable {

    private static final int POSSIBLE_WORK_START = 70;
    private static final int WORK_ASPECT_CAP = 150;

    private boolean multiblockStructurePresent = false;
    private boolean isMultiblock = false;

    private int workPhase = 0;
    private AspectList workAspectList = new AspectList();

    private boolean isManipulating = false;
    private int manipulatorTick = 0;

    @Override
    public void updateEntity() {

        if(worldObj.isRemote) return;

        if(isInMultiblock()) {
            checkMultiblockTick();
        }
        if(isInMultiblock()) {
            multiblockTick();
        }
    }

    private void multiblockTick() {
        if(!isManipulating) {
            doAspectChecks();
        } else {
            manipulationTick();
        }
    }

    private void manipulationTick() {
        manipulatorTick++;
        if(manipulatorTick < 300) {
            if(manipulatorTick % 16 == 0) {
                PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, xCoord, yCoord, zCoord);
                PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
            }
            if(worldObj.rand.nextInt(4) == 0) {
                Vec3 rel = getRelPillarLoc(worldObj.rand.nextInt(4));
                PacketTCNodeBolt bolt = new PacketTCNodeBolt(xCoord + 0.5F, yCoord + 2.5F, zCoord + 0.5F, (float) (xCoord + 0.5F + rel.xCoord), (float) (yCoord + 2.5F + rel.yCoord), (float) (zCoord + 0.5F + rel.zCoord), 0, false);
                PacketHandler.INSTANCE.sendToAllAround(bolt, getTargetPoint(32));
            }
        } else {
            scheduleManipulation();
        }
    }

    private Vec3 getRelPillarLoc(int pillarId) {
        switch (pillarId) {
            case 0:
                return Vec3.createVectorHelper(0.7, -0.6, 0.7);
            case 1:
                return Vec3.createVectorHelper(-0.7, -0.6, 0.7);
            case 2:
                return Vec3.createVectorHelper(-0.7, -0.6, -0.7);
            case 3:
                return Vec3.createVectorHelper(0.7, -0.6, -0.7);
        }
        return Vec3.createVectorHelper(0, 0, 0);
    }

    private void scheduleManipulation() {
        manipulatorTick = 0;
        isManipulating = false;
        workAspectList = new AspectList();

        TileEntity te = worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
        if(te == null || !(te instanceof TileExtendedNode)) return;
        TileExtendedNode node = (TileExtendedNode) te;
        NodeManipulatorResult result;
        do {
            result = NodeManipulatorResultHandler.getRandomResult(node);
        } while (!result.affect(node));
        PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, xCoord, yCoord + 2, zCoord);
        PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord + 2, zCoord);
        markDirty();
        node.markDirty();
    }

    private void doAspectChecks() {
        if(canDrainFromWand()) {
            Aspect a = drainAspectFromWand();
            if(a != null) {
                playAspectDrainFromWand(a);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }
        } else {
            checkIfEnoughVis();
        }
    }

    private void checkIfEnoughVis() {
        boolean enough = true;
        for(Aspect a : Aspect.getPrimalAspects()) {
            if(workAspectList.getAmount(a) < POSSIBLE_WORK_START) {
                enough = false;
                break;
            }
        }
        if(enough) {
            isManipulating = true;
        }
    }

    private Aspect drainAspectFromWand() {
        ItemStack stack = getStackInSlot(0);
        if(stack == null || !(stack.getItem() instanceof ItemWandCasting)) return null; //Should never happen..
        AspectList aspects = ((ItemWandCasting) stack.getItem()).getAllVis(stack);
        for(Aspect a : getRandomlyOrderedPrimalAspectList()) {
            if(aspects.getAmount(a) >= 100 && workAspectList.getAmount(a) < WORK_ASPECT_CAP) {
                int amt = aspects.getAmount(a);
                ((ItemWandCasting) stack.getItem()).storeVis(stack, a, amt - 100);
                workAspectList.add(a, 1);
                return a;
            }
        }
        return null;
    }

    private List<Aspect> getRandomlyOrderedPrimalAspectList() {
        ArrayList<Aspect> primals = (ArrayList<Aspect>) Aspect.getPrimalAspects().clone();
        Collections.shuffle(primals);
        return primals;
    }

    private boolean canDrainFromWand() {
        ItemStack stack = getStackInSlot(0);
        if(stack == null || !(stack.getItem() instanceof ItemWandCasting)) return false;
        AspectList aspects = ((ItemWandCasting) stack.getItem()).getAllVis(stack);
        for(Aspect a : Aspect.getPrimalAspects()) {
            if(aspects.getAmount(a) < 100) continue;
            if(workAspectList.getAmount(a) < WORK_ASPECT_CAP) return true;
        }
        return false;
    }

    private void checkMultiblockTick() {
        checkMultiblock();
        if(!isMultiblockStructurePresent()) {
            breakMultiblock();
            isMultiblock = false;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }
    }

    private void playAspectDrainFromWand(Aspect drained) {
        if(drained == null) return;
        NetworkRegistry.TargetPoint point = getTargetPoint(32);
        PacketTCWispyLine line = new PacketTCWispyLine(worldObj.provider.dimensionId, xCoord + 0.5, yCoord + 0.8, zCoord + 0.5,
                xCoord + 0.5, yCoord + 1.4 + (((double) worldObj.rand.nextInt(4)) / 10D), zCoord + 0.5, 40, drained.getColor());
        PacketHandler.INSTANCE.sendToAllAround(line, point);
    }

    @Override
    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
        return super.receiveClientEvent(p_145842_1_, p_145842_2_);
    }

    private void dropWand() {
        if(getStackInSlot(0) != null)
            InventoryUtils.dropItems(worldObj, xCoord, yCoord, zCoord);
    }

    public void breakMultiblock() {
        MultiblockHelper.MultiblockPattern compareableCompleteStructure = RegisteredMultiblocks.completeNodeManipulatorMultiblock;
        MultiblockHelper.MultiblockPattern toRestore = RegisteredMultiblocks.incompleteNodeManipulatorMultiblock;
        for(MultiblockHelper.IntVec3 v : compareableCompleteStructure.keySet()) {
            MultiblockHelper.BlockInfo info = compareableCompleteStructure.get(v);
            MultiblockHelper.BlockInfo restoreInfo = toRestore.get(v);
            if(info.block == RegisteredBlocks.blockNode || info.block == Blocks.air || info.block == RegisteredBlocks.blockNodeManipulator || restoreInfo == null) continue;
            int absX = v.x + xCoord;
            int absY = v.y + yCoord;
            int absZ = v.z + zCoord;
            if(worldObj.getBlock(absX, absY, absZ) == info.block && worldObj.getBlockMetadata(absX, absY, absZ) == info.meta) {
                worldObj.setBlock(absX, absY, absZ, Blocks.air, 0, 0);
                worldObj.setBlock(absX, absY, absZ, restoreInfo.block, restoreInfo.meta, 0);
                worldObj.markBlockForUpdate(absX, absY, absZ);
            }
        }

        dropWand();
    }

    public void formMultiblock() {
        MultiblockHelper.MultiblockPattern toBuild = RegisteredMultiblocks.completeNodeManipulatorMultiblock;
        for(MultiblockHelper.IntVec3 v : toBuild.keySet()) {
            MultiblockHelper.BlockInfo info = toBuild.get(v);
            if(info.block == RegisteredBlocks.blockNode || info.block == Blocks.air || info.block == RegisteredBlocks.blockNodeManipulator) continue;
            int absX = v.x + xCoord;
            int absY = v.y + yCoord;
            int absZ = v.z + zCoord;
            worldObj.setBlock(absX, absY, absZ, Blocks.air, 0, 0);
            worldObj.setBlock(absX, absY, absZ, info.block, info.meta, 0);
            worldObj.markBlockForUpdate(absX, absY, absZ);
        }
        NetworkRegistry.TargetPoint target = getTargetPoint(32);
        TileManipulatorPillar pillar = (TileManipulatorPillar) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord + 1); //wrong
        pillar.setOrientation((byte) 5);
        PacketStartAnimation animation = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, pillar.xCoord, pillar.yCoord, pillar.zCoord);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        TileManipulatorPillar pillar2 = (TileManipulatorPillar) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord + 1);
        pillar2.setOrientation((byte) 3);
        animation = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, pillar2.xCoord, pillar2.yCoord, pillar2.zCoord);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        TileManipulatorPillar pillar3 = (TileManipulatorPillar) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord - 1); //wrong
        pillar3.setOrientation((byte) 4);
        animation = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, pillar3.xCoord, pillar3.yCoord, pillar3.zCoord);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        animation = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, xCoord - 1, yCoord, zCoord - 1);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
        this.isMultiblock = true;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        NBTTagCompound tag = compound.getCompoundTag("Gadomancy");
        this.multiblockStructurePresent = tag.getBoolean("mBlockPresent");
        this.isMultiblock = tag.getBoolean("mBlockState");
        this.isManipulating = tag.getBoolean("manipulating");
        this.manipulatorTick = tag.getInteger("manipulatorTick");
        this.workPhase = tag.getInteger("workPhase");
        workAspectList.readFromNBT(tag, "workAspectList");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("mBlockPresent", this.multiblockStructurePresent);
        tag.setBoolean("mBlockState", this.isMultiblock);
        tag.setBoolean("manipulating", this.isManipulating);
        tag.setInteger("manipulatorTick", this.manipulatorTick);
        tag.setInteger("workPhase", this.workPhase);
        workAspectList.writeToNBT(tag, "workAspectList");
        compound.setTag("Gadomancy", tag);
    }

    public boolean isInMultiblock() {
        return isMultiblock;
    }

    public boolean isMultiblockStructurePresent() {
        return multiblockStructurePresent;
    }

    public boolean checkMultiblock() {
        boolean prevState = isMultiblockStructurePresent();
        MultiblockHelper.MultiblockPattern patternToCheck;
        if(prevState) { //If there is already a multiblock formed...
            if(isInMultiblock()) { //If we were actually in multiblock before
                patternToCheck = RegisteredMultiblocks.completeNodeManipulatorMultiblock;
            } else { //If we were'nt in multiblock eventhough it would be possible.
                patternToCheck = RegisteredMultiblocks.incompleteNodeManipulatorMultiblock;
            }
            this.multiblockStructurePresent = MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, patternToCheck);
        } else { //If there was no multiblock formed before..
            patternToCheck = RegisteredMultiblocks.incompleteNodeManipulatorMultiblock;
            this.multiblockStructurePresent = MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, patternToCheck);
        }
        return isMultiblockStructurePresent();
    }

    @Override
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
        return isInMultiblock() && super.canInsertItem(par1, par2ItemStack, par3);
    }

    public NetworkRegistry.TargetPoint getTargetPoint(double radius) {
        return new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, radius);
    }

    @Override
    public AspectList getAspects() {
        return workAspectList;
    }

    @Override
    public void setAspects(AspectList aspectList) {

    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return false;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        return 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }

    @Override
    public int onWandRightClick(World world, ItemStack stack, EntityPlayer player, int i, int i2, int i3, int i4, int i5) {
        return 0;
    }

    @Override
    public ItemStack onWandRightClick(World world, ItemStack stack, EntityPlayer player) {
        return null;
    }

    @Override
    public void onUsingWandTick(ItemStack stack, EntityPlayer player, int i) {

    }

    @Override
    public void onWandStoppedUsing(ItemStack stack, World world, EntityPlayer player, int i) {

    }
}
