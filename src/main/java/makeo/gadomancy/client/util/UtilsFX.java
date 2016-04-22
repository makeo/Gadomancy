package makeo.gadomancy.client.util;

import makeo.gadomancy.common.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import thaumcraft.client.fx.FXDispatcher;
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

    public static void doRuneEffects(World world, BlockPos pos, int colorFlag) {
        doRuneEffects(world, pos.getX(), pos.getY(), pos.getZ(), colorFlag);
    }

    public static void doRuneEffects(World world, int x, int y, int z, int colorFlag) {
        if(world.isRemote) {
            int cnt = 20;
            while(cnt > 0) {
                cnt--;
                Color rand = evaluateRandomColor(world.rand, colorFlag);
                FXDispatcher fx = Thaumcraft.proxy.getFX();
                fx.blockRunes(x + randomOffset(world), y + randomOffset(world), z + randomOffset(world),
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

    public static ResourceLocation getParticleTexture() {
        try {
            return (ResourceLocation) ReflectionHelper.getPrivateValue(EffectRenderer.class, null, "particleTextures", "b", "field_110737_b");
        }
        catch (Exception e) {}
        return null;
    }

    private static float randomOffset(World worldObj) {
        return (worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1)) / 2F;
    }

    public static void doSparkleEffectsAround(World world, int x, int y, int z) {
        for(BlockPos pos : MiscUtils.getPositionsAround(new BlockPos(x, y, z))) {
            doSparkleEffects(world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static void doSparkleEffects(World world, int x, int y, int z) {
        float r, g, b;
        for (int i = 0; i < 10; i++) {
            r = 0.33F + world.rand.nextFloat() * 0.67F;
            g = 0.33F + world.rand.nextFloat() * 0.67F;
            b = 0.33F + world.rand.nextFloat() * 0.67F;
            float pX = z- 0.1F + world.rand.nextFloat() * 1.2F;
            float pY = y - 0.1F + world.rand.nextFloat() * 1.2F;
            float pZ = z - 0.1F + world.rand.nextFloat() * 1.2F;
            Thaumcraft.proxy.getFX().spark(pX, pY, pZ, 1.0F, r, g, b, 0.9F);
        }
    }

    public static void doSmokeEffects(World world, int x, int y, int z) {
        int count = world.rand.nextInt(6) + 2;
        for (int i = 0; i < count; i++) {
            Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(),
                    x + 0.5 + randEffectOffset(world.rand), y + 0.5 + randEffectOffset(world.rand), z + 0.5 + randEffectOffset(world.rand),
                    randEffectOffset(world.rand) * 0.01F, randEffectOffset(world.rand) * 0.1F, randEffectOffset(world.rand) * 0.01F);
        }
    }

    public static void doSmokeEffectsAround(World world, int x, int y, int z) {
        for(BlockPos pos : MiscUtils.getPositionsAround(new BlockPos(x, y, z))) {
            doSmokeEffects(world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private static double randEffectOffset(Random rand) {
        return rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
    }
}
