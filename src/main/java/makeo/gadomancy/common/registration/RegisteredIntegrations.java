package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.integration.*;
import makeo.gadomancy.common.integration.mystcraft.IntegrationMystcraft;
import makeo.gadomancy.common.utils.Injector;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 09.07.2015 16:00
 */
public class RegisteredIntegrations {
    public static IntegrationMorph morph;
    public static IntegrationThaumicExploration thaumicExploration;
    public static IntegrationAutomagy automagy;
    public static IntegrationNEI nei;
    public static IntegrationMystcraft mystcraft;
    public static IntegrationThaumicTinkerer thaumicTinkerer;

    private RegisteredIntegrations() {}

    public static void init() {
        morph = registerIndependent(IntegrationMorph.class);
        thaumicExploration = registerIndependent(IntegrationThaumicExploration.class);
        automagy = registerIndependent(IntegrationAutomagy.class);
        nei = registerIndependent(IntegrationNEI.class);
        mystcraft = registerIndependent(IntegrationMystcraft.class);
        thaumicTinkerer = registerIndependent(IntegrationThaumicTinkerer.class);

        registerDependent("ThaumicHorizons", "makeo.gadomancy.common.integration.thaumichorizions.IntegrationThaumicHorizions");
        registerDependent("Waila", "makeo.gadomancy.common.integration.waila.IntegrationWaila");
    }

    private static void registerDependent(String modId, String clazz) {
        if(!Loader.isModLoaded(modId)) {
            return;
        }

        Object integration;
        try {
            integration = Injector.getClass(clazz).newInstance();
        } catch (Throwable e) {//InstantiationException | IllegalAccessException
            return;
        }

        if(integration instanceof IntegrationMod) {
            ((IntegrationMod) integration).init();
        }
    }

    private static  <T extends IntegrationMod> T registerIndependent(Class<T> clazz) {
        T integration;
        try {
            integration = clazz.newInstance();
        } catch (Exception e) {//InstantiationException | IllegalAccessException
            return null;
        }

        integration.init();
        if(integration.isPresent()) {
            Gadomancy.log.info("Initialized hook for mod \"" + integration.getModId() + "\"!");
        }
        return integration;
    }
}