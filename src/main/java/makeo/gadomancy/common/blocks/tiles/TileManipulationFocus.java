package makeo.gadomancy.common.blocks.tiles;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.10.2015 16:27
 */
public class TileManipulationFocus extends SynchronizedTileEntity {
    private int fociId;
    public TileManipulationFocus(int fociId) {
        this.fociId = fociId;
    }

    public int getFociId() {
        return fociId;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
