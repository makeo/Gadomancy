package makeo.gadomancy.common;

import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.common.api.DefaultApiHandler;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.data.config.ModData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.common.Thaumcraft;

/**
 * HellFirePvP@Admin
 * Date: 20.04.2016 / 17:50
 * on Gadomancy_1_8
 * Gadomancy
 */
@Mod(modid = Gadomancy.MODID, name = Gadomancy.NAME, version = Gadomancy.VERSION, dependencies="required-after:Thaumcraft@[5.2,)")
public class Gadomancy {

    //TODO check and change AccessTransformer config. Fields renamed&changed&removed

    public static final String MODID = "gadomancy";
    public static final String NAME = "Gadomancy";

    public static final String VERSION = "1.0.6.1";

    private static final String PROXY_CLIENT = "makeo.gadomancy.client.ClientProxy";
    private static final String PROXY_SERVER = "makeo.gadomancy.common.CommonProxy";

    @Mod.Instance(value = Gadomancy.MODID)
    public static Gadomancy instance;

    @SidedProxy(clientSide = PROXY_CLIENT, serverSide = PROXY_SERVER)
    public static CommonProxy proxy;

    public static Logger log = LogManager.getLogger("Gadomancy");
    private static ModData modData = null;

    public static ModData getModData() {
        return modData;
    }

    public static void loadModData() {
        modData = new ModData("data");
        modData.load();
    }

    public static void unloadModData() {
        if(modData != null) {
            modData.save();
            modData = null;
        }
    }

    @Mod.EventHandler
    public void onConstruct(FMLConstructionEvent event) {
        GadomancyApi.setInstance(new DefaultApiHandler());
        proxy.onConstruct();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        event.getModMetadata().version = VERSION;
        ModConfig.init(event.getSuggestedConfigurationFile());
        proxy.preInitalize();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initalize();

        //FMLInterModComms.sendMessage(Thaumcraft.MODID, "dimensionBlacklist", ModConfig.dimOuterId + ":0");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInitalize();
    }
}
