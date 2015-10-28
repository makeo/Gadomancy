package makeo.gadomancy.common.node;

import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import net.minecraft.util.WeightedRandom;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 28.10.2015 11:45
 */
public abstract class NodeManipulatorResult extends WeightedRandom.Item {

    private final ResultType effect;

    public NodeManipulatorResult(int chanceToRoll, ResultType effect) {
        super(chanceToRoll);
        this.effect = effect;
    }

    public boolean canAffect(TileExtendedNode node) {
        return true;
    }

    public abstract boolean affect(TileExtendedNode node);

    public final ResultType getEffectType() {
        return effect;
    }

    public static enum ResultType {
        NEGATIVE, POSITIVE, NEUTRAL
    }

}
