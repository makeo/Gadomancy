package makeo.gadomancy.api.internal;

import makeo.gadomancy.api.AuraEffect;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.entities.golems.EntityGolemBase;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 14.10.2015 14:28
 */
public interface IApiHandler {
    boolean registerAdditionalGolemType(String name, String modId, AdditionalGolemType newType);

    AdditionalGolemType getAdditionalGolemType(String name);

    List<AdditionalGolemType> getAdditionalGolemTypes();

    AdditionalGolemCore getAdditionalGolemCore(EntityGolemBase golem);

    AdditionalGolemCore getAdditionalGolemCore(ItemStack placer);

    boolean registerAdditionalGolemCore(String name, AdditionalGolemCore core);

    void setAdditionalGolemCore(EntityGolemBase golem, AdditionalGolemCore core);

    List<AdditionalGolemCore> getAdditionalGolemCores();

    void registerClawClickBehavior(ClickBehavior clickBehavior);

    void registerAdditionalAuraEffect(Aspect aspect, AuraEffect effect);
}
