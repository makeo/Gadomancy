package makeo.gadomancy.common.data;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

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

    public static int golemDatawatcherId = 29;
    public static int dimOuterId = -73;

    private ModConfig() {}

    public static void init(File file) {
        config = new Configuration(file);

        config.load();

        loadFromConfig();

        config.save();
    }

    private static void loadFromConfig() {
        golemDatawatcherId = config.getInt("datawatcherId", "golem", 29, 0, 31, "Do not edit unless you know what are you doing!");
        dimOuterId = config.getInt("dimOuterId", "dimension", -73, Integer.MIN_VALUE, Integer.MAX_VALUE, "Dimension Id for the eldrich mazes accessed via Node Manipulator");
    }
}
