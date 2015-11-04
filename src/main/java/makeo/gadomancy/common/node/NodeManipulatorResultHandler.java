package makeo.gadomancy.common.node;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.registration.RegisteredManipulations;
import net.minecraft.util.WeightedRandom;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.10.2015 11:43
 */
public class NodeManipulatorResultHandler {

    private static List<NodeManipulatorResult> possibleResults = new ArrayList<NodeManipulatorResult>();

    private NodeManipulatorResultHandler() {}

    public static NodeManipulatorResult getRandomResult(TileExtendedNode affectedNode) {
        return getRandomResult(affectedNode.getWorldObj().rand, affectedNode);
    }

    public static NodeManipulatorResult getRandomResult(Random random, TileExtendedNode affectedNode) {
        List<NodeManipulatorResult> localResults = new ArrayList<NodeManipulatorResult>();
        for(NodeManipulatorResult result : possibleResults) {
            if(result.canAffect(affectedNode)) localResults.add(result);
        }
        if(localResults.isEmpty()) return null;
        return (NodeManipulatorResult) WeightedRandom.getRandomItem(random, localResults);
    }

    public static void combine(AspectList containingList, Aspect a, Aspect b) {
        if(!canCombine(a, b)) return;
        Aspect combination = NodeManipulatorResultHandler.getCombination(a, b);
        int lowerAmount;
        if(containingList.getAmount(a) < containingList.getAmount(b)) {
            lowerAmount = containingList.getAmount(a);
        } else {
            lowerAmount = containingList.getAmount(b);
        }
        containingList.remove(a, lowerAmount);
        containingList.remove(b, lowerAmount);
        containingList.add(combination, lowerAmount);
    }

    public static boolean canCombine(Aspect a, Aspect b) {
        return getCombination(a, b) != null;
    }

    public static Aspect getCombination(Aspect a, Aspect b) {
        for(Aspect aspect : Aspect.getCompoundAspects()) {
            Aspect[] components = aspect.getComponents();
            if((components[0] == a && components[1] == b) ||
                    components[0] == b && components[1] == a) {
                return aspect;
            }
        }
        return null;
    }

    static {
        //Aspect combination pair
        possibleResults.add(RegisteredManipulations.resultBreakCompounds);
        possibleResults.add(RegisteredManipulations.resultCombineAspects);

        //Modifier pair
        possibleResults.add(RegisteredManipulations.resultIncreaseModifier);
        possibleResults.add(RegisteredManipulations.resultDecreaseModifier);

        //Switch node Type
        possibleResults.add(RegisteredManipulations.resultSwitchType);

        //Almighty Growing
        possibleResults.add(RegisteredManipulations.resultApplyGrowing);

        //Let the node gain primals
        possibleResults.add(RegisteredManipulations.resultGainPrimal);
    }

}
