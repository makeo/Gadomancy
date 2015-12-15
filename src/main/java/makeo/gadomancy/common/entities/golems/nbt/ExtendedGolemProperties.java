package makeo.gadomancy.common.entities.golems.nbt;

import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.entities.golems.cores.EntityAITasksWrapper;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 28.07.2015 17:25
 */
public class ExtendedGolemProperties implements IExtendedEntityProperties {
    private static final String TYPE_TAG = "golemTypeOverride";
    private static final String FORGE_TAG = "ForgeData";

    private EntityGolemBase golem;

    private boolean updateHealth = false;
    private float health;

    private boolean isSitting = false;

    private EntityAITasksWrapper taskWrapper = null;
    private EntityAITasksWrapper targetWrapper = null;

    public ExtendedGolemProperties(EntityGolemBase golem) {
        this.golem = golem;
    }

    public boolean shouldUpdateHealth() {
        return updateHealth;
    }

    public void resetUpdateHealth() {
        updateHealth = false;
    }

    public float getHealth() {
        return health;
    }

    public boolean isSitting() {
        return isSitting;
    }

    public void setSitting(boolean isSitting) {
        this.isSitting = isSitting;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        AdditionalGolemType type = GadomancyApi.getAdditionalGolemType(golem.getGolemType());
        if(type != null) {
            NBTTagCompound entityData;
            if(compound.hasKey(FORGE_TAG)) {
                entityData = compound.getCompoundTag(FORGE_TAG);
            } else {
                entityData = new NBTTagCompound();
                compound.setTag(FORGE_TAG, entityData);
            }

            writeAdvancedGolemType(entityData, type);
            golem.ridingEntity = new OverrideRidingEntity(golem, compound, golem.ridingEntity);
        }

        compound.setBoolean(Gadomancy.MODID + ":sitting", isSitting);
    }

    private static void writeAdvancedGolemType(NBTTagCompound base, AdditionalGolemType type) {
        NBTTagCompound compound;
        if(base.hasKey(Gadomancy.MODID)) {
            compound = base.getCompoundTag(Gadomancy.MODID);
        } else {
            compound = new NBTTagCompound();
            base.setTag(Gadomancy.MODID, compound);
        }
        compound.setString(TYPE_TAG, type.getEnumEntry().name());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if(compound.hasKey("GolemType")) {
            AdditionalGolemType type = readAdvancedGolemType(golem.getEntityData());
            if(type != null) {
                byte lastType = compound.getByte("GolemType");
                compound.setTag("GolemType", new OverrideNBTTagByte(lastType, (byte)type.getEnumEntry().ordinal()));

                health = compound.getFloat("HealF") * -1;
                updateHealth = true;
            }
        }

        isSitting = compound.getBoolean(Gadomancy.MODID + ":sitting");

        updateGolemCore();
    }

    public void updateGolemCore() {
        if(!golem.worldObj.isRemote) {
            if(NBTHelper.hasPersistentData(golem)) {
                NBTTagCompound persistent = NBTHelper.getPersistentData(golem);
                if(persistent.hasKey("Core")) {
                    for(AdditionalGolemCore core : GadomancyApi.getAdditionalGolemCores()) {
                        if(core.getName().equals(persistent.getString("Core"))) {
                            golem.getDataWatcher().updateObject(ModConfig.golemDatawatcherId, core.getName());
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setWrapperIfNeeded() {
        boolean needsUpdate = false;
        if(taskWrapper == null) {
            taskWrapper = new EntityAITasksWrapper(golem, golem.tasks, true);
            golem.tasks = taskWrapper;

            needsUpdate = true;
        }

        if(targetWrapper == null) {
            targetWrapper = new EntityAITasksWrapper(golem, golem.targetTasks, false);
            golem.targetTasks = targetWrapper;

            needsUpdate = true;
        }

        if(needsUpdate) {
            updateGolem();
        }
    }

    public void updateGolem() {
        taskWrapper.unlock();
        targetWrapper.unlock();

        golem.setupGolem();
        golem.setupGolemInventory();

        AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
        if(core != null) {
            taskWrapper.taskEntries.clear();
            targetWrapper.taskEntries.clear();

            core.setupGolem(golem);

            taskWrapper.lock();
            targetWrapper.lock();
        }
    }

    private static AdditionalGolemType readAdvancedGolemType(NBTTagCompound base) {
        if(base.hasKey(Gadomancy.MODID)) {
            NBTTagCompound compound = base.getCompoundTag(Gadomancy.MODID);
            if(compound.hasKey(TYPE_TAG)) {
                return GadomancyApi.getAdditionalGolemType(compound.getString(TYPE_TAG));
            }
        }
        return null;
    }

    @Override
    public void init(Entity entity, World world) {

    }
}
