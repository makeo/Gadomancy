package makeo.gadomancy.common.aura;

import makeo.gadomancy.api.AuraEffect;
import makeo.gadomancy.common.events.EventHandlerEntity;
import makeo.gadomancy.common.registration.RegisteredPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

import java.util.Random;

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

    //Std. potion effects
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
    public static final AuraEffect SENSUS = new PotionDistributionEffect(Potion.nightVision, 2, 8, ticksForMinutes(8), 0).register(Aspect.SENSES);
    public static final AuraEffect VOLATUS = new PotionDistributionEffect(Potion.jump, 2, 5, ticksForMinutes(4), 0).register(Aspect.FLIGHT);
    public static final AuraEffect POTENTIA = new PotionDistributionEffect(Potion.damageBoost, 4, 6, ticksForMinutes(4), 0).register(Aspect.ENERGY);
    public static final AuraEffect TENEBRAE = new PotionDistributionEffect(Potion.blindness, 4, 6, ticksForMinutes(1), 0).register(Aspect.DARKNESS);
    public static final AuraEffect SANO = new PotionDistributionEffect(Potion.regeneration, 8, 10, ticksForMinutes(5), 1).register(Aspect.HEAL);
    public static final AuraEffect MORTUUS = new PotionDistributionEffect(Potion.wither, 4, 6, ticksForMinutes(3), 1).register(Aspect.DEATH);
    public static final AuraEffect HUMANUS = new PotionDistributionEffect(Potion.field_76434_w, 2, 5, ticksForMinutes(4), 1).register(Aspect.MAN);
    public static final AuraEffect INSTRUMENTUM = new PotionDistributionEffect(Potion.digSpeed, 2, 7, ticksForMinutes(6), 0).register(Aspect.TOOL);
    public static final AuraEffect PERFODIO = new PotionDistributionEffect(Potion.digSpeed, 2, 7, ticksForMinutes(5), 1).register(Aspect.MINE);
    public static final AuraEffect VENENUM = new PotionDistributionEffect(Potion.poison, 2, 6, ticksForMinutes(1), 0).register(Aspect.POISON);
    public static final AuraEffect VINCULUM = new PotionDistributionEffect(Potion.moveSlowdown, 4, 6, ticksForMinutes(3), 0).register(Aspect.TRAP);

    //Special effects..
    public static final AuraEffect LUCRUM = new PotionDistributionEffect(RegisteredPotions.POTION_LUCK, 4, 6, ticksForMinutes(5), 1).register(Aspect.GREED); //Mining luck (Adding fortune)
    public static final AuraEffect VICTUS = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e != null && !e.isDead && ((e instanceof EntityAgeable && ((EntityAgeable) e).getGrowingAge() < 0) || e instanceof EntityPlayer);
        }

        @Override
        public void doEntityEffect(ChunkCoordinates originTile, Entity e) {
            if(e instanceof EntityPlayer) return;
            EntityAgeable ageable = (EntityAgeable) e;
            if(ageable.worldObj.rand.nextInt(20) == 0) ageable.setGrowingAge(0); //setAdult
        }
    }.register(Aspect.LIFE);
    public static final AuraEffect PANNUS = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e != null && !e.isDead && ((e instanceof EntitySheep && ((EntitySheep) e).getSheared()) || e instanceof EntityPlayer);
        }

        @Override
        public void doEntityEffect(ChunkCoordinates originTile, Entity e) {
            if(e instanceof EntityPlayer) return;
            EntitySheep sheep = (EntitySheep) e;
            if(sheep.worldObj.rand.nextInt(20) == 0) sheep.setSheared(false);
        }

        @Override
        public int getTickInterval() {
            return 20;
        }
    }.register(Aspect.CLOTH);
    public static final AuraEffect LUX = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return true;
        }

        @Override
        public void doEntityEffect(ChunkCoordinates originTile, Entity e) {
            if(!EventHandlerEntity.registeredLuxPylons.contains(originTile))
                EventHandlerEntity.registeredLuxPylons.add(originTile);
        }

        @Override
        public int getTickInterval() {
            return 4;
        }

        @Override
        public double getRange() {
            return 64;
        }
    }.register(Aspect.LIGHT);
    public static final AuraEffect TEMPESTAS = new AuraEffect.BlockAuraEffect() {
        @Override
        public int getBlockCount(Random random) {
            return random.nextInt(10) == 0 ? 1 : 0;
        }

        @Override
        public void doBlockEffect(ChunkCoordinates originTile, ChunkCoordinates selectedBlock, World world) {
            int highestY = world.getTopSolidOrLiquidBlock(selectedBlock.posX, selectedBlock.posZ);
            EntityLightningBolt entityLightning = new EntityLightningBolt(world, selectedBlock.posX + 0.5, highestY, selectedBlock.posZ + 0.5);
            world.addWeatherEffect(entityLightning);
        }
    }.register(Aspect.WEATHER);

    public static class PotionDistributionEffect extends AuraEffect.EntityAuraEffect {

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
        public void doEntityEffect(ChunkCoordinates originTile, Entity e) {
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
