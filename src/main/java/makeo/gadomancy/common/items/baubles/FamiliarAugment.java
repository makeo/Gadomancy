package makeo.gadomancy.common.items.baubles;

import net.minecraft.util.StatCollector;

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
    public static final FamiliarAugment SHOCK = new FamiliarAugment("shock");
    public static final FamiliarAugment POISON = new FamiliarAugment("poison").addConflict(SHOCK);
    public static final FamiliarAugment FIRE = new FamiliarAugment("fire").addConflict(SHOCK, POISON);
    public static final FamiliarAugment WEAKNESS = new FamiliarAugment("weakness").addConflict(SHOCK, POISON, FIRE);

    //Side effects
    public static final FamiliarAugment DAMAGE_INCREASE = new FamiliarAugment("damage").addCondition(new PreconditionAny(SHOCK, POISON, FIRE, WEAKNESS));
    public static final FamiliarAugment RANGE_INCREASE = new FamiliarAugment("range").addConflict(DAMAGE_INCREASE).addCondition(new PreconditionAny(SHOCK, POISON, FIRE, WEAKNESS));
    public static final FamiliarAugment ATTACK_SPEED = new FamiliarAugment("speed").addConflict(DAMAGE_INCREASE, RANGE_INCREASE).addCondition(new PreconditionAny(SHOCK, POISON, FIRE, WEAKNESS));


    private final String unlocalizedName;
    private List<FamiliarAugmentPrecondition> preconditions = new ArrayList<FamiliarAugmentPrecondition>();
    private boolean requiresPrevLevel = true;
    private List<FamiliarAugment> conflicts = new ArrayList<FamiliarAugment>();

    private static Map<String, FamiliarAugment> BY_NAME = new HashMap<String, FamiliarAugment>();

    private FamiliarAugment(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        BY_NAME.put(this.unlocalizedName.toLowerCase(), this);
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

    public String getUnlocalizedName() {
        return String.format(FORMAT_NAME, unlocalizedName);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName());
    }

    public static FamiliarAugment getByUnlocalizedName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    //Returns true, if the current conditions for application are fulfilled for a familiar with given augments.
    public boolean checkConditions(List<FamiliarAugment.FamiliarAugmentPair> currentAugments, int levelToSet) {
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

    private abstract static class FamiliarAugmentPrecondition {

        public abstract boolean isFulfilled(List<FamiliarAugmentPair> currentAugments, int levelToSet);

    }

    public static class PreconditionAny extends FamiliarAugmentPrecondition {

        private FamiliarAugment[] anyPrevAugment;

        private PreconditionAny(FamiliarAugment... any) {
            this.anyPrevAugment = any;
        }

        @Override
        public boolean isFulfilled(List<FamiliarAugmentPair> currentAugments, int levelToSet) {
            if(anyPrevAugment == null) return true;
            for(FamiliarAugment augment : anyPrevAugment) {
                if(augment == null) continue;
                for(FamiliarAugmentPair pair : currentAugments) {
                    if(pair.augment.equals(augment)) return true;
                }
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
        public boolean isFulfilled(List<FamiliarAugmentPair> currentAugments, int levelToSet) {
            if(prevAugments == null) return true;
            for(FamiliarAugment augment : prevAugments) {
                if(augment == null) continue;
                boolean contains = false;
                for(FamiliarAugmentPair pair : currentAugments) {
                    if(pair.augment.equals(augment)) contains = true;
                }
                if(!contains) return false;
            }
            return true;
        }
    }

    public static class FamiliarAugmentPair {

        public final FamiliarAugment augment;
        public final int level;

        public FamiliarAugmentPair(FamiliarAugment augment, int level) {
            this.augment = augment;
            this.level = level;
        }
    }

}
