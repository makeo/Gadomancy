package makeo.gadomancy.common.entities;

import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.utils.PosSerializer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thaumcraft.common.entities.EntityPermanentItem;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 16.11.2015 19:59
 */
public class EntityPermNoClipItem extends EntityPermanentItem {

    private float fixPosX = -1, fixPosY = -1, fixPosZ = -1; //Can never actually get those values since pylon coords are never .0

    private int masterX, masterY, masterZ; //Coords of its master tile.

    public EntityPermNoClipItem(World world) {
        super(world);
    }

    public EntityPermNoClipItem(World world, float x, float y, float z, ItemStack stack, int masterX, int masterY, int masterZ) {
        super(world, x, y, z, stack);
        this.fixPosX = x;
        this.fixPosY = y;
        this.fixPosZ = z;
        this.masterX = masterX;
        this.masterY = masterY;
        this.masterZ = masterZ;

        BlockPos masterCoords = new BlockPos(this.masterX, this.masterY, this.masterZ);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherMasterId, PosSerializer.serialize(masterCoords));
        int fixX = Float.floatToIntBits(x);
        int fixY = Float.floatToIntBits(y);
        int fixZ = Float.floatToIntBits(z);
        BlockPos fixCoords = new BlockPos(fixX, fixY, fixZ);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedId, PosSerializer.serialize(fixCoords));
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        getDataWatcher().addObjectByDataType(ModConfig.entityNoClipItemDatawatcherMasterId, 4);
        getDataWatcher().addObjectByDataType(ModConfig.entityNoClipItemDatawatcherFixedId, 4);

        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherMasterId, PosSerializer.serialize(new BlockPos(0, 0, 0)));
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedId, PosSerializer.serialize(new BlockPos(0, 0, 0)));
    }

    @Override
    public void onUpdate() {
        noClip = true;
        super.onUpdate();
        motionX = 0;
        motionY = 0;
        motionZ = 0;
        if (getDataWatcher().getWatchableObjectString(ModConfig.entityNoClipItemDatawatcherFixedId) == null)
            return;

        if((ticksExisted & 1) == 0) {
            String serialized = getDataWatcher().getWatchableObjectString(ModConfig.entityNoClipItemDatawatcherMasterId);
            if(serialized == null) return;
            BlockPos pos = PosSerializer.deserialize(serialized);
            TileEntity te = worldObj.getTileEntity(pos);
            if(te != null && te instanceof IItemMasterTile) {
                ((IItemMasterTile) te).informMaster();
                ItemChangeTask task = ((IItemMasterTile) te).getAndRemoveScheduledChangeTask();
                if(task != null) {
                    task.changeItem(this);
                }
                ((IItemMasterTile) te).broadcastItemStack(getEntityItem());
            }
        }

        String fixCSerialized = getDataWatcher().getWatchableObjectString(ModConfig.entityNoClipItemDatawatcherFixedId);
        BlockPos pos = PosSerializer.deserialize(fixCSerialized);
        float fX = Float.intBitsToFloat(pos.getX());
        float fY = Float.intBitsToFloat(pos.getY());
        float fZ = Float.intBitsToFloat(pos.getZ());
        setPositionAndRotation(fX, fY, fZ, 0, 0);



        if ((ticksExisted & 7) == 0 && !worldObj.isRemote) {
            String masterCoordStr = getDataWatcher().getWatchableObjectString(ModConfig.entityNoClipItemDatawatcherMasterId);
            BlockPos masterPos = PosSerializer.deserialize(masterCoordStr);
            TileEntity te = worldObj.getTileEntity(masterPos);
            if (te == null || !(te instanceof IItemMasterTile) || !((IItemMasterTile) te).canStillHoldItem()) {
                EntityItem item = new EntityItem(worldObj, posX, posY, posZ, getEntityItem());
                worldObj.spawnEntityInWorld(item);
                setDead();
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource ds, float p_70097_2_) {
        if(ds.equals(DamageSource.inFire) || ds.equals(DamageSource.onFire)) return false;
        return super.attackEntityFrom(ds, p_70097_2_);
    }

    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound com) {
        super.readEntityFromNBT(com);

        this.fixPosX = com.getFloat("fX");
        this.fixPosY = com.getFloat("fY");
        this.fixPosZ = com.getFloat("fZ");

        this.masterX = com.getInteger("mX");
        this.masterY = com.getInteger("mY");
        this.masterZ = com.getInteger("mZ");

        BlockPos pos = new BlockPos(masterX, masterY, masterZ);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherMasterId, PosSerializer.serialize(pos));
        BlockPos cc = new BlockPos(Float.floatToIntBits(fixPosX), Float.floatToIntBits(fixPosY), Float.floatToIntBits(fixPosZ));
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedId, PosSerializer.serialize(cc));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound com) {
        super.writeEntityToNBT(com);

        com.setFloat("fX", fixPosX);
        com.setFloat("fY", fixPosY);
        com.setFloat("fZ", fixPosZ);

        com.setInteger("mX", masterX);
        com.setInteger("mY", masterY);
        com.setInteger("mZ", masterZ);
    }

    public static interface IItemMasterTile {

        public boolean canStillHoldItem();

        public void informMaster();

        public void informItemRemoval();

        public ItemChangeTask getAndRemoveScheduledChangeTask();

        public void broadcastItemStack(ItemStack itemStack);

    }

    public static abstract class ItemChangeTask {

        public abstract void changeItem(EntityPermNoClipItem item);

    }

}
