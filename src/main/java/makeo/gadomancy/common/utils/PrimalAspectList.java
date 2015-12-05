package makeo.gadomancy.common.utils;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.research.ResearchManager;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 03.12.2015 17:22
 */
public class PrimalAspectList extends AspectList {

    public AspectList addOne(Aspect a) {
        return add(a, 1);
    }

    @Override
    public AspectList add(AspectList in) {
        in = ResearchManager.reduceToPrimals(in);
        return super.add(in);
    }

    @Override
    public AspectList add(Aspect aspect, int amount) {
        AspectList al = new AspectList();
        al.add(aspect, amount);
        al = ResearchManager.reduceToPrimals(al);
        for(Aspect a : al.getAspects()) {
            super.add(a, al.getAmount(a));
        }
        return this;
    }
}
