package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
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
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileWandPedestal;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 26.10.2015 20:16
 */
public class TileNodeManipulator extends TileWandPedestal implements IAspectContainer, IWandable {

    private int ticksExisted = 0;
    private boolean multiblockStructurePresent = false;
    private boolean isMultiblock = false;

    @Override
    public void updateEntity() {
        ticksExisted++;

        if(isInMultiblock() && ticksExisted % 2 == 0 && !worldObj.isRemote) {
            checkMultiblock();
            if(!isMultiblockStructurePresent()) {
                breakMultiblock();
                isMultiblock = false;
            }

            //TODO multiblock tick.
        }
    }

    @Override
    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
        return super.receiveClientEvent(p_145842_1_, p_145842_2_);
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

        if(getStackInSlot(0) != null)
            InventoryUtils.dropItems(worldObj, xCoord, yCoord, zCoord);
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
        NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32);
        TileManipulatorPillar pillar = (TileManipulatorPillar) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord + 1); //wrong
        pillar.setOrientation((byte) 5);
        PacketStartAnimation animation = new PacketStartAnimation(PacketStartAnimation.ID_PILLAR_RUNES, pillar.xCoord, pillar.yCoord, pillar.zCoord);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        TileManipulatorPillar pillar2 = (TileManipulatorPillar) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord + 1);
        pillar2.setOrientation((byte) 3);
        animation = new PacketStartAnimation(PacketStartAnimation.ID_PILLAR_RUNES, pillar2.xCoord, pillar2.yCoord, pillar2.zCoord);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        TileManipulatorPillar pillar3 = (TileManipulatorPillar) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord - 1); //wrong
        pillar3.setOrientation((byte) 4);
        animation = new PacketStartAnimation(PacketStartAnimation.ID_PILLAR_RUNES, pillar3.xCoord, pillar3.yCoord, pillar3.zCoord);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        animation = new PacketStartAnimation(PacketStartAnimation.ID_PILLAR_RUNES, xCoord - 1, yCoord, zCoord - 1);
        PacketHandler.INSTANCE.sendToAllAround(animation, target);
        markDirty();
        this.isMultiblock = true;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        NBTTagCompound tag = compound.getCompoundTag("Gadomancy");
        this.multiblockStructurePresent = tag.getBoolean("mBlockPresent");
        this.isMultiblock = tag.getBoolean("mBlockState");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("mBlockPresent", this.multiblockStructurePresent);
        tag.setBoolean("mBlockState", this.isMultiblock);
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

    @Override
    public AspectList getAspects() {
        return new AspectList();
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
