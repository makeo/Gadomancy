package makeo.gadomancy.common.utils;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.research.ResearchManager;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 03.12.2015 17:22
 */
public class PrimalAspectList extends AspectList {

    public AspectList addOne(Aspect a) {
        return add(a, 1);
    }

    @Override
    public AspectList add(AspectList in) {
        in = reduceToPrimals(in);
        return super.add(in);
    }

    @Override
    public AspectList add(Aspect aspect, int amount) {
        AspectList al = new AspectList();
        al.add(aspect, amount);
        al = reduceToPrimals(al);
        for (Aspect a : al.getAspects()) {
            super.add(a, al.getAmount(a));
        }
        return this;
    }

    public static AspectList reduceToPrimals(AspectList al) {
        return reduceToPrimals(al, false);
    }

    public static AspectList reduceToPrimals(AspectList al, boolean merge) {
        AspectList out = new AspectList();
        for (Aspect aspect : al.getAspects()) {
            if (aspect != null) {
                if (aspect.isPrimal()) {
                    if (merge) {
                        out.merge(aspect, al.getAmount(aspect));
                    } else {
                        out.add(aspect, al.getAmount(aspect));
                    }
                } else {
                    AspectList send = new AspectList();
                    send.add(aspect.getComponents()[0], al.getAmount(aspect));
                    send.add(aspect.getComponents()[1], al.getAmount(aspect));
                    send = reduceToPrimals(send, merge);
                    for (Aspect a : send.getAspects()) {
                        if (merge) {
                            out.merge(a, send.getAmount(a));
                        } else {
                            out.add(a, send.getAmount(a));
                        }
                    }
                }
            }
        }
        return out;
    }

}
