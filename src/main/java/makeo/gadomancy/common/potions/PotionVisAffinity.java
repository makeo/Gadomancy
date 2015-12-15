package makeo.gadomancy.common.potions;

import thaumcraft.api.aspects.Aspect;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 15.12.2015 19:06
 */
public class PotionVisAffinity extends PotionCustomTexture {

    public PotionVisAffinity(int id) {
        super(id, false, Aspect.MAGIC.getColor(), Aspect.MAGIC.getImage());
        setPotionName("potion.visdiscount");
    }

}
