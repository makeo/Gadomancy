package makeo.gadomancy.common.potions;

import thaumcraft.api.aspects.Aspect;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 11.12.2015 20:50
 */
public class PotionEldritch extends PotionCustomTexture {
    public PotionEldritch(int id) {
        super(id, false, Aspect.ELDRITCH.getColor(), Aspect.ELDRITCH.getImage());
        setPotionName("potion.eldritch");
    }
}
