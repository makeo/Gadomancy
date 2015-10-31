package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.10.2015 16:27
 */
public class TileManipulationFocus extends SynchronizedTileEntity {
    public int fociId;

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        fociId = compound.getInteger("fociId");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("fociId", fociId);
    }
}
