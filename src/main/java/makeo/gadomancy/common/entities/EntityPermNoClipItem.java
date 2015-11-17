package makeo.gadomancy.common.entities;

import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.data.ModConfig;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import thaumcraft.common.entities.EntityPermanentItem;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
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

        ChunkCoordinates masterCoords = new ChunkCoordinates(this.masterX, this.masterY, this.masterZ);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherMasterId, masterCoords);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedXId, x);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedYId, y);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedZId, z);
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        getDataWatcher().addObjectByDataType(ModConfig.entityNoClipItemDatawatcherMasterId, 6);
        getDataWatcher().addObjectByDataType(ModConfig.entityNoClipItemDatawatcherFixedXId, 3);
        getDataWatcher().addObjectByDataType(ModConfig.entityNoClipItemDatawatcherFixedYId, 3);
        getDataWatcher().addObjectByDataType(ModConfig.entityNoClipItemDatawatcherFixedZId, 3);

        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherMasterId, new ChunkCoordinates(0, 0, 0));
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedXId, -1F);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedYId, -1F);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedZId, -1F);
    }

    @Override
    public void onUpdate() {
        noClip = true;
        super.onUpdate();
        setVelocity(0, 0, 0);
        if (getDataWatcher().getWatchedObject(ModConfig.entityNoClipItemDatawatcherFixedXId).getObject() == null)
            return;

        float fX = getDataWatcher().getWatchableObjectFloat(ModConfig.entityNoClipItemDatawatcherFixedXId);
        float fY = getDataWatcher().getWatchableObjectFloat(ModConfig.entityNoClipItemDatawatcherFixedYId);
        float fZ = getDataWatcher().getWatchableObjectFloat(ModConfig.entityNoClipItemDatawatcherFixedZId);
        setPositionAndRotation(fX, fY, fZ, 0, 0);

        if ((ticksExisted & 7) == 0 && !worldObj.isRemote) {
            ChunkCoordinates masterCoords = (ChunkCoordinates) getDataWatcher().getWatchedObject(ModConfig.entityNoClipItemDatawatcherMasterId).getObject();
            TileEntity te = worldObj.getTileEntity(masterCoords.posX, masterCoords.posY, masterCoords.posZ);
            if (te == null || !(te instanceof TileAuraPylon) || !((TileAuraPylon) te).isMasterTile()) {
                EntityItem item = new EntityItem(worldObj, posX, posY, posZ, getEntityItem());
                worldObj.spawnEntityInWorld(item);
                setDead();
            }
        }
    }

    @Override
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_) {
        this.setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
        this.setRotation(p_70056_7_, p_70056_8_);
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

        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherMasterId, new ChunkCoordinates(masterX, masterY, masterZ));
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedXId, fixPosX);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedYId, fixPosY);
        getDataWatcher().updateObject(ModConfig.entityNoClipItemDatawatcherFixedZId, fixPosZ);
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
}
