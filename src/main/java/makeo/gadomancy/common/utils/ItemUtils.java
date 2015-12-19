package makeo.gadomancy.common.utils;

import net.minecraft.entity.item.EntityItem;

import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 12.11.2015 23:13
 */
public class ItemUtils {

    public static void applyRandomDropOffset(EntityItem item, Random rand) {
        item.motionX = rand.nextFloat() * 0.7F - 0.35D;
        item.motionY = rand.nextFloat() * 0.7F - 0.35D;
        item.motionZ = rand.nextFloat() * 0.7F - 0.35D;
    }

}
