package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.utils.world.TCMazeHandler;
import thaumcraft.common.lib.world.dim.CellLoc;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.tiles.TileEldritchLock;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 09.11.2015 21:20
 */
public class TileOverrideEldritchLock extends TileEldritchLock {
    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            if (this.count != -1) {
                if (this.count + 1 > 100) {
                    ConcurrentHashMap<CellLoc, Short> old = MazeHandler.labyrinth;
                    MazeHandler.labyrinth = TCMazeHandler.labyrinthCopy;
                    super.updateEntity();
                    MazeHandler.labyrinth = old;
                    return;
                }
            }
        }
        super.updateEntity();
    }
}
