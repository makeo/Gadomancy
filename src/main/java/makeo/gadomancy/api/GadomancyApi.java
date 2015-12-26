package makeo.gadomancy.api;

import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.api.internal.IApiHandler;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EnumGolemType;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 26.07.2015 19:00
 */
public abstract class GadomancyApi {
    private static IApiHandler handler;
    public static void setInstance(IApiHandler handler) {
        GadomancyApi.handler = handler;
    }

    @Deprecated
    public static boolean registerAdditionalGolemType(String name, String modId, AdditionalGolemType newType){
        return handler.registerAdditionalGolemType(name, modId, newType);
    }

    public static AdditionalGolemType getAdditionalGolemType(String name) {
        return handler.getAdditionalGolemType(name);
    }

    public static AdditionalGolemType getAdditionalGolemType(EnumGolemType type) {
        return getAdditionalGolemType(type.name());
    }

    public static List<AdditionalGolemType> getAdditionalGolemTypes() {
        return handler.getAdditionalGolemTypes();
    }

    public static boolean isAdditionalGolemType(String name) {
        return getAdditionalGolemType(name) != null;
    }

    public static boolean isAdditionalGolemType(EnumGolemType type) {
        return isAdditionalGolemType(type.name());
    }

    public static AdditionalGolemCore getAdditionalGolemCore(EntityGolemBase golem) {
        return handler.getAdditionalGolemCore(golem);
    }

    public static AdditionalGolemCore getAdditionalGolemCore(ItemStack placer) {
        return handler.getAdditionalGolemCore(placer);
    }

    public static void setAdditionalGolemCore(EntityGolemBase golem, AdditionalGolemCore core) {
        handler.setAdditionalGolemCore(golem, core);
    }

    public static boolean registerAdditionalGolemCore(String name, AdditionalGolemCore core) {
        return handler.registerAdditionalGolemCore(name, core);
    }

    public static void registerAdditionalAuraEffect(Aspect aspect, AuraEffect effect) {
        handler.registerAdditionalAuraEffect(aspect, effect);
    }

    public static List<AdditionalGolemCore> getAdditionalGolemCores() {
        return handler.getAdditionalGolemCores();
    }

    public static void registerClawClickBehavior(ClickBehavior clickBehavior) {
        handler.registerClawClickBehavior(clickBehavior);
    }
}
