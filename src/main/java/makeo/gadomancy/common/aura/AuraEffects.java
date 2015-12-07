package makeo.gadomancy.common.aura;

import makeo.gadomancy.api.AuraEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import thaumcraft.api.aspects.Aspect;

import static makeo.gadomancy.common.utils.MiscUtils.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 29.11.2015 19:09
 */
public class AuraEffects {

    //Potion amplifiers start with 0 == lvl 1!

    public static final AuraEffect AER = new PotionDistributionEffect(Potion.moveSpeed, 4, 6, ticksForMinutes(6), 1).register(Aspect.AIR);
    public static final AuraEffect AQUA = new PotionDistributionEffect(Potion.waterBreathing, 4, 10, ticksForMinutes(10), 0).register(Aspect.WATER);
    public static final AuraEffect TERRA = new PotionDistributionEffect(Potion.resistance, 4, 8, ticksForMinutes(5), 0).register(Aspect.EARTH);
    public static final AuraEffect ORDO = new PotionDistributionEffect(Potion.regeneration, 4, 6, ticksForMinutes(3), 0).register(Aspect.ORDER);
    public static final AuraEffect PERDITIO = new PotionDistributionEffect(Potion.weakness, 4, 8, ticksForMinutes(3), 0).register(Aspect.ENTROPY);

    public static final AuraEffect TELUM = new PotionDistributionEffect(Potion.damageBoost, 4, 6, ticksForMinutes(6), 2).register(Aspect.WEAPON);
    public static final AuraEffect TUTAMEN = new PotionDistributionEffect(Potion.resistance, 4, 6, ticksForMinutes(6), 1).register(Aspect.ARMOR);
    public static final AuraEffect MOTUS = new PotionDistributionEffect(Potion.moveSpeed, 4, 6, ticksForMinutes(2), 2).register(Aspect.MOTION); //Fast speed for short duration, long charge time
    public static final AuraEffect ITER = new PotionDistributionEffect(Potion.moveSpeed, 2, 10, ticksForMinutes(15), 0).register(Aspect.TRAVEL); //Slower speed for long duration, fast charge time
    public static final AuraEffect FAMES = new PotionDistributionEffect(Potion.hunger, 4, 6, ticksForMinutes(2), 0).register(Aspect.HUNGER);
    public static final AuraEffect LUX = new PotionDistributionEffect(Potion.nightVision, 2, 8, ticksForMinutes(8), 0).register(Aspect.LIGHT);
    public static final AuraEffect VOLATUS = new PotionDistributionEffect(Potion.jump, 2, 5, ticksForMinutes(4), 0).register(Aspect.FLIGHT);
    public static final AuraEffect POTENTIA = new PotionDistributionEffect(Potion.damageBoost, 4, 6, ticksForMinutes(4), 0).register(Aspect.ENERGY);

    public static final AuraEffect SANO = new PotionDistributionEffect(Potion.regeneration, 8, 10, ticksForMinutes(5), 1).register(Aspect.HEAL);

    public static class PotionDistributionEffect extends AuraEffect {

        private Potion potion;
        private int tickInterval, addedDuration, durationCap, amplifier;

        public PotionDistributionEffect(Potion potion, int tickInterval, int addedDuration, int durationCap, int amplifier) {
            this.potion = potion;
            this.tickInterval = tickInterval;
            this.addedDuration = addedDuration;
            this.durationCap = durationCap;
            this.amplifier = amplifier;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase;
        }

        @Override
        public void applyEffect(Entity e) {
            if(e == null || !(e instanceof EntityLivingBase)) return;

            EntityLivingBase living = (EntityLivingBase) e;
            int activeDuration = 0;
            if(living.isPotionActive(potion)) {
                PotionEffect effect = living.getActivePotionEffect(potion);
                activeDuration = effect.getDuration();
            }

            boolean canAdd = (durationCap - activeDuration) >= addedDuration;
            if(canAdd) {
                living.addPotionEffect(new PotionEffect(potion.getId(), activeDuration + addedDuration, amplifier, true));
            }
        }

        @Override
        public int getTickInterval() {
            return tickInterval;
        }
    }

}
