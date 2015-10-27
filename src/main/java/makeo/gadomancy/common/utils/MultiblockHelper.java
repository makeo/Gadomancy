package makeo.gadomancy.common.utils;

import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.LinkedHashMap;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 27.10.2015 13:56
 */
public class MultiblockHelper {

    public static boolean isMultiblockPresent(World world, int x, int y, int z, MultiblockPattern pattern) {
        for(Vec3 vec : pattern.keySet()) {
            BlockInfo expected = pattern.get(vec);
            Block realBlock = world.getBlock(x + (int) vec.xCoord, y + (int) vec.yCoord, z + (int) vec.zCoord);
            int realMeta = world.getBlockMetadata(x + (int) vec.xCoord, y + (int) vec.yCoord, z + (int) vec.zCoord);
            if(expected.block != realBlock || expected.meta != realMeta) return false;
        }
        return true;
    }

    public static class MultiblockPattern extends LinkedHashMap<Vec3, BlockInfo> {

        public MultiblockPattern(Block originBlock, int originMeta) {
            put(createIntVec3(0, 0, 0), new BlockInfo(originBlock, originMeta));
        }

        public MultiblockPattern addBlock(int relativeX, int relativeY, int relativeZ, Block expectedBlock, int expectedMeta) {
            put(createIntVec3(relativeX, relativeY, relativeZ), new BlockInfo(expectedBlock, expectedMeta));
            return this;
        }

        private Vec3 createIntVec3(int x, int y, int z) {
            return Vec3.createVectorHelper(x, y, z);
        }

    }

    public static class BlockInfo {
        public Block block;
        public int meta;

        private BlockInfo(Block block, int meta) {
            this.block = block;
            this.meta = meta;
        }
    }
}
