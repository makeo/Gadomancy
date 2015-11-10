package makeo.gadomancy.common.utils.world.fake;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
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

    public FakeWorld(WorldProvider provider) {
        super(FakeSaveHandler.instance, "", null, provider, new Profiler());
        theProfiler.profilingEnabled = false;
        difficultySetting = EnumDifficulty.NORMAL;
    }

    public FakeWorld() {
        super(FakeSaveHandler.instance, "", null, FakeWorldProvider.instance, null);
        difficultySetting = EnumDifficulty.NORMAL;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return Blocks.air;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected int func_152379_p() {
        return 0;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_) {
        return null;
    }

    @Override
    public int getSkyBlockTypeBrightness(EnumSkyBlock p_72925_1_, int p_72925_2_, int p_72925_3_, int p_72925_4_) {
        return 15;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        return 0;
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
        public IPlayerFileData getSaveHandler() {
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
