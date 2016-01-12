package makeo.gadomancy.common.potions;

import thaumcraft.api.aspects.Aspect;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 07.12.2015 20:05
 */
public class PotionMiningLuck extends PotionCustomTexture {

    public PotionMiningLuck(int id) {
        super(id, false, Aspect.GREED.getColor(), Aspect.GREED.getImage());
        setPotionName("potion.luck");
    }

}
