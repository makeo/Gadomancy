package makeo.gadomancy.common.node;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.registration.RegisteredManipulations;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 28.10.2015 11:43
 */
public class NodeManipulatorResultHandler {

    private static List<NodeManipulatorResult> possibleResults = new ArrayList<NodeManipulatorResult>();

    private NodeManipulatorResultHandler() {}

    public static NodeManipulatorResult getRandomResult(Random random, TileExtendedNode affectedNode) {
        List<NodeManipulatorResult> localResults = new ArrayList<NodeManipulatorResult>();
        for(NodeManipulatorResult result : possibleResults) {
            if(result.canAffect(affectedNode)) localResults.add(result);
        }
        if(localResults.isEmpty()) return null;
        return (NodeManipulatorResult) WeightedRandom.getRandomItem(random, localResults);
    }

    static {
        possibleResults.add(RegisteredManipulations.resultBreakCompounds);
    }

}
