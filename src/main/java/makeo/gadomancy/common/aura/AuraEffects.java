package makeo.gadomancy.common.aura;

import makeo.gadomancy.api.AuraEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 29.11.2015 19:09
 */
public class AuraEffects {

    //TODO create and register in AuraEffectHandler

    public static final AuraEffect DUMMY_SANO = new AuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase;
        }

        @Override
        public void applyEffect(Entity e) {
            e.motionY = 0.6;
        }

        @Override
        public int getTickInterval() {
            return 2;
        }
    };

}
