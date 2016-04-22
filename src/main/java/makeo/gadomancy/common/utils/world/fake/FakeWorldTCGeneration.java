package makeo.gadomancy.common.utils.world.fake;

import makeo.gadomancy.common.data.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import thaumcraft.api.blocks.BlocksTC;
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
        super(TCFakeWorldProvider.instance, false);
    }

    public ChunkMap chunks = new ChunkMap();
    public List<Object> bufferedEntities = new ArrayList<Object>();
    public Map<BlockPos, TileEntity> gettedTE = new HashMap<BlockPos, TileEntity>();
    //public int blockCount = 0, blockOverwriteCount = 0;

    /*@Override
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
            BlockPos pos = new BlockPos(x, y, z);
            if(gettedTE.containsKey(pos)) {
                gettedTE.remove(pos);
            }
        }

        /*if(block == ConfigBlocks.blockEldritchNothing) {
            if(BlockUtils.isBlockExposed(this, x, y, z)) {
                meta = 1;
            }
        }

        buf.blockData[key] = block;
        buf.metaBuffer[key] = (byte) meta;
        if(block.hasTileEntity(block.getStateFromMeta(meta))) buf.tiles.add(new Integer[] {x, y, z, key});
        return true;
    }*/

    /*@Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int metadata, int flags) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        buf.metaBuffer[key] = (byte) metadata;
        return true;
    }*/

    private void setBufferedMeta(int x, int y, int z, int metadata) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        buf.metaBuffer[key] = (byte) metadata;
    }

    private void setBufferedBlock(int x, int y, int z, Block b) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        buf.blockData[key] = b;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Block b = newState.getBlock();
        int meta = b.getMetaFromState(newState);
        if(b == BlocksTC.eldritch && (meta == 3 || meta == 2)) {
            return true;
        }
        setBufferedMeta(x, y, z, meta);
        setBufferedBlock(x, y, z, b);
        if(b.hasTileEntity(newState)) {
            long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
            ChunkBuffer buf = chunks.get(chKey);
            int keyX = (x & 15) << 7 << 4;
            int keyZ = (z & 15) << 7;
            int key = keyX | keyZ | y;
            buf.tiles.add(new Integer[] {x, y, z, key});
        }
        return true;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Block b = getBufferedBlock(x, y, z);
        int meta = getBufferedMeta(x, y, z);
        return b.getStateFromMeta(meta);
    }

    public Block getBufferedBlock(int x, int y, int z) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;
        //int key = ((x & 15) << 4 | (z & 15)) << 7 | y;

        Block b = buf.blockData[key];
        return b == null ? Blocks.air : b;
    }

    private int getBufferedMeta(int x, int y, int z) {
        long chKey = ((long) x >> 4) | ((long) z >> 4) << 32;
        ChunkBuffer buf = chunks.get(chKey);
        int keyX = (x & 15) << 7 << 4;
        int keyZ = (z & 15) << 7;
        int key = keyX | keyZ | y;

        return buf.metaBuffer[key];
    }

    @Override
    public boolean canBlockSeeSky(BlockPos pos) {
        return false;
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
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
        public void setLightFor(EnumSkyBlock type, BlockPos pos, int light) {
            if(type == EnumSkyBlock.BLOCK) {
                int key = ((pos.getX() & 15) << 4 | (pos.getZ() & 15)) << 7 | pos.getY();
                buf.lightData[key] = light;
            }
        }
    }

    @Override
    public int getLightFor(EnumSkyBlock type, BlockPos pos) {
        if(type == EnumSkyBlock.BLOCK) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            ChunkBuffer buf = chunks.get(((long) x >> 4) | ((long) z >> 4) << 32);

            int keyX = (x & 15) << 7 << 4;
            int keyZ = (z & 15) << 7;
            int key = keyX | keyZ | y;
            return buf.lightData[key];
        }
        return 0;
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return getBufferedBlock(pos.getX(), pos.getY(), pos.getZ()) == Blocks.air;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side) {
        return getBufferedBlock(pos.getX(), pos.getY(), pos.getZ()).isSideSolid(this, pos, side);
    }

    @Override
    public Chunk getChunkFromBlockCoords(BlockPos pos) {
        return new Chunk(this, 0, 0);
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        TileEntity te = getBufferedBlock(pos.getX(), pos.getY(), pos.getZ()).createTileEntity(this, getBlockState(pos));
        if(te == null) return null;
        if(!gettedTE.containsKey(pos)) gettedTE.put(pos, te);
        te.setWorldObj(this);
        te.setPos(pos);
        return te;
    }

    @Override
    public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {}

    @Override
    public void updateComparatorOutputLevel(BlockPos pos, Block blockIn) {}

    @Override
    public boolean spawnEntityInWorld(Entity entity) {
        if(entity instanceof EntityPermanentItem) {
            bufferedEntities.add(new EntityPermItem(entity.posX, entity.posY, entity.posZ, ((EntityPermanentItem) entity).getEntityItem()));
        } else if(entity instanceof EntityEldritchGuardian) {
            bufferedEntities.add(new EntityGuardianBuf(((EntityEldritchGuardian) entity).getHomePosition(), entity.posX, entity.posY, entity.posZ, ((EntityEldritchGuardian) entity).getMaximumHomeDistance()));
        }
        return true;
    }

    @Override
    public BlockPos getHeight(BlockPos pos) {
        return new BlockPos(pos.getX(), 0, pos.getZ());
    }

    @Override
    public void notifyBlockOfStateChange(BlockPos pos, Block blockIn) {}

    public static class EntityGuardianBuf {

        public BlockPos coordinates;
        public double x, y, z, dst;

        public EntityGuardianBuf(BlockPos coordinates, double x, double y, double z, double dst) {
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
            //instance.dimensionId = ModConfig.dimOuterId;
            instance.hasNoSky = true;
        }

        @Override
        public String getDimensionName() {
            return "FAKEWORLD-TC";
        }

        @Override
        public String getInternalNameSuffix() {
            return "FW-TC";
        }
    }

}
