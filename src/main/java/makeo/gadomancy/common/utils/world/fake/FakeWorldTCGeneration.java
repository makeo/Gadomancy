package makeo.gadomancy.common.utils.world.fake;

import makeo.gadomancy.common.data.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 08.11.2015 21:57
 */
public class FakeWorldTCGeneration extends FakeWorld {

    public FakeWorldTCGeneration() {
        super(TCFakeWorldProvider.instance);
    }

    public ChunkMap chunks = new ChunkMap();
    public List<Object> bufferedEntities = new ArrayList<Object>();
    public Map<ChunkCoordinates, TileEntity> gettedTE = new HashMap<ChunkCoordinates, TileEntity>();
    public int blockCount = 0, blockOverwriteCount = 0;

    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int flags) {
        if(block == ConfigBlocks.blockEldritchPortal) return true;
        if(block == ConfigBlocks.blockEldritch && (meta == 1 || meta == 2 || meta == 3)) return true;

        blockCount++;

        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        if(buf.blockData[key] != null) {
            blockOverwriteCount++;
            ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
            if(gettedTE.containsKey(cc)) {
                gettedTE.remove(cc);
            }
        }

        /*if(block == ConfigBlocks.blockEldritchNothing) {
            if(BlockUtils.isBlockExposed(this, x, y, z)) {
                meta = 1;
            }
        }*/

        buf.blockData[key] = block;
        buf.metaBuffer[key] = (byte) meta;
        if(block.hasTileEntity(meta)) buf.tiles.add(new Integer[] {x, y, z, key});
        return true;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int metadata, int flags) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        buf.metaBuffer[key] = (byte) metadata;
        return true;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;
        //int key = ((x & 15) << 4 | (z & 15)) << 7 | y;

        Block b = buf.blockData[key];
        return b == null ? Blocks.air : b;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        return buf.metaBuffer[key];
    }

    @Override
    public boolean canBlockSeeTheSky(int p_72937_1_, int p_72937_2_, int p_72937_3_) {
        return false;
    }

    @Override
    protected boolean chunkExists(int p_72916_1_, int p_72916_2_) {
        return true;
    }

    @Override
    public Chunk getChunkFromChunkCoords(int x, int z) {
        long key = ((long)x) | (((long)z) << 32);
        return new FakeChunk(this, x, z, chunks.get(key));
    }

    private static class FakeChunk extends Chunk {
        private ChunkBuffer buf;

        public FakeChunk(World world, int x, int z, ChunkBuffer buf) {
            super(world, x, z);
            this.buf = buf;
        }

        @Override
        public void setLightValue(EnumSkyBlock type, int x, int y, int z, int light) {
            if(type == EnumSkyBlock.Block) {
                int key = ((x & 15) << 4 | (z & 15)) << 7 | y;
                buf.lightData[key] = light;
            }
        }
    }

    @Override
    public int getSavedLightValue(EnumSkyBlock type, int x, int y, int z) {
        if(type == EnumSkyBlock.Block) {
            ChunkBuffer buf = chunks.get(((long) x >> 4) | ((long) z >> 4) << 32);

            int keyX = (x & 15) << 7 << 4;
            int keyZ = (z & 15) << 7;
            int key = keyX | keyZ | y;
            return buf.lightData[key];
        }
        return 0;
    }

    @Override
    public boolean isAirBlock(int x, int y, int z) {
        return getBlock(x, y, z) == Blocks.air;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side) {
        return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
    }

    @Override
    public Chunk getChunkFromBlockCoords(int p_72938_1_, int p_72938_2_) {
        return new Chunk(this, 0, 0);
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        TileEntity te = getBlock(x, y, z).createTileEntity(null, getBlockMetadata(x, y, z));
        if(te == null) return null;
        ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
        if(!gettedTE.containsKey(cc)) gettedTE.put(cc, te);
        te.setWorldObj(this);
        te.xCoord = x;
        te.yCoord = y;
        te.zCoord = z;
        return te;
    }

    @Override
    public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_) {}

    @Override
    public void func_147453_f(int p_147453_1_, int p_147453_2_, int p_147453_3_, Block p_147453_4_) {}

    @Override
    public boolean spawnEntityInWorld(Entity entity) {
        if(entity instanceof EntityPermanentItem) {
            bufferedEntities.add(new EntityPermItem(entity.posX, entity.posY, entity.posZ, ((EntityPermanentItem) entity).getEntityItem()));
        } else if(entity instanceof EntityEldritchGuardian) {
            bufferedEntities.add(new EntityGuardianBuf(((EntityEldritchGuardian) entity).getHomePosition(), entity.posX, entity.posY, entity.posZ, ((EntityEldritchGuardian) entity).func_110174_bM()));
        }
        return true;
    }

    //TC eldrich guardian on spawn egg
    @Override
    public float func_147462_b(double p_147462_1_, double p_147462_3_, double p_147462_5_) {
        return 0;
    }

    @Override
    public int getHeightValue(int p_72976_1_, int p_72976_2_) {
        return 0;
    }

    @Override
    public void notifyBlockChange(int p_147444_1_, int p_147444_2_, int p_147444_3_, Block p_147444_4_) {}

    public static class EntityGuardianBuf {

        public ChunkCoordinates coordinates;
        public double x, y, z, dst;

        public EntityGuardianBuf(ChunkCoordinates coordinates, double x, double y, double z, double dst) {
            this.coordinates = coordinates;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dst = dst;
        }
    }

    public static class EntityPermItem {

        public double x, y, z;
        public ItemStack buffItemStack;

        public EntityPermItem(double x, double y, double z, ItemStack buffItemStack) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.buffItemStack = buffItemStack;
        }
    }

    public static class ChunkBuffer {
        private long key;
        public Block[] blockData = new Block[32768];
        public byte[] metaBuffer = new byte[32768];
        public List<Integer[]> tiles = new ArrayList<Integer[]>();
        public int[] lightData = new int[32768];

        public ChunkBuffer(long key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkBuffer that = (ChunkBuffer) o;
            return key == that.key;
        }

        @Override
        public int hashCode() {
            return (int) (key ^ (key >>> 32));
        }
    }

    public static class ChunkMap extends HashMap<Long, ChunkBuffer> {
        @Override
        public ChunkBuffer get(Object key) {
            if(!containsKey(key)) {
                put((Long) key, new ChunkBuffer((Long) key));
            }
            return super.get(key);
        }
    }

    public static class TCFakeWorldProvider extends WorldProvider {
        private static TCFakeWorldProvider instance;

        static {
            instance = new TCFakeWorldProvider();
            instance.dimensionId = ModConfig.dimOuterId;
            instance.hasNoSky = true;
        }

        @Override
        public String getDimensionName() {
            return "FAKEWORLD-TC";
        }
    }

}
