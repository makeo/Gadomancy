package makeo.gadomancy.common.registration;

import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.golems.cores.BodyguardGolemCore;
import makeo.gadomancy.common.entities.golems.cores.BreakGolemCore;
import makeo.gadomancy.common.entities.golems.types.ObsidianGolemType;
import makeo.gadomancy.common.entities.golems.types.SilverwoodGolemType;
import makeo.gadomancy.common.entities.golems.upgrades.GolemUpgrade;
import makeo.gadomancy.common.entities.golems.upgrades.GolemUpgradeRunicShield;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 22.06.2015 02:58
 */
public class RegisteredGolemStuff {
    private RegisteredGolemStuff() {}

    public static final List<GolemUpgrade> UPGRADES = new ArrayList<GolemUpgrade>();
    public static GolemUpgradeRunicShield upgradeRunicShield;

    public static SilverwoodGolemType typeSilverwood;
    //public static ObsidianGolemType typeObsidian;

    public static AdditionalGolemCore breakCore;
    public static AdditionalGolemCore bodyguardCore;

    public static void init() {
        typeSilverwood = registerGolemType("SILVERWOOD", new SilverwoodGolemType());
        //typeObsidian = registerGolemType("OBSIDIAN", new ObsidianGolemType());

        breakCore = registerGolemCore("breakCore", new BreakGolemCore());
        bodyguardCore = registerGolemCore("bodyguardCore", new BodyguardGolemCore());

        upgradeRunicShield = registerGolemUpgrade(new GolemUpgradeRunicShield());
    }

    private static <T extends AdditionalGolemType> T registerGolemType(String name, T type) {
        GadomancyApi.registerAdditionalGolemType(name, Gadomancy.MODID, type);
        return type;
    }

    private static <T extends AdditionalGolemCore> T registerGolemCore(String name, T core) {
        GadomancyApi.registerAdditionalGolemCore(name, core);
        return core;
    }

    private static <T extends GolemUpgrade> T registerGolemUpgrade(T upgrade) {
        UPGRADES.add(upgrade);
        return upgrade;
    }
}
