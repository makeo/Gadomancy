package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.potions.PotionAchromatic;
import makeo.gadomancy.common.potions.PotionBuffGolem;
import makeo.gadomancy.common.potions.PotionLetsGargamelItUp;
import makeo.gadomancy.common.potions.PotionMiningLuck;
import makeo.gadomancy.common.potions.PotionVisAffinity;
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
    public static PotionLetsGargamelItUp ELDRITCH;
    public static PotionVisAffinity VIS_DISCOUNT;

    public static void init() {
        POTION_LUCK = registerPotion(PotionMiningLuck.class);
        BUFF_GOLEM = registerPotion(PotionBuffGolem.class);
        ACHROMATIC = registerPotion(PotionAchromatic.class);
        ELDRITCH = registerPotion(PotionLetsGargamelItUp.class);
        VIS_DISCOUNT = registerPotion(PotionVisAffinity.class);
        System.out.println(VIS_DISCOUNT);
    }

    private static <T extends Potion> T registerPotion(Class<T> potionClass) {
        int id = Potion.potionTypes.length;
        Potion[] potions = new Potion[Potion.potionTypes.length + 1];
        System.arraycopy(Potion.potionTypes, 0, potions, 0, Potion.potionTypes.length);
        Potion.potionTypes = potions;

        return new Injector(potionClass).invokeConstructor(int.class, id);
    }
}
