package makeo.gadomancy.common.potions;

import thaumcraft.api.aspects.Aspect;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 11.12.2015 17:32
 */
public class PotionAchromatic extends PotionCustomTexture {
    public PotionAchromatic(int id) {
        super(id, false, Aspect.CRYSTAL.getColor(), Aspect.CRYSTAL.getImage());
        setPotionName("potion.achromatic");
    }
}
