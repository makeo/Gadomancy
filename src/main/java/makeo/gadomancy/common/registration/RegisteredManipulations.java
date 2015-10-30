package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.node.NodeManipulatorResult;
import makeo.gadomancy.common.node.NodeManipulatorResultHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.10.2015 11:52
 */
public class RegisteredManipulations {

    public static NodeManipulatorResult resultBreakCompounds = new NodeManipulatorResult(4) {
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
                    baseList.add(subComponents[1], initialValue);
                    list.add(subComponents[1], initialValue);
                    return true;
                }
            }
            return false;
        }
    };

    public static NodeManipulatorResult resultCombineAspects = new NodeManipulatorResult(4) {
        @Override
        public boolean affect(TileExtendedNode node) {
            AspectList base = node.getAspectsBase();
            if(base.getAspects().length < 2)
                return false;
            if(base.getAspects().length == 2) {
                if(!NodeManipulatorResultHandler.canCombine(base.getAspects()[0], base.getAspects()[1])) return false;
                NodeManipulatorResultHandler.combine(base, base.getAspects()[0], base.getAspects()[1]);
                NodeManipulatorResultHandler.combine(node.getAspects(), base.getAspects()[0], base.getAspects()[1]);
                return true;
            }
            if(base.getAspects().length > 2) {
                int actuallyCombined = 0;
                int combineCount = node.getWorldObj().rand.nextInt(1) + 1;
                doLabel:
                do {
                    if(base.getAspects().length < 2) break;

                    int randIndexForA = node.getWorldObj().rand.nextInt(base.getAspects().length);
                    for (int i = 0; i < base.getAspects().length; i++) {
                        int indexA = (i + randIndexForA) % base.getAspects().length;
                        Aspect a = base.getAspects()[indexA];
                        int randIndexForB = node.getWorldObj().rand.nextInt(base.getAspects().length);
                        for (int j = 0; j < base.getAspects().length; j++) {
                            int indexB = (j + randIndexForB) % base.getAspects().length;
                            Aspect b = base.getAspects()[indexB];

                            if(NodeManipulatorResultHandler.canCombine(a, b)) {
                                NodeManipulatorResultHandler.combine(base, a, b);
                                NodeManipulatorResultHandler.combine(node.getAspects(), a, b);
                                combineCount--;
                                actuallyCombined++;
                                continue doLabel;
                            } else if(base.getAspects().length <= 2) {
                                break doLabel;
                            }

                        }
                    }
                    combineCount--;
                } while (combineCount > 0);
                if(actuallyCombined > 0) return true;
            }
            return false;
        }
    };

    public static NodeManipulatorResult resultIncreaseModifier = new NodeManipulatorResult(1) {

        @Override
        public boolean canAffect(TileExtendedNode node) {
            return node.getNodeModifier() != NodeModifier.BRIGHT;
        }

        @Override
        public boolean affect(TileExtendedNode node) {
            if(node.getNodeModifier() == null) {
                node.setNodeModifier(NodeModifier.BRIGHT);
                return true;
            }
            switch (node.getNodeModifier()) {
                case BRIGHT:
                    return false;
                case PALE:
                    node.setNodeModifier(null);
                    break;
                case FADING:
                    node.setNodeModifier(NodeModifier.PALE);
                    break;
            }
            return true;
        }
    };

    public static NodeManipulatorResult resultDecreaseModifier = new NodeManipulatorResult(1) {

        @Override
        public boolean canAffect(TileExtendedNode node) {
            return node.getNodeModifier() != NodeModifier.FADING;
        }

        @Override
        public boolean affect(TileExtendedNode node) {
            if(node.getNodeModifier() == null) {
                node.setNodeModifier(NodeModifier.PALE);
                return true;
            }
            switch (node.getNodeModifier()) {
                case BRIGHT:
                    node.setNodeModifier(null);
                    break;
                case PALE:
                    node.setNodeModifier(NodeModifier.FADING);
                    break;
                case FADING:
                    return false;
            }
            return true;
        }
    };

}
