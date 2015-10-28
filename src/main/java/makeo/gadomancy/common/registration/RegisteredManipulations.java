package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.node.NodeManipulatorResult;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.10.2015 11:52
 */
public class RegisteredManipulations {

    public static NodeManipulatorResult resultBreakCompounds = new NodeManipulatorResult(1, NodeManipulatorResult.ResultType.NEUTRAL) {
        @Override
        public boolean affect(TileExtendedNode node) {
            AspectList baseList = node.getAspectsBase();
            AspectList list = node.getAspects();
            for(Aspect a : baseList.getAspects()) {
                if(!a.isPrimal()) {
                    Aspect[] subComponents = a.getComponents();
                    int initialValue = baseList.getAmount(a);
                    list.remove(a);
                    baseList.remove(a);
                    baseList.add(subComponents[0], initialValue);
                    list.add(subComponents[0], initialValue);
                }
            }
            return true;
        }
    };

}
