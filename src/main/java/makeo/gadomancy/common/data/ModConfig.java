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

    private ModConfig() {}

    public static void init(File file) {
        config = new Configuration(file);

        config.load();

        getGolemDatawatcherId();

        config.save();
    }

    private static int golemDatawatcherId = -1;
    public static int getGolemDatawatcherId() {
        if(golemDatawatcherId < 0) {
            golemDatawatcherId = config.getInt("datawatcherId", "golem", 29, 0, 31, "Do not edit unless you know what are you doing!");
        }
        return golemDatawatcherId;
    }
}
