package makeo.gadomancy.common.potions;

import makeo.gadomancy.common.utils.ColorHelper;
import net.minecraft.potion.Potion;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 31.10.2015 23:01
 */
public class PotionInactiveGolem extends Potion {

    //TODO makeoo ?
    public PotionInactiveGolem() {
        super(null, false, ColorHelper.toHex(Color.WHITE));
    }

    /*@Override
    public void performEffect(EntityLivingBase entity, int par2) {
        if(entity instanceof EntityThaumcraftGolem) {
            ((EntityThaumcraftGolem) entity). = true;
        }
    }*/

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }
}
