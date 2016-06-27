package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 07.10.2015 18:39
 */
public class SynchronizedTileEntity extends TileEntity {

    public ChunkCoordinates getCoords() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCustomNBT(compound);
    }

    public void readCustomNBT(NBTTagCompound compound) {}

    public final void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCustomNBT(compound);
    }

    public void writeCustomNBT(NBTTagCompound compound) {}

    public final Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeCustomNBT(compound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 255, compound);
    }

    public final void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity paket) {
        super.onDataPacket(manager, paket);
        readCustomNBT(paket.func_148857_g());
    }
}
