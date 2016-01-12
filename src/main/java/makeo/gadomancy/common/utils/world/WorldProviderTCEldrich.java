package makeo.gadomancy.common.utils.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.data.config.ModConfig;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 03.11.2015 19:02
 */
public class WorldProviderTCEldrich extends WorldProvider {

    @Override
    public String getDimensionName() {
        return "Outer Lands";
    }

    public String getWelcomeMessage() {
        return "Entering The Outer Lands";
    }

    public String getDepartMessage() {
        return "Leaving The Outer Lands";
    }

    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return true;
    }

    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        return false;
    }

    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        return false;
    }

    public boolean canDoLightning(Chunk chunk) {
        return false;
    }

    public boolean canDoRainSnowIce(Chunk chunk) {
        return false;
    }

    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(ThaumcraftWorldGenerator.biomeEldritchLands, 0.0F);
        this.dimensionId = ModConfig.dimOuterId;
        this.hasNoSky = true;
    }

    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderTCOuter(this.worldObj, this.worldObj.getSeed(), true);
    }

    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        int i = 10518688;
        float f2 = MathHelper.cos(p_76562_1_ * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }
        float f3 = (i >> 16 & 0xFF) / 255.0F;
        float f4 = (i >> 8 & 0xFF) / 255.0F;
        float f5 = (i & 0xFF) / 255.0F;
        f3 *= (f2 * 0.0F + 0.15F);
        f4 *= (f2 * 0.0F + 0.15F);
        f5 *= (f2 * 0.0F + 0.15F);
        return Vec3.createVectorHelper(f3, f4, f5);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    public boolean canRespawnHere() {
        return false;
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 1.0F;
    }

    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
    }

    public ChunkCoordinates getEntrancePortalLocation() {
        return null;
    }

    public int getAverageGroundLevel() {
        return 50;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        return true;
    }
}
