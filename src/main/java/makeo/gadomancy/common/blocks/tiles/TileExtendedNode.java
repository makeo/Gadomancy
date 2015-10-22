package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.common.tiles.TileNode;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 21.10.2015 23:39
 */
public class TileExtendedNode extends TileNode {

    private boolean growing = false;

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);

        NBTTagCompound compound = nbttagcompound.getCompoundTag("Gadomancy");
        this.growing = compound.getBoolean("growing");

    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);

        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("growing", growing);

        nbttagcompound.setTag("Gadomancy", compound);
    }

}
