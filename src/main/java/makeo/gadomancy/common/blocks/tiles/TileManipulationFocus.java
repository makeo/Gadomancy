package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.10.2015 16:27
 */
public class TileManipulationFocus extends SynchronizedTileEntity {

    private int fociId = -1;


    public int getFociId() {
        return fociId == -1 ? blockMetadata != -1 ? blockMetadata == 0 ? 0 : 1 : -1 : fociId;
    }

    public TileManipulationFocus setFociId(int fociId) {
        this.fociId = fociId;
        return this;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("fociId", this.fociId);
        super.writeCustomNBT(compound);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        this.fociId = compound.getInteger("fociId");
        super.readCustomNBT(compound);
    }
}
