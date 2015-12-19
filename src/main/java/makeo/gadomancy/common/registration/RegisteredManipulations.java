package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.node.ExtendedNodeType;
import makeo.gadomancy.common.node.NodeManipulatorResult;
import makeo.gadomancy.common.node.NodeManipulatorResultHandler;
import makeo.gadomancy.common.utils.ResearchHelper;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.10.2015 11:52
 */
public class RegisteredManipulations {

    public static NodeManipulatorResult resultBreakCompounds = new NodeManipulatorResult(4, NodeManipulatorResultHandler.ResultType.NEGATIVE) {
        @Override
        public boolean affect(World world, INode node) {
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

    public static NodeManipulatorResult resultCombineAspects = new NodeManipulatorResult(5, NodeManipulatorResultHandler.ResultType.POSITIVE) {
        @Override
        public boolean affect(World world, INode node) {
            AspectList base = node.getAspectsBase();
            if(base.getAspects().length < 2)
                return false;
            if(base.getAspects().length == 2) {
                if(!NodeManipulatorResultHandler.canCombine(base.getAspects()[0], base.getAspects()[1])) return false;
                int addition = world.rand.nextInt(4);
                NodeManipulatorResultHandler.combine(base, base.getAspects()[0], base.getAspects()[1], addition);
                NodeManipulatorResultHandler.combine(node.getAspects(), base.getAspects()[0], base.getAspects()[1], addition);
                return true;
            }
            if(base.getAspects().length > 2) {
                int actuallyCombined = 0;
                int combineCount = world.rand.nextInt(1) + 1;
                doLabel:
                do {
                    if(base.getAspects().length < 2) break;

                    int randIndexForA = world.rand.nextInt(base.getAspects().length);
                    for (int i = 0; i < base.getAspects().length; i++) {
                        int indexA = (i + randIndexForA) % base.getAspects().length;
                        Aspect a = base.getAspects()[indexA];
                        int randIndexForB = world.rand.nextInt(base.getAspects().length);
                        for (int j = 0; j < base.getAspects().length; j++) {
                            int indexB = (j + randIndexForB) % base.getAspects().length;
                            Aspect b = base.getAspects()[indexB];

                            if(NodeManipulatorResultHandler.canCombine(a, b)) {
                                int addition = world.rand.nextInt(4);
                                NodeManipulatorResultHandler.combine(base, a, b, addition);
                                NodeManipulatorResultHandler.combine(node.getAspects(), a, b, addition);
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

    public static NodeManipulatorResult resultIncreaseModifier = new NodeManipulatorResult(4, NodeManipulatorResultHandler.ResultType.POSITIVE) {

        @Override
        public boolean canAffect(World world, INode node) {
            return node.getNodeModifier() != NodeModifier.BRIGHT;
        }

        @Override
        public boolean affect(World world, INode node) {
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

    public static NodeManipulatorResult resultDecreaseModifier = new NodeManipulatorResult(5, NodeManipulatorResultHandler.ResultType.NEGATIVE) {

        @Override
        public boolean canAffect(World world, INode node) {
            return node.getNodeModifier() != NodeModifier.FADING;
        }

        @Override
        public boolean affect(World world, INode node) {
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

    public static NodeManipulatorResult resultGainPrimal = new NodeManipulatorResult(3, NodeManipulatorResultHandler.ResultType.POSITIVE) {
        @Override
        public boolean affect(World world, INode node) {
            List<Aspect> primals = Aspect.getPrimalAspects();
            int visSize = node.getAspectsBase().visSize();
            int size = node.getAspectsBase().size();
            int modulo = primals.size();
            int index = world.rand.nextInt(primals.size());
            for (int i = 0; i < primals.size(); i++) {
                int get = (index + i) % modulo;
                Aspect rand = primals.get(get);
                if(node.getAspectsBase().getAmount(rand) <= 0) {
                    int randGain = visSize / (size * 2);
                    if(randGain > 0) {
                        node.getAspectsBase().add(rand, randGain);
                        node.getAspects().add(rand, randGain);
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
    };

    public static NodeManipulatorResult resultSwitchType = new NodeManipulatorResult(2, NodeManipulatorResultHandler.ResultType.NEUTRAL) {
        @Override
        public boolean affect(World world, INode node) {
            NodeType newType = node.getNodeType();
            int random = world.rand.nextInt(40);
            if(random > 38) {
                if(node instanceof TileExtendedNode) {
                    TileExtendedNode tNode = (TileExtendedNode) node;
                    if(!(tNode.getExtendedNodeType() != null && tNode.getExtendedNodeType().equals(ExtendedNodeType.GROWING))) {
                        newType = NodeType.HUNGRY; //1 of 40
                    }
                } else {
                    newType = NodeType.HUNGRY; //1 of 40
                }
            } else if(random > 37) {
                newType = NodeType.TAINTED; //1 of 40
            } else if(random > 34) {
                newType = NodeType.UNSTABLE; //3 of 40
            } else if(random > 29) {
                newType = NodeType.DARK; //5 of 40
            } else if(random > 24) {
                newType = NodeType.PURE; //5 of 40
            } else if(random > 14) {
                newType = NodeType.NORMAL; //10 of 40
            }
            //15 of 40 chance nothing happens
            boolean changed = !newType.equals(node.getNodeType());
            if(changed) node.setNodeType(newType);
            return changed;
        }
    };

    public static NodeManipulatorResult resultApplyGrowing = new NodeManipulatorResult(1, NodeManipulatorResultHandler.ResultType.POSITIVE) {
        @Override
        public boolean affect(World world, INode inode) {
            if(!(inode instanceof TileExtendedNode)) return false;
            TileExtendedNode node = (TileExtendedNode) inode;
            boolean isGrowingAlready = node.getExtendedNodeType() != null && node.getExtendedNodeType().equals(ExtendedNodeType.GROWING);
            if(isGrowingAlready) if(node.getNodeType().equals(NodeType.HUNGRY)) isGrowingAlready = false;
            if(!isGrowingAlready) {
                node.setExtendedNodeType(ExtendedNodeType.GROWING);
                ResearchHelper.distributeResearch(Gadomancy.MODID.toUpperCase() + ".GROWING", node.getWorldObj(), node.xCoord, node.yCoord, node.zCoord, 12);
            }
            return isGrowingAlready;
        }
    };

}
