package makeo.gadomancy.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 11.10.2015 12:09
 */
public abstract class ClickBehavior {
    protected World world;
    protected IBlockState state;
    protected BlockPos pos;

    protected boolean hasVisCost;

    public ClickBehavior(boolean hasVisCost) {
        this.hasVisCost = hasVisCost;
    }

    public ClickBehavior() {
        this(false);
    }

    /**
     * This method is called by me to init all variables
     */
    @Deprecated
    public void init(World world, IBlockState state, BlockPos pos) {
        this.world = world;
        this.state = state;
        this.pos = pos;
    }

    /**
     * @return if this class needs to handle the given block
     */
    public abstract boolean isValidForBlock();

    //TODO not handled as expected.
    public boolean hasVisCost() {
        return hasVisCost;
    }

    public int getComparatorOutput() {
        return 0;
    }

    public void addInstability(int instability) { }
}
