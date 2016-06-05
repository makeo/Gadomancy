package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 05.06.2016 15:35
 */
public class TileAISuppressor extends SynchronizedTileEntity {
    private static final AxisAlignedBB PART_RANGE = AxisAlignedBB.getBoundingBox(0, 0, 0, 10, 10, 10);

    private Map<Entity, Float> tracked = new HashMap<Entity, Float>();
    private int count = 0;

    @Override
    public void updateEntity() {
        if(count++ % 10 == 0) {
            AxisAlignedBB cube = PART_RANGE.addCoord(xCoord + .5 - ((PART_RANGE.maxX - PART_RANGE.minX) / 2), yCoord + .5 - ((PART_RANGE.maxY - PART_RANGE.minY) / 2), zCoord + .5 - ((PART_RANGE.maxZ - PART_RANGE.minZ) / 2));
            List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class, cube);

            List<Entity> outdated = new ArrayList<Entity>(tracked.keySet());

            for(Entity entity : entities) {
                if(!tracked.containsKey(entity)) {
                    tracked.put(entity, entity.entityCollisionReduction);
                }

                entity.entityCollisionReduction = 0;
                outdated.remove(entity);
            }

            for(Entity entity : outdated) {
                float val = tracked.get(entity);
                tracked.remove(entity);
                entity.entityCollisionReduction = val;
            }
        }
    }
}
