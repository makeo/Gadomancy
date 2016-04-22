package makeo.gadomancy.common.registration;

import net.minecraft.block.state.IBlockState;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 21.07.2015 02:31
 */
public class StickyJarInfo {
    private IBlockState blockState;
    private boolean needsLabelHandling;
    private boolean needsPhialHandling;

    public StickyJarInfo(IBlockState state, boolean needsLabelHandling, boolean needsPhialHandling) {
        this.blockState = state;
        this.needsLabelHandling = needsLabelHandling;
        this.needsPhialHandling = needsPhialHandling;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public boolean needsLabelHandling() {
        return needsLabelHandling;
    }

    public boolean needsPhialHandling() {
        return needsPhialHandling;
    }
}
