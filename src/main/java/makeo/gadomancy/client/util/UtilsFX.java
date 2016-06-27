package makeo.gadomancy.client.util;

import makeo.gadomancy.common.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.util.ChunkCoordinates;
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

    private static final Color[] RUNE_COLORS_RED = new Color[] { new Color(0xB60707), new Color(0xEE3782), new Color(0xF37B20), new Color(0xFF6722), new Color(0xFF6D30), new Color(0xFF0000) };
    private static final Color[] RUNE_COLORS_GREEN = new Color[] { new Color(0x015629), new Color(0x0E9C00), new Color(0x010000), new Color(0x01FF00), new Color(0x014C3E)};
    private static final Color[] RUNE_COLORS_BLUE = new Color[] { new Color(0x060956), new Color(0x012578), new Color(0x0C1556), new Color(0x0100FF), new Color(0x01018C)};

    public static void doRuneEffects(World world, int x, int y, int z, int colorFlag) {
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

    private static Color evaluateRandomColor(Random rand, int colorFlag) {
        Color[] possibleColors;
        if(colorFlag == 1) {
            possibleColors = RUNE_COLORS_BLUE;
        } else {
            possibleColors = RUNE_COLORS_RED;
        }
        return possibleColors[rand.nextInt(possibleColors.length)];
    }

    private static float randomOffset(World worldObj) {
        return (worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1)) / 2F;
    }

    public static void doSparkleEffectsAround(World world, int x, int y, int z) {
        for(ChunkCoordinates cc : MiscUtils.getCoordinatesAround(new ChunkCoordinates(x, y, z))) {
            doSparkleEffects(world, cc.posX, cc.posY, cc.posZ);
        }
    }

    public static void doSparkleEffects(World world, int x, int y, int z) {
        Thaumcraft.proxy.blockSparkle(world, x, y, z, -9999, 10);
    }

    public static void doSmokeEffects(World world, int x, int y, int z, float size) {
        int count = world.rand.nextInt(6) + 2;
        for (int i = 0; i < count; i++) {
            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new EntitySmokeFX(world, x + 0.5 + randEffectOffset(world.rand), y + 0.5 + randEffectOffset(world.rand), z + 0.5 + randEffectOffset(world.rand),
                            randEffectOffset(world.rand) * 0.01F, randEffectOffset(world.rand) * 0.1F, randEffectOffset(world.rand) * 0.01F, size));
        }
    }

    public static void doSmokeEffectsAround(World world, int x, int y, int z, float size) {
        for(ChunkCoordinates cc : MiscUtils.getCoordinatesAround(new ChunkCoordinates(x, y, z))) {
            doSmokeEffects(world, cc.posX, cc.posY, cc.posZ, size);
        }
    }

    private static double randEffectOffset(Random rand) {
        return rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
    }
}
