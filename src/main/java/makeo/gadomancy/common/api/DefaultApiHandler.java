package makeo.gadomancy.common.api;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.api.AuraEffect;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.api.internal.IApiHandler;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.entities.golems.ItemAdditionalGolemPlacer;
import makeo.gadomancy.common.entities.golems.nbt.ExtendedGolemProperties;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.aura.AuraEffectHandler;
import makeo.gadomancy.common.utils.GolemEnumHelper;
import makeo.gadomancy.common.utils.NBTHelper;
import makeo.gadomancy.common.utils.StringHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.entities.golems.EntityGolemBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 14:38
 */
public class DefaultApiHandler implements IApiHandler {

    private static final Logger log = LogManager.getLogger("Gadomancy_API");

    private static Map<String, AdditionalGolemType> additionalGolemTypes = new HashMap<String, AdditionalGolemType>();

    @Override
    public boolean registerAdditionalGolemType(String name, String modId, AdditionalGolemType newType) {
        String uniqueName = name.toUpperCase();
        if(!additionalGolemTypes.containsKey(uniqueName)) {
            GolemEnumHelper.addGolemType(uniqueName, newType);

            ItemAdditionalGolemPlacer placerItem = new ItemAdditionalGolemPlacer(newType);
            GameRegistry.registerItem(placerItem, "item" + StringHelper.firstToUpper(name.toLowerCase()) + "GolemPlacer");
            newType.setModId(modId);
            newType.setPlacerItem(placerItem);

            additionalGolemTypes.put(uniqueName, newType);
            return true;
        }
        return false;
    }

    @Override
    public AdditionalGolemType getAdditionalGolemType(String name) {
        return additionalGolemTypes.get(name.toUpperCase());
    }

    @Override
    public List<AdditionalGolemType> getAdditionalGolemTypes() {
        return new ArrayList<AdditionalGolemType>(additionalGolemTypes.values());
    }

    private static Map<String, AdditionalGolemCore> additionalGolemCores = new HashMap<String, AdditionalGolemCore>();

    @Override
    public AdditionalGolemCore getAdditionalGolemCore(EntityGolemBase golem) {
        String coreName = golem.getDataWatcher().getWatchableObjectString(ModConfig.golemDatawatcherId);
        if(!coreName.isEmpty()) {
            return additionalGolemCores.get(coreName);
        }
        return null;
    }

    @Override
    public AdditionalGolemCore getAdditionalGolemCore(ItemStack placer) {
        if(NBTHelper.hasPersistentData(placer)) {
            NBTTagCompound persistent = NBTHelper.getPersistentData(placer);
            if(persistent.hasKey("Core")) {
                return additionalGolemCores.get(persistent.getString("Core"));
            }
        }
        return null;
    }

    @Override
    public boolean registerAdditionalGolemCore(String name, AdditionalGolemCore core) {
        if(!additionalGolemCores.containsKey(name)) {
            core.setName(name);
            additionalGolemCores.put(name, core);
            return true;
        }
        return false;
    }

    @Override
    public void setAdditionalGolemCore(EntityGolemBase golem, AdditionalGolemCore core) {
        String coreName = core == null ? "" : core.getName();

        golem.setCore(core == null ? -1 : core.getBaseCore());
        golem.getDataWatcher().updateObject(ModConfig.golemDatawatcherId, coreName);

        if(!golem.worldObj.isRemote) {
            NBTHelper.getPersistentData(golem).setString("Core", coreName);
            ((ExtendedGolemProperties)golem.getExtendedProperties(Gadomancy.MODID)).updateGolem();
        } else {
            golem.setupGolem();
            golem.setupGolemInventory();
        }

        golem.worldObj.setEntityState(golem, (byte)7);
    }

    @Override
    public List<AdditionalGolemCore> getAdditionalGolemCores() {
        return new ArrayList<AdditionalGolemCore>(additionalGolemCores.values());
    }

    @Override
    public void registerClawClickBehavior(ClickBehavior clickBehavior) {
        RegisteredBlocks.registerClawClickBehavior(clickBehavior);
    }

    @Override
    public void registerAdditionalAuraEffect(Aspect aspect, AuraEffect effect) {
        if(aspect == null || effect == null) return;
        if(AuraEffectHandler.registeredEffects.containsKey(aspect)) {
            log.warn("AuraEffect for '" + aspect.getTag() + "' is already registered!");
        } else {
            AuraEffectHandler.registeredEffects.put(aspect, effect);
        }
    }
}
