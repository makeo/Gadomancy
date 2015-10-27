package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileInfusionPillar;

import java.awt.*;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 27.10.2015 13:21
 */
public class TileManipulatorPillar extends TileInfusionPillar {

    private static final Color[] runeColors = new Color[] { new Color(0xB60707), new Color(0xEE3782), new Color(0xF37B20), new Color(0xFF6722), new Color(0xFF6D30), new Color(0xFF0000) };

    public static void doRuneEffects(World world, int x, int y, int z) {
        if(world.isRemote) {
            int cnt = 20;
            while(cnt > 0) {
                cnt--;
                Color rand = runeColors[world.rand.nextInt(runeColors.length)];
                Thaumcraft.proxy.blockRunes(world, x + randomOffset(world), y + randomOffset(world), z + randomOffset(world),
                        rand.getRed() / 255F, rand.getGreen() / 255F, rand.getBlue() / 255F, world.rand.nextInt(10) + 30, -0.01F - (world.rand.nextBoolean() ? world.rand.nextBoolean() ? 0.02F : 0.01F : 0F));
            }
        }
    }

    private static float randomOffset(World worldObj) {
        return (worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1)) / 2F;
    }

}