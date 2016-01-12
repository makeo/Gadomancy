package makeo.gadomancy.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 17.11.2015 14:36
 */
public abstract class AuraEffect {

    /**
     * Return the EffectType you want to use here.
     *
     * EffectType.ENTITY_EFFECT selects entities within {@link AuraEffect#getRange()}
     *      range and fires the Entity-related methods
     *
     * EffectType.BLOCK_EFFECT selects random locations within {@link #getRange()}
     *      range and fires {@link #doBlockEffect(net.minecraft.util.ChunkCoordinates, net.minecraft.util.ChunkCoordinates, net.minecraft.world.World)}
     *
     * Returning null will cause BOTH effects to happen and thus, both should be implemented.
     */
    public abstract EffectType getEffectType();

    /**
     * Only fired when {@link #getEffectType()} returns {@link makeo.gadomancy.api.AuraEffect.EffectType#ENTITY_EFFECT}
     *
     * @param e an entity to check if it should be handled.
     * @return if the entity should be handled with {@link #doEntityEffect(net.minecraft.util.ChunkCoordinates, net.minecraft.entity.Entity)}
     */
    public abstract boolean isEntityApplicable(Entity e);

    /**
     * Only fired when {@link #getEffectType()} returns {@link makeo.gadomancy.api.AuraEffect.EffectType#ENTITY_EFFECT}
     * Do the actual effect on the Entity here.
     *
     * @param originTile where the master tile of the aura pylon is.
     * @param e an entity where {@link #isEntityApplicable(net.minecraft.entity.Entity)} returned true
     */
    public abstract void doEntityEffect(ChunkCoordinates originTile, Entity e);

    /**
     * Only fired when {@link #getEffectType()} returns {@link makeo.gadomancy.api.AuraEffect.EffectType#BLOCK_EFFECT}
     *
     * @param random a random to evaluate random numbers. convenience reasons
     * @return the amount of blocks that should be collected.
     */
    public abstract int getBlockCount(Random random);

    /**
     * Only fired when {@link #getEffectType()} returns {@link makeo.gadomancy.api.AuraEffect.EffectType#BLOCK_EFFECT}
     * Do the actual effect on a block here.
     *
     * @param originTile where the master tile of the aura pylon is.
     * @param selectedBlock a selected block
     * @param world world of the tile and the block
     */
    public abstract void doBlockEffect(ChunkCoordinates originTile, ChunkCoordinates selectedBlock, World world);

    /**
     * Returns the tick interval how often the aura effect is called
     */
    public int getTickInterval() {
        return 8;
    }

    /**
     * Get the range of the aura pylon's effect
     *
     * Defines
     *  - the range where entities are collected around the pylon.
     *  - the range where blocks are collected around the pylon.
     *  - how close the player has to be to get the aura's research
     */
    public double getRange() {
        return 8;
    }

    /**
     * Registers the AuraEffect to the given Aspect.
     *
     * @param a to register this to.
     * @return the current AuraEffect object
     */
    public AuraEffect register(Aspect a) {
        GadomancyApi.registerAdditionalAuraEffect(a, this);
        return this;
    }

    /**
     * A EntityEffect AuraEffect class for convenience
     */
    public static abstract class EntityAuraEffect extends AuraEffect {

        @Override
        public final EffectType getEffectType() {
            return EffectType.ENTITY_EFFECT;
        }

        @Override
        public abstract boolean isEntityApplicable(Entity e);

        @Override
        public abstract void doEntityEffect(ChunkCoordinates originTile, Entity e);

        @Override
        public final int getBlockCount(Random random) {
            return 0;
        }

        @Override
        public final void doBlockEffect(ChunkCoordinates originTile, ChunkCoordinates selectedBlock, World world) {}
    }

    /**
     * A BlockEffect AuraEffect class for convenience
     */
    public static abstract class BlockAuraEffect extends AuraEffect {

        @Override
        public final EffectType getEffectType() {
            return EffectType.BLOCK_EFFECT;
        }

        @Override
        public final boolean isEntityApplicable(Entity e) {
            return false;
        }

        @Override
        public final void doEntityEffect(ChunkCoordinates originTile, Entity e) {}

    }

    public static enum EffectType {

        ENTITY_EFFECT,
        BLOCK_EFFECT

    }

}
