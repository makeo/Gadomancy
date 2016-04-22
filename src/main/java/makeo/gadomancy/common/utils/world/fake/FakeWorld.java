package makeo.gadomancy.common.utils.world.fake;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import java.io.File;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 16.10.2015 22:15
 */
public class FakeWorld extends World {

    private static final WorldInfo fakeInfo = new WorldInfo(new WorldSettings(0, WorldSettings.GameType.SURVIVAL, false, false, WorldType.DEFAULT), "FAKE");

    public FakeWorld(WorldProvider provider, boolean clientWorld) {
        super(FakeSaveHandler.instance, fakeInfo, provider, new Profiler(), clientWorld);
        theProfiler.profilingEnabled = false;
        getWorldInfo().setDifficulty(EnumDifficulty.NORMAL);
    }

    public FakeWorld(boolean clientWorld) {
        super(FakeSaveHandler.instance, fakeInfo, FakeWorldProvider.instance, new Profiler(), clientWorld);
        getWorldInfo().setDifficulty(EnumDifficulty.NORMAL);
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return Blocks.air.getBlockState().getBaseState();
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected int getRenderDistanceChunks() {
        return 12;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_) {
        return null;
    }

    @Override
    public float getLightBrightness(BlockPos pos) {
        return super.getLightBrightness(pos); //TODO uhm HELP?
    }

    private static class FakeWorldInfo extends WorldInfo {
        private static FakeWorldInfo instance = new FakeWorldInfo();
    }

    private static class FakeWorldProvider extends WorldProvider {

        private static FakeWorldProvider instance = new FakeWorldProvider();

        @Override
        public String getDimensionName() {
            return "FAKEWORLD";
        }

        @Override
        public String getInternalNameSuffix() {
            return "FAKE";
        }
    }

    private static class FakeSaveHandler implements ISaveHandler {
        private static FakeSaveHandler instance = new FakeSaveHandler();

        @Override
        public WorldInfo loadWorldInfo() {
            return FakeWorldInfo.instance;
        }

        @Override
        public void checkSessionLock() throws MinecraftException {
        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
        }

        @Override
        public void saveWorldInfo(WorldInfo p_75761_1_) {
        }

        @Override
        public IPlayerFileData getPlayerNBTManager() {
            return null;
        }

        @Override
        public void flush() {}

        @Override
        public File getWorldDirectory() {
            return null;
        }

        @Override
        public File getMapFileFromName(String p_75758_1_) {
            return null;
        }

        @Override
        public String getWorldDirectoryName() {
            return null;
        }
    }
}
