package makeo.gadomancy.common.entities.ai;

import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.passive.EntityTameable;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 31.10.2015 15:55
 */
public class AIFollowOwner extends EntityAIFollowOwner {
    EntityTameable entity;

    public AIFollowOwner(EntityTameable entity, float minDist, float maxDist) {
        super(entity, 0, minDist, maxDist);
        this.entity = entity;
    }

    @Override
    public void startExecuting() {
        boolean avoidWater = entity.getNavigator().getAvoidsWater();
        super.startExecuting();
        entity.getNavigator().setAvoidsWater(avoidWater);
    }

    @Override
    public void resetTask() {
        boolean avoidWater = entity.getNavigator().getAvoidsWater();
        super.resetTask();
        entity.getNavigator().setAvoidsWater(avoidWater);
    }

    @Override
    public void updateTask() {
        super.field_75336_f = entity.getAIMoveSpeed();
        super.updateTask();
    }
}
