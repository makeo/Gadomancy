package makeo.gadomancy.common.blocks.tiles;

import makeo.gadomancy.common.utils.world.TCMazeHandler;
import makeo.gadomancy.common.utils.world.TCMazeSession;
import net.minecraft.entity.Entity;
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
    private int count = 0;
    private Entity[] bosses = null;

    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            if (super.count != -1) {
                if (super.count + 1 > 100) {
                    ConcurrentHashMap<CellLoc, Short> old = MazeHandler.labyrinth;
                    MazeHandler.labyrinth = TCMazeHandler.labyrinthCopy;

                    int nextEntityId = Entity.nextEntityID;

                    super.updateEntity();

                    int count = Entity.nextEntityID - nextEntityId;
                    bosses = new Entity[count];
                    for(int i = 0; i < count; i++) {
                        bosses[i] = worldObj.getEntityByID(nextEntityId + i);
                    }

                    MazeHandler.labyrinth = old;
                    return;
                }
            }
        }

        super.updateEntity();

        if(bosses != null && ++count % 20 == 0) {
            for(Entity entity : bosses) {
                if(entity != null && !entity.isDead) {
                    return;
                }
            }

            for(TCMazeSession session : TCMazeHandler.getSessions().values()) {
                if(session.chunksAffected != null && session.chunksAffected.containsKey(new CellLoc(xCoord >> 4, zCoord >> 4))) {
                    TCMazeHandler.handleBossFinish(session);
                    break;
                }
            }
            bosses = null;
        }
    }
}
