package makeo.gadomancy.common.entities.golems.cores;

import makeo.gadomancy.api.GadomancyApi;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import thaumcraft.common.entities.golems.EntityGolemBase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 25.08.2015 22:24
 */
public class EntityAITasksWrapper extends EntityAITasks {
    private final EntityGolemBase golem;
    private final EntityAITasks original;
    private boolean locked = true;

    public EntityAITasksWrapper(EntityGolemBase golem) {
        super(golem.targetTasks.theProfiler);

        this.golem = golem;
        this.original = golem.targetTasks;

        this.taskEntries = new WrapperList(original.taskEntries);
        this.original.taskEntries = this.taskEntries;
    }

    private class WrapperList extends ArrayList {
        private WrapperList(Collection c) {
            super(c);
        }

        @Override
        public void clear() {
            if(!isLocked()) {
                super.clear();
            }
        }
    }

    public void unlock() {
        locked = false;
    }

    public void lock() {
        locked = true;
    }

    public boolean isLocked() {
        return locked && GadomancyApi.getAdditionalGolemCore(golem) != null;
    }

    @Override
    public void addTask(int index, EntityAIBase ai) {
        if(!isLocked()) {
            original.addTask(index, ai);
        }
    }

    @Override
    public void removeTask(EntityAIBase ai) {
        original.removeTask(ai);
    }

    @Override
    public void onUpdateTasks() {
        original.onUpdateTasks();
    }
}
