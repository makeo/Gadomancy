package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.network.packets.PacketTCNodeBolt;
import makeo.gadomancy.common.network.packets.PacketTCWispyLine;
import makeo.gadomancy.common.node.NodeManipulatorResult;
import makeo.gadomancy.common.node.NodeManipulatorResultHandler;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredMultiblocks;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import makeo.gadomancy.common.utils.MultiblockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TilePedestal;
import thaumcraft.common.tiles.TileWandPedestal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 26.10.2015 20:16
 */
public class TileNodeManipulator extends TileWandPedestal implements IAspectContainer, IWandable {

    private static final int NODE_MANIPULATION_POSSIBLE_WORK_START = 70;
    private static final int NODE_MANIPULATION_WORK_ASPECT_CAP = 120;

    private static final int ELDRITCH_PORTAL_CREATOR_WORK_START = 120;
    private static final int ELDRITCH_PORTAL_CREATOR_ASPECT_CAP = 150;

    //Already set when only multiblock would be present. aka is set, if 'isMultiblockPresent()' returns true.
    private MultiblockType multiblockType = null;
    private boolean multiblockStructurePresent = false;
    private boolean isMultiblock = false;

    private AspectList workAspectList = new AspectList();

    private List<ChunkCoordinates> bufferedCCPedestals = new ArrayList<ChunkCoordinates>();

    private boolean isWorking = false;
    private int workTick = 0;

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
        if(multiblockType == null) {
            if(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, RegisteredMultiblocks.completeNodeManipulatorMultiblock)) {
                multiblockType = MultiblockType.NODE_MANIPULATOR;
            } else if(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, RegisteredMultiblocks.completeEldritchPortalCreator) && checkEldritchEyes(false)) {
                multiblockType = MultiblockType.E_PORTAL_CREATOR;
            }
        }
        if(multiblockType == null) {
            breakMultiblock();
            return;
        }
        switch (multiblockType) {
            case NODE_MANIPULATOR:
                if(!isWorking) {
                    doAspectChecks(NODE_MANIPULATION_WORK_ASPECT_CAP, NODE_MANIPULATION_POSSIBLE_WORK_START);
                } else {
                    manipulationTick();
                }
                break;
            case E_PORTAL_CREATOR:
                if(!isWorking) {
                    doAspectChecks(ELDRITCH_PORTAL_CREATOR_ASPECT_CAP, ELDRITCH_PORTAL_CREATOR_WORK_START);
                } else {
                    if(!checkEldritchEyes(true)) {
                        if(workTick > 1) {
                            workAspectList = new AspectList();
                            workTick = 0;
                            isWorking = false;
                            markDirty();
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }
                        return;
                    }
                    eldritchPortalCreationTick();
                }
                break;
        }
    }

    private boolean checkEldritchEyes(boolean checkForEyes) {
        bufferedCCPedestals.clear();

        int validPedestalsFound = 0;

        labelIt: for (int xDiff = -8; xDiff <= 8; xDiff++) {
            labelZ: for (int zDiff = -8; zDiff <= 8; zDiff++) {
                for (int yDiff = -5; yDiff <= 10; yDiff++) {
                    int itX = xCoord + xDiff;
                    int itY = yCoord + yDiff;
                    int itZ = zCoord + zDiff;

                    Block block = worldObj.getBlock(itX, itY, itZ);
                    int meta = worldObj.getBlockMetadata(itX, itY, itZ);
                    TileEntity te = worldObj.getTileEntity(itX, itY, itZ);
                    if(block != null && block.equals(RegisteredBlocks.blockStoneMachine) && meta == 1
                            && te != null && te instanceof TilePedestal && (!checkForEyes || checkTile((TilePedestal) te))) {
                        validPedestalsFound++;
                        bufferedCCPedestals.add(new ChunkCoordinates(itX, itY, itZ));
                        if(validPedestalsFound >= 4) {
                            break labelIt;
                        }
                        continue labelZ;
                    }
                }
            }
        }
        return validPedestalsFound >= 4;
    }

    private boolean checkTile(TilePedestal te) {
        ItemStack stack = te.getStackInSlot(0);
        return !(stack == null || stack.getItem() != ConfigItems.itemEldritchObject || stack.getItemDamage() != 0);
    }

    private void eldritchPortalCreationTick() {
        workTick++;
        if(workTick < 400) {
            if((workTick & 15) == 0) {
                PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, xCoord, yCoord, zCoord, (byte) 1);
                PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
            }
            if((workTick & 7) == 0) {
                int index = (workTick >> 3) & 3;
                try {
                    ChunkCoordinates cc = bufferedCCPedestals.get(index);
                    PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_RUNES, cc.posX, cc.posY, cc.posZ, (byte) 1);
                    PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
                } catch (Exception exc) {}
            }
            if(worldObj.rand.nextBoolean()) {
                Vec3 rel = getRelPillarLoc(worldObj.rand.nextInt(4));
                PacketTCNodeBolt bolt = new PacketTCNodeBolt(xCoord + 0.5F, yCoord + 2.5F, zCoord + 0.5F, (float) (xCoord + 0.5F + rel.xCoord), (float) (yCoord + 2.5F + rel.yCoord), (float) (zCoord + 0.5F + rel.zCoord), 2, false);
                PacketHandler.INSTANCE.sendToAllAround(bolt, getTargetPoint(32));
            }
            if(worldObj.rand.nextInt(4) == 0) {
                Vec3 relPed = getRelPedestalLoc(worldObj.rand.nextInt(4));
                PacketTCNodeBolt bolt = new PacketTCNodeBolt(xCoord + 0.5F, yCoord + 2.5F, zCoord + 0.5F, (float) (xCoord + 0.5F - relPed.xCoord), (float) (yCoord + 1.5 + relPed.yCoord), (float) (zCoord + 0.5F - relPed.zCoord), 2, false);
                PacketHandler.INSTANCE.sendToAllAround(bolt, getTargetPoint(32));
            }
        } else {
            schedulePortalCreation();
        }
    }

    private void schedulePortalCreation() {
        workTick = 0;
        isWorking = false;
        workAspectList = new AspectList();

        TileEntity te = worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
        if(te == null || !(te instanceof INode)) return;

        consumeEldritchEyes();

        worldObj.removeTileEntity(xCoord, yCoord + 2, zCoord);
        worldObj.setBlockToAir(xCoord, yCoord + 2, zCoord);
        worldObj.setBlock(xCoord, yCoord + 2, zCoord, RegisteredBlocks.blockAdditionalEldrichPortal);

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord + 2, zCoord);
        markDirty();
    }

    private void consumeEldritchEyes() {
        for (ChunkCoordinates cc : bufferedCCPedestals) {
            try {
                TilePedestal pedestal = (TilePedestal) worldObj.getTileEntity(cc.posX, cc.posY, cc.posZ);
                pedestal.setInventorySlotContents(0, null);
                PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, cc.posX, cc.posY, cc.posZ);
                PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
            } catch (Exception exc) {}
        }
    }

    private void manipulationTick() {
        workTick++;
        if(workTick < 300) {
            if(workTick % 16 == 0) {
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

    private Vec3 getRelPedestalLoc(int pedestalId) {
        try {
            ChunkCoordinates cc = bufferedCCPedestals.get(pedestalId);
            return Vec3.createVectorHelper(xCoord - cc.posX, yCoord - cc.posY, zCoord - cc.posZ);
        } catch (Exception exc) {}
        return Vec3.createVectorHelper(0, 0, 0);
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
        float overSized = calcOversize(NODE_MANIPULATION_POSSIBLE_WORK_START);

        workTick = 0;
        isWorking = false;
        workAspectList = new AspectList();

        TileEntity te = worldObj.getTileEntity(xCoord, yCoord + 2, zCoord);
        if(te == null || !(te instanceof INode)) return;
        INode node = (INode) te;
        int areaRange = NODE_MANIPULATION_WORK_ASPECT_CAP - NODE_MANIPULATION_POSSIBLE_WORK_START;
        int percChanceForBetter = 0;
        if(areaRange > 0) {
            percChanceForBetter = (int) ((overSized / ((float) areaRange)) * 100);
        }
        NodeManipulatorResult result;
        do {
            result = NodeManipulatorResultHandler.getRandomResult(worldObj, node, percChanceForBetter);
        } while (!result.affect(worldObj, node));
        PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, xCoord, yCoord + 2, zCoord);
        PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord + 2, zCoord);
        markDirty();
        ((TileEntity) node).markDirty();
    }

    private float calcOversize(int neededAspects) {
        int overall = 0;
        for(Aspect a : Aspect.getPrimalAspects()) {
            overall += workAspectList.getAmount(a) - neededAspects;
        }
        return ((float) overall) / 6F;
    }

    private void doAspectChecks(int aspectCap, int possibleWorkStart) {
        if(canDrainFromWand(aspectCap)) {
            Aspect a = drainAspectFromWand(aspectCap);
            if(a != null) {
                playAspectDrainFromWand(a);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }
        } else {
            checkIfEnoughVis(possibleWorkStart);
        }
    }

    private void checkIfEnoughVis(int start) {
        boolean enough = true;
        for(Aspect a : Aspect.getPrimalAspects()) {
            if(workAspectList.getAmount(a) < start) {
                enough = false;
                break;
            }
        }
        if(enough) {
            isWorking = true;
        }
    }

    private Aspect drainAspectFromWand(int cap) {
        ItemStack stack = getStackInSlot(0);
        if(stack == null || !(stack.getItem() instanceof ItemWandCasting)) return null; //Should never happen..
        AspectList aspects = ((ItemWandCasting) stack.getItem()).getAllVis(stack);
        for(Aspect a : getRandomlyOrderedPrimalAspectList()) {
            if(aspects.getAmount(a) >= 100 && workAspectList.getAmount(a) < cap) {
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

    private boolean canDrainFromWand(int cap) {
        ItemStack stack = getStackInSlot(0);
        if(stack == null || !(stack.getItem() instanceof ItemWandCasting)) return false;
        AspectList aspects = ((ItemWandCasting) stack.getItem()).getAllVis(stack);
        for(Aspect a : Aspect.getPrimalAspects()) {
            if(aspects.getAmount(a) < 100) continue;
            if(workAspectList.getAmount(a) < cap) return true;
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

    private void dropWand() {
        if(getStackInSlot(0) != null)
            InventoryUtils.dropItems(worldObj, xCoord, yCoord, zCoord);
    }

    public void breakMultiblock() {
        MultiblockHelper.MultiblockPattern compareableCompleteStructure, toRestore;
        if(multiblockType == null) {
            workAspectList = new AspectList();
            dropWand();
            workTick = 0;
            isWorking = false;
            return;
        }
        switch (multiblockType) {
            case NODE_MANIPULATOR:
                compareableCompleteStructure = RegisteredMultiblocks.completeNodeManipulatorMultiblock;
                toRestore = RegisteredMultiblocks.incompleteNodeManipulatorMultiblock;
                break;
            case E_PORTAL_CREATOR:
                compareableCompleteStructure = RegisteredMultiblocks.completeEldritchPortalCreator;
                toRestore = RegisteredMultiblocks.incompleteEldritchPortalCreator;
                break;
            default:
                return;
        }
        for(MultiblockHelper.IntVec3 v : compareableCompleteStructure.keySet()) {
            MultiblockHelper.BlockInfo info = compareableCompleteStructure.get(v);
            MultiblockHelper.BlockInfo restoreInfo = toRestore.get(v);
            if(info.block == RegisteredBlocks.blockNode || (info.block == RegisteredBlocks.blockStoneMachine && (info.meta == 0 || info.meta == 3))
                    || info.block == Blocks.air || info.block == RegisteredBlocks.blockNodeManipulator) continue;
            int absX = v.x + xCoord;
            int absY = v.y + yCoord;
            int absZ = v.z + zCoord;
            if(worldObj.getBlock(absX, absY, absZ) == info.block && worldObj.getBlockMetadata(absX, absY, absZ) == info.meta) {
                worldObj.setBlock(absX, absY, absZ, Blocks.air, 0, 0);
                worldObj.setBlock(absX, absY, absZ, restoreInfo.block, restoreInfo.meta, 0);
                worldObj.markBlockForUpdate(absX, absY, absZ);
            }
        }

        workAspectList = new AspectList();
        this.multiblockType = null;
        dropWand();
        workTick = 0;
        isWorking = false;
    }

    public void formMultiblock() {
        MultiblockHelper.MultiblockPattern toBuild;
        if(multiblockType == null) return;
        switch (multiblockType) {
            case NODE_MANIPULATOR:
                toBuild = RegisteredMultiblocks.completeNodeManipulatorMultiblock;
                break;
            case E_PORTAL_CREATOR:
                toBuild = RegisteredMultiblocks.completeEldritchPortalCreator;
                break;
            default:
                return;
        }
        for(MultiblockHelper.IntVec3 v : toBuild.keySet()) {
            MultiblockHelper.BlockInfo info = toBuild.get(v);
            if(info.block == RegisteredBlocks.blockNode || (info.block == RegisteredBlocks.blockStoneMachine && (info.meta == 0 || info.meta == 1 || info.meta == 3))
                    || info.block == Blocks.air || info.block == RegisteredBlocks.blockNodeManipulator) continue;
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
        this.isWorking = tag.getBoolean("manipulating");
        this.workTick = tag.getInteger("workTick");
        if(tag.hasKey("multiblockType")) {
            this.multiblockType = MultiblockType.values()[tag.getInteger("multiblockType")];
        }
        workAspectList.readFromNBT(tag, "workAspectList");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("mBlockPresent", this.multiblockStructurePresent);
        tag.setBoolean("mBlockState", this.isMultiblock);
        tag.setBoolean("manipulating", this.isWorking);
        tag.setInteger("workTick", this.workTick);
        if(multiblockType != null) {
            tag.setInteger("multiblockType", this.multiblockType.ordinal());
        }
        workAspectList.writeToNBT(tag, "workAspectList");
        compound.setTag("Gadomancy", tag);
    }

    public boolean isInMultiblock() {
        return isMultiblock;
    }

    public boolean isMultiblockStructurePresent() {
        return multiblockStructurePresent;
    }

    public MultiblockType getMultiblockType() {
        return multiblockType;
    }

    public boolean checkMultiblock() {
        boolean prevState = isMultiblockStructurePresent();
        if(prevState) { //If there is already a multiblock formed...
            if(isInMultiblock()) { //If we were actually in multiblock before
                if(multiblockType == null) {
                    if(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, RegisteredMultiblocks.completeNodeManipulatorMultiblock)) {
                        multiblockType = MultiblockType.NODE_MANIPULATOR;
                    } else if(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, RegisteredMultiblocks.completeEldritchPortalCreator) && checkEldritchEyes(false)) {
                        multiblockType = MultiblockType.E_PORTAL_CREATOR;
                    }
                }
                if(multiblockType == null) {
                    breakMultiblock();
                    return false;
                }
                switch (multiblockType) {
                    case NODE_MANIPULATOR:
                        setMultiblockStructurePresent(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, RegisteredMultiblocks.completeNodeManipulatorMultiblock), MultiblockType.NODE_MANIPULATOR);
                        break;
                    case E_PORTAL_CREATOR:
                        setMultiblockStructurePresent(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, RegisteredMultiblocks.completeEldritchPortalCreator) && checkEldritchEyes(false), MultiblockType.E_PORTAL_CREATOR);
                        break;
                }
            } else { //If we weren't in multiblock eventhough it would be possible.
                checkForNonExistingMultiblock();
            }
        } else { //If there was no multiblock formed before..
            checkForNonExistingMultiblock();
        }
        return isMultiblockStructurePresent();
    }

    private void setMultiblockStructurePresent(boolean present, MultiblockType type) {
        if(present) {
            this.multiblockType = type;
        }
        this.multiblockStructurePresent = present;
    }

    private void checkForNonExistingMultiblock() {
        Map<MultiblockHelper.MultiblockPattern, MultiblockType> patternMap = new HashMap<MultiblockHelper.MultiblockPattern, MultiblockType>();
        patternMap.put(RegisteredMultiblocks.incompleteEldritchPortalCreator, MultiblockType.E_PORTAL_CREATOR);
        patternMap.put(RegisteredMultiblocks.incompleteNodeManipulatorMultiblock, MultiblockType.NODE_MANIPULATOR);

        for(MultiblockHelper.MultiblockPattern pattern : patternMap.keySet()) {
            if(MultiblockHelper.isMultiblockPresent(worldObj, xCoord, yCoord, zCoord, pattern)) {
                if(pattern == RegisteredMultiblocks.incompleteEldritchPortalCreator) {
                    if(!checkEldritchEyes(false)) return;
                }
                setMultiblockStructurePresent(true, patternMap.get(pattern));
                return;
            }
        }
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
    public void setAspects(AspectList aspectList) {}

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
    public void onUsingWandTick(ItemStack stack, EntityPlayer player, int i) {}

    @Override
    public void onWandStoppedUsing(ItemStack stack, World world, EntityPlayer player, int i) {}

    public static enum MultiblockType {

        NODE_MANIPULATOR(Gadomancy.MODID.toUpperCase() + ".NODE_MANIPULATOR", RegisteredRecipes.costsNodeManipulatorMultiblock),
        E_PORTAL_CREATOR(Gadomancy.MODID.toUpperCase() + ".E_PORTAL_CREATOR", RegisteredRecipes.costsEldritchPortalCreatorMultiblock);

        private String research;
        private AspectList costs;

        private MultiblockType(String research, AspectList costs) {
            this.research = research;
            this.costs = costs;
        }

        public String getResearchNeeded() {
            return research;
        }

        public AspectList getMultiblockCosts() {
            return costs;
        }
    }
}
