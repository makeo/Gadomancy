package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.FMLLog;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.potions.*;
import makeo.gadomancy.common.potions.PotionEldritch;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.potion.Potion;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 31.10.2015 23:09
 */
public class RegisteredPotions {

    public static PotionMiningLuck POTION_LUCK;
    public static PotionBuffGolem BUFF_GOLEM;
    public static PotionAchromatic ACHROMATIC;
    public static PotionEldritch ELDRITCH;
    public static PotionVisAffinity VIS_DISCOUNT;

    public static void init() {
        POTION_LUCK = registerPotion(PotionMiningLuck.class);
        BUFF_GOLEM = registerPotion(PotionBuffGolem.class);
        ACHROMATIC = registerPotion(PotionAchromatic.class);
        ELDRITCH = registerPotion(PotionEldritch.class);
        VIS_DISCOUNT = registerPotion(PotionVisAffinity.class);

        ModConfig.save();
    }

    private static <T extends Potion> T registerPotion(Class<T> potionClass) {
        int id = ModConfig.loadPotionId(potionClass.getSimpleName());
        if(id == -1) {
            id = Potion.potionTypes.length;
        }

        if(id > 127) {
            FMLLog.warning("The potion id '" + id + "' of potion '" + Gadomancy.NAME + ":" + potionClass.getSimpleName() + "' is bigger then 127 this might cause errors. Please consider changing the config.");
        }

        if(id >= Potion.potionTypes.length) {
            Potion[] potions = new Potion[Potion.potionTypes.length + 1];
            System.arraycopy(Potion.potionTypes, 0, potions, 0, Potion.potionTypes.length);
            Potion.potionTypes = potions;
        } else if(Potion.potionTypes[id] != null) {
            Potion conflict = Potion.potionTypes[id];
            throw new RuntimeException("Potion id conflict! Do not report this bug you just have to change the configuration files. Failed to register potion '"
                    + Gadomancy.NAME + ":" + potionClass.getSimpleName() + "' with id '" + id
                    + "'. Another potion with this id already exists: " + conflict.getName() + " (as " + conflict.getClass().getName() + ")");
        }

        return new Injector(potionClass).invokeConstructor(int.class, id);
    }
}
