package makeo.gadomancy.common.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 27.10.2015 13:56
 */
public class MultiblockHelper {

    public static boolean isAnyMultiblockPresent(World world, BlockPos pos, List<MultiblockPattern> patternList) {
        for(MultiblockPattern pattern : patternList) {
            if(isMultiblockPresent(world, pos, pattern)) return true;
        }
        return false;
    }

    public static boolean isMultiblockPresent(World world, BlockPos pos, MultiblockPattern pattern) {
        for(IntVec3 vec : pattern.keySet()) {
            IBlockState expected = pattern.get(vec);
            IBlockState state = world.getBlockState(pos.add(vec.x, vec.y, vec.z));
            if(!expected.equals(state)) return false;
        }
        return true;
    }

    public static class MultiblockPattern extends LinkedHashMap<IntVec3, IBlockState> {

        public MultiblockPattern(IBlockState state) {
            put(createIntVec3(0, 0, 0), state);
        }

        public MultiblockPattern addBlock(int relativeX, int relativeY, int relativeZ, IBlockState state) {
            put(createIntVec3(relativeX, relativeY, relativeZ), state);
            return this;
        }

        private IntVec3 createIntVec3(int x, int y, int z) {
            return new IntVec3(x, y, z);
        }

    }

    public static class IntVec3 {
        public int x, y, z;

        public IntVec3(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntVec3 intVec3 = (IntVec3) o;
            return x == intVec3.x && y == intVec3.y && z == intVec3.z;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
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
