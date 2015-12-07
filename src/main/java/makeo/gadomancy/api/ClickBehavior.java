package makeo.gadomancy.api;

import net.minecraft.block.Block;
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
    protected Block block;
    protected int x, y, z, metadata;

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
    public void init(World world, Block block, int x, int y, int z, int metadata) {
        this.world = world;
        this.block = block;
        this.x = x;
        this.y = y;
        this.z = z;
        this.metadata = metadata;
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
