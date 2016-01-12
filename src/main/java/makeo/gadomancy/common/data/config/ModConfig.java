package makeo.gadomancy.common.data.config;

import makeo.gadomancy.common.registration.RegisteredEnchantments;
import makeo.gadomancy.common.registration.RegisteredPotions;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import thaumcraft.api.aspects.Aspect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 15.09.2015 18:25
 */
public class ModConfig {

    private static Configuration config;

    @Sync
    public static int entityNoClipItemDatawatcherMasterId = 19;
    @Sync
    public static int entityNoClipItemDatawatcherFixedId = 20;

    @Sync
    public static int entityAuraCoreDatawatcherAspectsId = 21;

    @Sync
    public static int golemDatawatcherId = 29;
    @Sync
    public static int dimOuterId = -73;

    @Sync
    public static String[] blacklistAuraEffects = new String[0];

    public static boolean doLightCalculations = true;
    public static int maxMazeCount = -1;
    public static int renderParticleDistance = 100;

    public static boolean enableAdditionalNodeTypes;

    //Skyblock stuff
    public static boolean ancientStoneRecipes = false;

    private ModConfig() {}

    public static void init(File file) {
        config = new Configuration(file);

        config.load();

        loadFromConfig();

        config.save();
    }

    private static void loadFromConfig() {
        //Datawatcher entity stuff
        entityNoClipItemDatawatcherMasterId = config.getInt("itemNoClipDatawatcherMasterId", "entities", 19, 0, 31, "Do not edit unless you know what are you doing! - Datawatcher ID of the Master-Coordinates for EntityNoClipItem");
        entityNoClipItemDatawatcherFixedId = config.getInt("itemNoClipDatawatcherFixId", "entities", 20, 0, 31, "Do not edit unless you know what are you doing! - Datawatcher ID of the Fixed-Coordinates for EntityNoClipItem");

        entityAuraCoreDatawatcherAspectsId = config.getInt("auraCoreDatawatcherAuraId", "entities", 21, 0, 31, "Do not edit unless you know what are you doing! - Datawatcher ID of the Auracore's current aspects");

        golemDatawatcherId = config.getInt("datawatcherId", "golem", 29, 0, 31, "Do not edit unless you know what are you doing!");

        //Dimension stuff
        dimOuterId = config.getInt("dimOuterId", "dimension", -73, Integer.MIN_VALUE, Integer.MAX_VALUE, "Dimension Id for the eldrich mazes accessed via Node Manipulator");
        doLightCalculations = config.getBoolean("calculateEldritchLight", "dimension", true, "TRUE = Calculating Light values for the Gadomancy-Eldritch Mazes; FALSE = No calculation, but some Light Bugs - Calculating the Light takes ~2 seconds -> Can be measured when trying to enter the eldritch mazed via Gadomancy Eldritch portal.");
        maxMazeCount = config.getInt("maxMazeCount", "dimension", -1, -1, Integer.MAX_VALUE, "Defines how many Eldritch mazes may exist at the same time using the Gadomancy Eldritch ritual. (-1 = infinite) Note that 1 maze = 1 player; Once the player finishes the maze, the maze closes itself and teleports the player out.");

        //Skyblock stuff
        ancientStoneRecipes = config.getBoolean("ancientStoneRecipes", "skyblock", false, "TRUE = Adds recipes for Ancient Stone and Ancient Stone Pedestal (This may be usefull for severs and skyblock packs to craft the Node Manipulator and get more primodial pearls). You have to change this client- and server-side!");

        //General stuff
        renderParticleDistance = config.getInt("particleRenderDistance", "general", 100, 5, 1000, "Defines, how close a player has to be towards the particle origin to see the particles created by it.");
        enableAdditionalNodeTypes = config.getBoolean("enableAdditionalNodeTypes", "general", true, "Enables our custom node types. This might solve some compatibility issues (e.g. WitchingGadgets). You have to change this client- and server-side! Only change when you experience issues with special mods' features ONLY not working when using the mod together with Gadomancy.");
        String listOfAspects = config.getString("auraPylonBlacklist", "general", "", "Write a list of aspects (e.g. aura,aer) here that should not be active/obtainable with the aura pylon. Multiple aspects can be seperated with ','. Leave it empty to blacklist nothing");
        blacklistAuraEffects = refactorAspects(listOfAspects);

        config.addCustomCategoryComment("potions", "Use the following if you have problems with conflicting potion ids. If the entry is set to '-1' it will try to automatically find the lowest free potion id.");
        config.addCustomCategoryComment("enchantments", "Use the following if you have problems with conflicting enchantment ids. If the entry is set to '-1' it will try to automatically find the lowest free enchantment id.");

        RegisteredPotions.createConfigEntries();
        RegisteredEnchantments.createConfigEntries();
    }

    private static String[] refactorAspects(String listOfAspects) {
        List<String> aspects = new ArrayList<String>();
        if(listOfAspects != null && listOfAspects.length() > 0) {
            String[] aspectTags = listOfAspects.split(",");
            for (String s : aspectTags) {
                if(Aspect.getAspect(s) != null) aspects.add(s);
            }
        }
        return aspects.toArray(new String[aspects.size()]);
    }

    public static int loadPotionId(String name) {
        return loadId("potions", name);
    }

    public static int loadEnchantmentId(String name) {
        return loadId("enchantments", name);
    }

    public static int loadId(String category, String name) {
        Property prop = config.get(category, name, -1);
        prop.setLanguageKey(name);
        int result = prop.getInt(-1);
        return result < -1 ? -1 : result;
    }

    public static void save() {
        config.save();
    }
}
