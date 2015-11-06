package makeo.gadomancy.client.util;

import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

import java.awt.*;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 30.10.2015 20:08
 */
public class UtilsFX {

    private static final Color[] runeColors_red = new Color[] { new Color(0xB60707), new Color(0xEE3782), new Color(0xF37B20), new Color(0xFF6722), new Color(0xFF6D30), new Color(0xFF0000) };
    private static final Color[] runeColors_green = new Color[] { new Color(0x005629), new Color(0x0E9C00), new Color(0x000000), new Color(0x00FF00), new Color(0x004C3E)};

    public static void doRuneEffects(World world, int x, int y, int z, byte colorFlag) {
        if(world.isRemote) {
            int cnt = 20;
            while(cnt > 0) {
                cnt--;
                Color rand = evaluateRandomColor(world.rand, colorFlag);
                Thaumcraft.proxy.blockRunes(world, x + randomOffset(world), y + randomOffset(world), z + randomOffset(world),
                        rand.getRed() / 255F, rand.getGreen() / 255F, rand.getBlue() / 255F, world.rand.nextInt(10) + 30, -0.01F - (world.rand.nextBoolean() ? world.rand.nextBoolean() ? 0.02F : 0.01F : 0F));
            }
        }
    }

    private static Color evaluateRandomColor(Random rand, byte colorFlag) {
        Color[] possibleColors;
        if(colorFlag == 1) {
            possibleColors = runeColors_green;
        } else {
            possibleColors = runeColors_red;
        }
        return possibleColors[rand.nextInt(possibleColors.length)];
    }

    private static float randomOffset(World worldObj) {
        return (worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1)) / 2F;
    }

    public static void doSparkleEffectsAround(World world, int x, int y, int z) {
        doSparkleEffects(world, x,     y,     z);
        doSparkleEffects(world, x + 1, y,     z);
        doSparkleEffects(world, x,     y,     z + 1);
        doSparkleEffects(world, x - 1, y,     z);
        doSparkleEffects(world, x,     y,     z - 1);
        doSparkleEffects(world, x,     y - 1, z);
        doSparkleEffects(world, x,     y + 1, z);
    }

    public static void doSparkleEffects(World world, int x, int y, int z) {
        Thaumcraft.proxy.blockSparkle(world, x, y, z, -9999, 10);
    }
}
