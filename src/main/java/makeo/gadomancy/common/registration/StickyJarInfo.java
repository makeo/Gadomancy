package makeo.gadomancy.common.registration;

import net.minecraft.block.Block;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 21.07.2015 02:31
 */
public class StickyJarInfo {
    private Block block;
    private int metadata;
    private boolean needsLabelHandling;
    private boolean needsPhialHandling;

    public StickyJarInfo(Block block, int metadata, boolean needsLabelHandling, boolean needsPhialHandling) {
        this.block = block;
        this.metadata = metadata;
        this.needsLabelHandling = needsLabelHandling;
        this.needsPhialHandling = needsPhialHandling;
    }

    public Block getBlock() {
        return block;
    }

    public int getMetadata() {
        return metadata;
    }

    public boolean needsLabelHandling() {
        return needsLabelHandling;
    }

    public boolean needsPhialHandling() {
        return needsPhialHandling;
    }
}
