package makeo.gadomancy.common.utils;

import net.minecraft.util.BlockPos;

/**
 * HellFirePvP@Admin
 * Date: 21.04.2016 / 01:29
 * on Gadomancy_1_8
 * PosSerializer
 */
public class PosSerializer {

    public static String serialize(BlockPos pos) {
        return pos.getX() + ";" + pos.getY() + ";" + pos.getZ();
    }

    public static BlockPos deserialize(String serialized) {
        String[] spl = serialized.split(";");
        return new BlockPos(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Integer.parseInt(spl[2]));
    }

}
