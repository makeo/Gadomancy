package makeo.gadomancy.common.aura;

import makeo.gadomancy.api.AuraEffect;
import makeo.gadomancy.common.event.EventHandlerEntity;
import makeo.gadomancy.common.registration.RegisteredIntegrations;
import makeo.gadomancy.common.registration.RegisteredPotions;
import makeo.gadomancy.common.utils.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.world.taint.BlockTaintFibre;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.items.wands.ItemWand;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.biomes.BiomeHandler;

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
    private static final ItemStack[][] ITEMS_SOUL = {{new ItemStack(Items.bow), new ItemStack(Items.wooden_sword), new ItemStack(Items.stone_sword), new ItemStack(Items.iron_sword), new ItemStack(Items.stone_axe), new ItemStack(Items.iron_axe)},{new ItemStack(Items.leather_boots), new ItemStack(Items.chainmail_boots), new ItemStack(Items.iron_boots)},{new ItemStack(Items.leather_leggings), new ItemStack(Items.chainmail_leggings), new ItemStack(Items.iron_leggings)},{new ItemStack(Items.leather_chestplate), new ItemStack(Items.chainmail_chestplate), new ItemStack(Items.iron_chestplate)},{new ItemStack(Blocks.lit_pumpkin), new ItemStack(Blocks.pumpkin)}};

    //Potion amplifiers start with 0 == lvl 1!

    //Std. potion effects
    public static final AuraEffect AER = new PotionDistributionEffect(Potion.moveSpeed, 4, 6, ticksForMinutes(6), 1).register(Aspect.AIR);
    public static final AuraEffect AQUA = new PotionDistributionEffect(Potion.waterBreathing, 4, 10, ticksForMinutes(10), 0).register(Aspect.WATER);
    public static final AuraEffect TERRA = new PotionDistributionEffect(Potion.resistance, 4, 8, ticksForMinutes(5), 0).register(Aspect.EARTH);
    public static final AuraEffect ORDO = new PotionDistributionEffect(Potion.regeneration, 4, 6, ticksForMinutes(3), 0).register(Aspect.ORDER);
    public static final AuraEffect PERDITIO = new PotionDistributionEffect(Potion.weakness, 4, 8, ticksForMinutes(3), 0).register(Aspect.ENTROPY);
    public static final AuraEffect TELUM = new PotionDistributionEffect(Potion.damageBoost, 4, 6, ticksForMinutes(6), 2).register(Aspect.AVERSION);
    public static final AuraEffect TUTAMEN = new PotionDistributionEffect(Potion.resistance, 4, 6, ticksForMinutes(6), 1).register(Aspect.PROTECT);
    public static final AuraEffect MOTUS = new PotionDistributionEffect(Potion.moveSpeed, 4, 6, ticksForMinutes(2), 2).register(Aspect.MOTION); //Fast speed for short duration, long charge time
    //public static final AuraEffect ITER = new PotionDistributionEffect(Potion.moveSpeed, 2, 10, ticksForMinutes(15), 0).register(Aspect.TRAVEL); //Slower speed for long duration, fast charge time
    //public static final AuraEffect FAMES = new PotionDistributionEffect(Potion.hunger, 4, 6, ticksForMinutes(2), 0).register(Aspect.HUNGER);
    public static final AuraEffect SENSUS = new PotionDistributionEffect(Potion.nightVision, 2, 8, ticksForMinutes(8), 0).register(Aspect.SENSES);
    public static final AuraEffect VOLATUS = new PotionDistributionEffect(Potion.jump, 2, 5, ticksForMinutes(4), 0).register(Aspect.FLIGHT);
    public static final AuraEffect POTENTIA = new PotionDistributionEffect(Potion.damageBoost, 4, 6, ticksForMinutes(4), 0).register(Aspect.ENERGY);
    public static final AuraEffect TENEBRAE = new PotionDistributionEffect(Potion.blindness, 4, 6, ticksForMinutes(1), 0).register(Aspect.DARKNESS);
    //public static final AuraEffect SANO = new PotionDistributionEffect(Potion.regeneration, 8, 10, ticksForMinutes(5), 1).register(Aspect.HEAL);
    public static final AuraEffect MORTUUS = new PotionDistributionEffect(Potion.wither, 4, 6, ticksForMinutes(3), 1).register(Aspect.DEATH);
    public static final AuraEffect HUMANUS = new PotionDistributionEffect(Potion.absorption, 2, 5, ticksForMinutes(4), 1).register(Aspect.MAN);
    public static final AuraEffect INSTRUMENTUM = new PotionDistributionEffect(Potion.digSpeed, 2, 7, ticksForMinutes(6), 0).register(Aspect.TOOL);
    public static final AuraEffect FABRICO = new PotionDistributionEffect(Potion.digSpeed, 2, 7, ticksForMinutes(5), 2).register(Aspect.CRAFT);
    //public static final AuraEffect VENENUM = new PotionDistributionEffect(Potion.poison, 2, 6, ticksForMinutes(1), 0).register(Aspect.POISON);
    public static final AuraEffect VINCULUM = new PotionDistributionEffect(Potion.moveSlowdown, 4, 6, ticksForMinutes(3), 0).register(Aspect.TRAP);

    //Special effects..
    public static final AuraEffect LUCRUM = new PotionDistributionEffect(RegisteredPotions.POTION_LUCK, 4, 6, ticksForMinutes(5), 1).register(Aspect.DESIRE); //Mining luck (Adding fortune)
    public static final AuraEffect VICTUS = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityAgeable && ((EntityAgeable) e).getGrowingAge() < 0;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            if(e instanceof EntityPlayer) return;
            EntityAgeable ageable = (EntityAgeable) e;
            if(ageable.worldObj.rand.nextInt(20) == 0) ageable.setGrowingAge(0); //setAdult
        }
    }.register(Aspect.LIFE);
    /*public static final AuraEffect PANNUS = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntitySheep && ((EntitySheep) e).getSheared();
        }

        @Override
        public void doEntityEffect(ChunkCoordinates originTile, Entity e) {
            EntitySheep sheep = (EntitySheep) e;
            if(sheep.worldObj.rand.nextInt(10) == 0) sheep.setSheared(false);
        }

        @Override
        public int getTickInterval() {
            return 10;
        }
    }.register(Aspect.CLOTH);*/
    public static final AuraEffect LUX = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return true;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
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
    /*public static final AuraEffect TEMPESTAS = new AuraEffect.BlockAuraEffect() {
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
    }.register(Aspect.WEATHER);*/
    public static final AuraEffect VITIUM = new AuraEffect() {
        @Override
        public EffectType getEffectType() {
            return null;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            if(e.worldObj.rand.nextInt(20) == 0 && !((EntityLivingBase) e).isPotionActive(PotionFluxTaint.instance)) {
                ((EntityLivingBase) e).addPotionEffect(new PotionEffect(PotionFluxTaint.instance.getId(), ticksForSeconds(20), 0, true, false));
            }
        }

        @Override
        public int getBlockCount(Random random) {
            return 5;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            BlockTaintFibre.spreadFibres(world, selectedBlock);
            if(world.rand.nextInt(12) == 0) {
                Utils.setBiomeAt(world, selectedBlock, BiomeHandler.biomeTaint, true);
                //world.addBlockEvent(x, y, z, world.getBlock(x, y, z), 1, 0);
            }
        }

        @Override
        public double getRange() {
            return 14;
        }
    }.register(Aspect.FLUX);
    public static final AuraEffect GELUM = new AuraEffect() {
        @Override
        public EffectType getEffectType() {
            return null;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            EntityLivingBase living = (EntityLivingBase) e;
            int activeDuration = 0;
            if(living.isPotionActive(Potion.moveSlowdown)) {
                PotionEffect effect = living.getActivePotionEffect(Potion.moveSlowdown);
                activeDuration = effect.getDuration();
            }

            boolean canAdd = (ticksForMinutes(3) - activeDuration) >= 5;
            if(canAdd) {
                living.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), activeDuration + 5, 1, true, false));
            }
        }

        @Override
        public int getTickInterval() {
            return 2;
        }

        @Override
        public int getBlockCount(Random random) {
            return random.nextInt(10) + 10;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            IBlockState state = world.getBlockState(selectedBlock);
            int meta = state.getBlock().getMetaFromState(state);
            if(state.getBlock().equals(Blocks.water) && meta == 0) {
                world.setBlockState(selectedBlock, Blocks.ice.getDefaultState());
                //world.setBlock(selectedBlock.posX, selectedBlock.posY, selectedBlock.posZ, Blocks.ice);
            }
        }
    }.register(Aspect.COLD);
    public static final AuraEffect EXAMINIS = new AuraEffect() {
        @Override
        public EffectType getEffectType() {
            return null;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase && ((EntityLivingBase) e).isEntityUndead();
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            addOrExtendPotionEffect(Potion.damageBoost, (EntityLivingBase) e, ticksForMinutes(3), 30, 1, false);
            addOrExtendPotionEffect(Potion.moveSpeed, (EntityLivingBase) e, ticksForMinutes(3), 30, 0, false);
            addOrExtendPotionEffect(Potion.regeneration, (EntityLivingBase) e, ticksForMinutes(3), 30, 0, false);
        }

        @Override
        public int getTickInterval() {
            return 4;
        }

        @Override
        public int getBlockCount(Random random) {
            return random.nextInt(60) == 0 ? 1 : 0;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            EntityBrainyZombie zombie = new EntityBrainyZombie(world);
            boolean canSpawn = setAndCheckPosition(zombie, selectedBlock, world, true) && zombie.getCanSpawnHere(); //Position for getCanSpawn here is updated.
            if(canSpawn) {
                BlockPos pos = new BlockPos((int) zombie.posX, (int) zombie.posY, (int) zombie.posZ);
                pos = iterateDown(pos, world);
                zombie.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                world.spawnEntityInWorld(zombie);
            }
        }

        @Override
        public double getRange() {
            return 10;
        }
    }.register(Aspect.UNDEAD);
    public static final AuraEffect AURAM = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityPlayer;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            EntityPlayer player = (EntityPlayer) e;

            if(player.getHeldItem() != null && player.getHeldItem().getItem() != null && player.getHeldItem().getItem() instanceof ItemWand) {
                ItemStack wand = player.getHeldItem();
                ItemWand wandCasting = (ItemWand) wand.getItem();
                AspectList al = wandCasting.getAspectsWithRoom(wand);
                for(Aspect a : al.getAspects()) {
                    if(a != null) wandCasting.addVis(wand, a, 22, true);
                }
            }
        }

        @Override
        public int getTickInterval() {
            return 1;
        }
    }.register(Aspect.AURA);
    public static final AuraEffect COGNITIO = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityPlayer;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            EntityPlayer player = (EntityPlayer) e;

            //TODO come up with something interesting.
            /*for(Aspect a : Aspect.getPrimalAspects()) {
                if(a != null && e.worldObj.rand.nextInt(40) == 0) {
                    if(e.worldObj.rand.nextInt(20) == 0) {
                        //1 temp warp.
                        Thaumcraft.addWarpToPlayer(player, 1, true);
                    }
                    ScanManager.checkAndSyncAspectKnowledge(player, a, 1);
                }
            }*/
        }

        @Override
        public int getTickInterval() {
            return 40;
        }
    }.register(Aspect.MIND);
    /*public static final AuraEffect FABRICO = new AuraEffect() {
        @Override
        public EffectType getEffectType() {
            return null;
        }

        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityGolemBase;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            EntityGolemBase golem = (EntityGolemBase) e;

            addOrExtendPotionEffect(RegisteredPotions.BUFF_GOLEM, golem, ticksForMinutes(6), 10, 0, false);
        }

        @Override
        public int getBlockCount(Random random) {
            return RegisteredIntegrations.automagy.isPresent() ? 50 : 0;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            RegisteredIntegrations.automagy.tryFillGolemCrafttable(selectedBlock, world);
        }
    }.register(Aspect.CRAFT);*/
    public static final AuraEffect HERBA = new AuraEffect.BlockAuraEffect() {
        @Override
        public int getBlockCount(Random random) {
            return 180;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            waterLocation(selectedBlock, world);
        }

        @Override
        public int getTickInterval() {
            return 1;
        }
    }.register(Aspect.PLANT);
    /*public static final AuraEffect ARBOR = new AuraEffect.BlockAuraEffect() {
        @Override
        public int getBlockCount(Random random) {
            return 120;
        }

        @Override
        public void doBlockEffect(ChunkCoordinates originTile, ChunkCoordinates selectedBlock, World world) {
            waterLocation(selectedBlock, world);
        }

        @Override
        public int getTickInterval() {
            return 3;
        }
    }.register(Aspect.TREE);*/
    public static final AuraEffect IGNIS = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityLivingBase;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            if(!e.isImmuneToFire()) {
                e.setFire(10);
            }
        }
    }.register(Aspect.FIRE);
    public static final AuraEffect ELDRITCH = new AuraEffect.EntityAuraEffect() {
        @Override
        public boolean isEntityApplicable(Entity e) {
            if(e instanceof EntityMob) {
                EntityMob mob = (EntityMob) e;
                if(mob.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue() < 0
                        && mob.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() >= 10.0D) {
                    boolean whitelisted = false;
                    for (Class clazz : ConfigEntities.championModWhitelist.keySet()) {
                        if (clazz.isAssignableFrom(e.getClass())) {
                            whitelisted = true;
                        }
                    }
                    return whitelisted;
                }
            }
            return false;
        }

        @Override
        public int getTickInterval() {
            return 20;
        }

        @Override
        public void doEntityEffect(BlockPos originTile, Entity e) {
            if(e.worldObj.rand.nextInt(4) == 0) {
                EntityMob mob = (EntityMob) e;
                PotionEffect effect = mob.getActivePotionEffect(RegisteredPotions.ELDRITCH);

                if(effect != null && effect.getAmplifier() > 4) {
                    EntityUtils.makeChampion(mob, false);
                    mob.removePotionEffect(RegisteredPotions.ELDRITCH.getId());
                } else {
                    mob.addPotionEffect(new PotionEffect(RegisteredPotions.ELDRITCH.getId(), MiscUtils.ticksForMinutes(1), effect == null ? 1 : effect.getAmplifier() + 1));
                }
            }
        }
    }.register(Aspect.ELDRITCH);
    public static final AuraEffect SOUL = new AuraEffect.BlockAuraEffect() {
        @Override
        public int getBlockCount(Random random) {
            return random.nextInt(60) == 0 ? 1 : 0;
        }

        @Override
        public int getTickInterval() {
            return 4;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            EntityLiving mob = world.rand.nextBoolean() ? new EntitySkeleton(world) : new EntityZombie(world);
            if(setAndCheckPosition(mob, selectedBlock, world, true) && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                for(int i = 0; i < 5; i++) {
                    mob.setCurrentItemOrArmor(i, null);
                }

                int timeout = 40;
                int totalCount = (world.rand.nextInt(3) == 0 ? 1 : 0) + 2;
                do {
                    int slot = mob.getEquipmentInSlot(0) == null ? 0 : (mob.getEquipmentInSlot(4) == null ? 4 : (world.rand.nextInt(3)+1));
                    if(mob.getEquipmentInSlot(slot) == null) {
                        ItemStack[] items = ITEMS_SOUL[slot];
                        ItemStack stack = items[world.rand.nextInt(items.length)];
                        if(stack.getItem() != Items.bow || mob instanceof EntitySkeleton) {
                            totalCount--;
                            mob.setCurrentItemOrArmor(slot, stack);
                            mob.setEquipmentDropChance(slot, 0);
                        }
                    }
                    timeout--;
                } while (totalCount > 0 && timeout > 0);

                if(timeout > 0) {
                    mob.addPotionEffect(new PotionEffect(Potion.invisibility.getId(), MiscUtils.ticksForMinutes(60*24*365), 1, true, false));

                    BlockPos pos = new BlockPos((int) mob.posX, (int) mob.posY, (int) mob.posZ);
                    pos = iterateDown(pos, world);
                    mob.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    world.spawnEntityInWorld(mob);
                }
            }
        }
    }.register(Aspect.SOUL);
    //public static final AuraEffect PRAECANTATIO = new PotionDistributionEffect(RegisteredPotions.VIS_DISCOUNT, 4, 6, ticksForMinutes(5), 1).register(Aspect.MAGIC);
    /*public static final AuraEffect LIMUS = new AuraEffect.BlockAuraEffect() {
        @Override
        public int getBlockCount(Random random) {
            return random.nextInt(40) == 0 ? 1 : 0;
        }

        @Override
        public void doBlockEffect(ChunkCoordinates originTile, ChunkCoordinates selectedBlock, World world) {
            EntitySlime slime = new EntitySlime(world);
            NBTTagCompound data = new NBTTagCompound();
            slime.writeEntityToNBT(data);
            data.setInteger("Size", 0);
            slime.readEntityFromNBT(data);
            boolean canSpawn = setAndCheckPosition(slime, selectedBlock, world, true) && world.difficultySetting != EnumDifficulty.PEACEFUL;
            if(canSpawn) {
                ChunkCoordinates pos = new ChunkCoordinates((int) slime.posX, (int) slime.posY, (int) slime.posZ);
                pos = iterateDown(pos, world);
                slime.setPosition(pos.posX + 0.5, pos.posY, pos.posZ + 0.5);
                world.spawnEntityInWorld(slime);
            }
        }

        @Override
        public int getTickInterval() {
            return 4;
        }
    }.register(Aspect.SLIME);*/
    public static final AuraEffect BESTIA = new AuraEffect.BlockAuraEffect() {

        private final Class[] animalClasses = new Class[] { EntitySheep.class, EntityCow.class, EntityChicken.class, EntityPig.class };

        @Override
        public int getBlockCount(Random random) {
            return random.nextInt(60) == 0 ? 1 : 0;
        }

        @Override
        public void doBlockEffect(BlockPos originTile, BlockPos selectedBlock, World world) {
            Class animalClass = animalClasses[world.rand.nextInt(animalClasses.length)];
            EntityLivingBase animal;
            try {
                animal = (EntityLivingBase) animalClass.getConstructor(World.class).newInstance(world);
            } catch (Exception e) {
                return;
            }
            boolean canSpawn = setAndCheckPosition(animal, selectedBlock, world, true);
            if(canSpawn) {
                BlockPos pos = new BlockPos((int) animal.posX, (int) animal.posY, (int) animal.posZ);
                pos = iterateDown(pos, world);
                animal.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                world.spawnEntityInWorld(animal);
            }
        }

        @Override
        public int getTickInterval() {
            return 4;
        }
    }.register(Aspect.BEAST);

    public static final AuraEffect VITREUS = new PotionDistributionEffect(RegisteredPotions.ACHROMATIC, 4, 6, ticksForMinutes(5), 0) {
        @Override
        public boolean isEntityApplicable(Entity e) {
            return e instanceof EntityPlayer;
        }
    }.register(Aspect.CRYSTAL);

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
        public void doEntityEffect(BlockPos originTile, Entity e) {
            if (e == null || !(e instanceof EntityLivingBase)) return;

            EntityLivingBase living = (EntityLivingBase) e;
            addOrExtendPotionEffect(potion, living, durationCap, addedDuration, amplifier);
        }

        @Override
        public int getTickInterval() {
            return tickInterval;
        }
    }

    private static void waterLocation(BlockPos pos, World world) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.getTickRandomly()) {
            world.scheduleBlockUpdate(pos, block, world.rand.nextInt(8) + 2, 1);
        }
        /*if(IntegrationThaumicTinkerer.isCropBlock(block)) {
            TileEntity te = world.getTileEntity(coordinates.posX, coordinates.posY, coordinates.posZ);
            if(te != null && IntegrationThaumicTinkerer.isCropTile(te)) {
                for (int i = 0; i < 10; i++) {
                    te.updateEntity(); //Badumm tss..
                }
            }
        }*/
    }

    private static void addOrExtendPotionEffect(Potion potion, EntityLivingBase entityLiving, int cap, int durToAdd, int amplifier, boolean ambient) {
        int activeDuration = 0;
        if(entityLiving.isPotionActive(potion)) {
            PotionEffect effect = entityLiving.getActivePotionEffect(potion);
            activeDuration = effect.getDuration();
        }

        boolean canAdd = (cap - activeDuration) >= durToAdd;
        if(canAdd) {
            entityLiving.addPotionEffect(new PotionEffect(potion.getId(), activeDuration + durToAdd, amplifier, ambient, false));
        }
    }

    private static void addOrExtendPotionEffect(Potion potion, EntityLivingBase entityLiving, int cap, int durToAdd, int amplifier) {
        addOrExtendPotionEffect(potion, entityLiving, cap, durToAdd, amplifier, true);
    }

    private static BlockPos iterateDown(BlockPos pos, World world) {
        while(world.isAirBlock(pos)) {
            pos = pos.add(0, -1, 0);
        }
        pos = pos.add(0, 1, 0);
        return pos;
    }

    //Designed for spawning. Looks in 1 block radius for possible location, and if one location is found, teleporting the entity there.
    //Call with mayVary == true to search in 1 block radius, call with mayVary == false for only checking the current block.
    private static boolean setAndCheckPosition(EntityLivingBase entity, BlockPos pos, World world, boolean mayVary) {
        if(!world.isAirBlock(pos)) {
            if(!mayVary) return false;
            BlockPos up = pos.add(0, 1, 0);
            if(setAndCheckPosition(entity, up, world, false)) {
                return true;
            }
            BlockPos down = pos.add(0, -1, 0);
            return setAndCheckPosition(entity, down, world, false);
        }
        BlockPos up = pos.add(0, 1, 0);
        if(world.isAirBlock(up)) {
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        BlockPos down = pos.add(0, -1, 0);
        if(world.isAirBlock(down)) {
            entity.setPosition(down.getX(), down.getY(), down.getZ());
            return true;
        }
        if(!mayVary) return false;
        BlockPos hMove = pos.add(1, 0, 0);
        if(setAndCheckPosition(entity, hMove, world, false)) return true;
        hMove = pos.add(0, 0, 1);
        if(setAndCheckPosition(entity, hMove, world, false)) return true;
        hMove = pos.add(-1, 0, 0);
        if(setAndCheckPosition(entity, hMove, world, false)) return true;
        hMove = pos.add(0, 0, -1);
        if(setAndCheckPosition(entity, hMove, world, false)) return true;
        return false;
    }

}
