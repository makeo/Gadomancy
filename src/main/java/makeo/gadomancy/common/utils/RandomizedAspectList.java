package makeo.gadomancy.common.utils;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 18.12.2015 13:58
 */
public class RandomizedAspectList extends AspectList {

    private static final Random RANDOM = new Random();
    private Map<Aspect, Integer> aspectMap = new HashMap<Aspect, Integer>();
    private long lastRandomization = 0;
    private boolean halfCap = false;

    public RandomizedAspectList addAspectRandomBase(Aspect aspect, int amount) {
        aspectMap.put(aspect, amount);
        return this;
    }

    public RandomizedAspectList setHalfCap(boolean halfCap) {
        this.halfCap = halfCap;
        return this;
    }

    private void checkRandomization() {
        if((System.currentTimeMillis() - lastRandomization) > 500) {
            super.aspects.clear();
            for(Aspect a : aspectMap.keySet()) {
                if(a == null) continue;
                int am;
                if(halfCap) {
                    int c = aspectMap.get(a);
                    am = (c / 2) + RANDOM.nextInt(c / 2);
                } else {
                    am = RANDOM.nextInt(aspectMap.get(a));
                }
                if(am > 0) {
                    super.add(a, am);
                }
            }
            lastRandomization = System.currentTimeMillis();
        }
    }

    @Override
    public Aspect[] getAspects() {
        checkRandomization();
        return super.getAspects();
    }

    @Override
    public Aspect[] getAspectsSorted() {
        checkRandomization();
        return super.getAspectsSorted();
    }

    @Override
    public Aspect[] getAspectsSortedAmount() {
        checkRandomization();
        return super.getAspectsSortedAmount();
    }

    @Override
    public int getAmount(Aspect key) {
        checkRandomization();
        return super.getAmount(key);
    }

    @Override
    public Aspect[] getPrimalAspects() {
        checkRandomization();
        return super.getPrimalAspects();
    }

}
