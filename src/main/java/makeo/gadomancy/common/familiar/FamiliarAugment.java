package makeo.gadomancy.common.familiar;

import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.12.2015 23:44
 */
public class FamiliarAugment {

    private static final String FORMAT_NAME = "familiar.augment.%s.name";

    //Main effects
    public static final FamiliarAugment SHOCK = new FamiliarAugment("shock", new AspectList().add(Aspect.AIR, 3).add(Aspect.ENTROPY, 2));
    public static final FamiliarAugment POISON = new FamiliarAugment("poison", new AspectList().add(Aspect.WATER, 3).add(Aspect.ENTROPY, 4)).addConflict(SHOCK);
    public static final FamiliarAugment FIRE = new FamiliarAugment("fire", new AspectList().add(Aspect.FIRE, 4).add(Aspect.ORDER, 2)).addConflict(SHOCK, POISON);
    public static final FamiliarAugment WEAKNESS = new FamiliarAugment("weakness", new AspectList().add(Aspect.ENTROPY, 3).add(Aspect.EARTH, 3)).addConflict(SHOCK, POISON, FIRE);

    //Side effects
    public static final FamiliarAugment DAMAGE_INCREASE = new FamiliarAugment("damage", new AspectList().add(Aspect.FIRE, 3)).addCondition(new PreconditionAny(SHOCK, POISON, FIRE, WEAKNESS));
    public static final FamiliarAugment RANGE_INCREASE = new FamiliarAugment("range", new AspectList().add(Aspect.AIR, 2).add(Aspect.ORDER, 1)).addConflict(DAMAGE_INCREASE).addCondition(new PreconditionAny(SHOCK, POISON, FIRE, WEAKNESS));
    public static final FamiliarAugment ATTACK_SPEED = new FamiliarAugment("speed", new AspectList().add(Aspect.ORDER, 2).add(Aspect.FIRE, 1)).addConflict(DAMAGE_INCREASE, RANGE_INCREASE).addCondition(new PreconditionAny(SHOCK, POISON, FIRE, WEAKNESS));


    private final String unlocalizedName;
    private List<FamiliarAugmentPrecondition> preconditions = new ArrayList<FamiliarAugmentPrecondition>();
    private boolean requiresPrevLevel = true;
    private AspectList costsPerLevel;
    private List<FamiliarAugment> conflicts = new ArrayList<FamiliarAugment>();

    private static Map<String, FamiliarAugment> BY_NAME = new HashMap<String, FamiliarAugment>();

    private FamiliarAugment(String unlocalizedName, AspectList costs) {
        this.unlocalizedName = unlocalizedName;
        this.costsPerLevel = costs;
    }

    private FamiliarAugment setIgnorePreviousLevel() {
        this.requiresPrevLevel = false;
        return this;
    }

    private FamiliarAugment addCondition(FamiliarAugmentPrecondition condition) {
        preconditions.add(condition);
        return this;
    }

    private FamiliarAugment addConflict(FamiliarAugment... others) {
        for(FamiliarAugment augment : others) {
            if(augment == null) continue;
            addConflict(augment);
        }
        return this;
    }

    private FamiliarAugment addConflict(FamiliarAugment other) {
        if(!conflicts.contains(other)) conflicts.add(other);
        if(!other.conflicts.contains(this)) other.conflicts.add(this);
        return this;
    }

    public AspectList getCostsPerLevel() {
        return costsPerLevel;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(String.format(FORMAT_NAME, unlocalizedName));
    }

    public static FamiliarAugment getByUnlocalizedName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    //Returns true, if the current conditions for application are fulfilled for a familiar with given augments.
    public boolean checkConditions(FamiliarAugmentList currentAugments, int levelToSet) {
        if(levelToSet <= 0) return false;
        for(FamiliarAugmentPair current : currentAugments) {
            if(conflicts.contains(current.augment)) return false;
        }
        if(requiresPrevLevel) {
            boolean containsAtAll = false;
            int foundLevel = -1;
            for(FamiliarAugmentPair pair : currentAugments) {
                if(pair.augment.equals(FamiliarAugment.this)) {
                    containsAtAll = true;
                    foundLevel = pair.level;
                }
            }
            if(!containsAtAll) {
                if(levelToSet != 1) return false;
            } else {
                if(foundLevel != (levelToSet - 1)) return false;
            }
        }
        for(FamiliarAugmentPrecondition precondition : preconditions) {
            if(precondition == null) continue;
            if(!precondition.isFulfilled(currentAugments, levelToSet)) return false;
        }
        return true;
    }

    static {
        addNameEntry(SHOCK, SHOCK.unlocalizedName);
        addNameEntry(FIRE, FIRE.unlocalizedName);
        addNameEntry(POISON, POISON.unlocalizedName);
        addNameEntry(WEAKNESS, WEAKNESS.unlocalizedName);

        addNameEntry(DAMAGE_INCREASE, DAMAGE_INCREASE.unlocalizedName);
        addNameEntry(RANGE_INCREASE, RANGE_INCREASE.unlocalizedName);
        addNameEntry(ATTACK_SPEED, ATTACK_SPEED.unlocalizedName);
    }

    private static void addNameEntry(FamiliarAugment augment, String name) {
        BY_NAME.put(name.toLowerCase(), augment);
    }

    private abstract static class FamiliarAugmentPrecondition {

        public abstract boolean isFulfilled(FamiliarAugmentList currentAugments, int levelToSet);

    }

    public static class PreconditionAny extends FamiliarAugmentPrecondition {

        private FamiliarAugment[] anyPrevAugment;

        private PreconditionAny(FamiliarAugment... any) {
            this.anyPrevAugment = any;
        }

        @Override
        public boolean isFulfilled(FamiliarAugmentList currentAugments, int levelToSet) {
            if(anyPrevAugment == null) return true;
            for(FamiliarAugment augment : anyPrevAugment) {
                if(augment == null) continue;
                if(currentAugments.contains(augment)) return true;
            }
            return false;
        }
    }

    public static class PreconditionAll extends FamiliarAugmentPrecondition {

        private FamiliarAugment[] prevAugments;

        private PreconditionAll(FamiliarAugment... previous) {
            this.prevAugments = previous;
        }

        @Override
        public boolean isFulfilled(FamiliarAugmentList currentAugments, int levelToSet) {
            if(prevAugments == null) return true;
            for(FamiliarAugment augment : prevAugments) {
                if(augment == null) continue;
                if(!currentAugments.contains(augment)) return false;
            }
            return true;
        }
    }

    public static class FamiliarAugmentList extends ArrayList<FamiliarAugmentPair> {

        public boolean contains(FamiliarAugment augment) {
            return getAugmentPair(augment) != null;
        }

        public FamiliarAugmentPair getAugmentPair(FamiliarAugment augment) {
            for (FamiliarAugmentPair pair : this) {
                if(pair.augment.equals(augment)) return pair;
            }
            return null;
        }

        public int getLevel(FamiliarAugment augment) {
            if(!contains(augment)) return -1;
            return getAugmentPair(augment).level;
        }

    }

    public static class FamiliarAugmentPair {

        public final FamiliarAugment augment;
        public final int level;

        public FamiliarAugmentPair(FamiliarAugment augment, int level) {
            this.augment = augment;
            this.level = level;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FamiliarAugmentPair that = (FamiliarAugmentPair) o;

            return level == that.level && !(augment != null ? !augment.equals(that.augment) : that.augment != null);
        }

        @Override
        public int hashCode() {
            int result = augment != null ? augment.hashCode() : 0;
            result = 31 * result + level;
            return result;
        }
    }

}
