package makeo.gadomancy.common.utils.world;

import makeo.gadomancy.common.utils.world.fake.FakeWorldTCGeneration;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.biomes.BiomeHandler;
import thaumcraft.common.tiles.misc.TileEldritchLock;

import java.util.Iterator;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 06.11.2015 23:39
 */
public class ChunkProviderTCOuter implements IChunkProvider {

    private static final Block[] blockArr;
    private static final byte[] metaArr;
    private static final byte[] biomeArr;

    private World worldObj;

    public ChunkProviderTCOuter(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_) {
        this.worldObj = p_i2006_1_;
    }

    public Chunk provideChunk(int x, int z) {
        return provideChunk(new BlockPos(x, 0, z));
    }

    @Override
    public Chunk provideChunk(BlockPos pos) {
        Chunk chunk;
        int x = pos.getX();
        int z = pos.getZ();
        long key = ((long) x) | ((long) z) << 32;
        if(TCMazeHandler.GEN.chunks.containsKey(key)) {
            FakeWorldTCGeneration.ChunkBuffer buf = TCMazeHandler.GEN.chunks.get(key);

            /*for(int i = 0; i < buf.blockData.length; i++) {
                if(buf.blockData[i] == BlocksTCConfigBlocks.blockEldritchNothing) {
                    ConfigBlocks.blockEldritchNothing.onNeighborBlockChange(TCMazeHandler.GEN, (i >> 11 & 15) | (x << 4), i & 127, (i >> 7 & 15) | (z << 4), ConfigBlocks.blockEldritchNothing);
                }
            }*/

            TCMazeHandler.GEN.chunks.remove(key);

            ChunkPrimer primer = new ChunkPrimer();
            chunk = new Chunk(worldObj, primer, x, z);

            byte[] abyte = chunk.getBiomeArray();
            System.arraycopy(biomeArr, 0, abyte, 0, biomeArr.length);

            /*for(Integer[] pos : buf.tiles) {
                TileEntity te = buf.blockData[pos[3]].createTileEntity(worldObj, buf.metaBuffer[pos[3]]);
                if(te != null) {
                    if(te instanceof TileEldritchLock) {
                        te = new TileOverrideEldritchLock();
                    }

                    worldObj.addTileEntity(te);
                    chunk.func_150812_a(pos[0] & 15, pos[1], pos[2] & 15, te);
                    te.blockMetadata = buf.metaBuffer[pos[3]];
                }
            }*/

            /*Iterator<ChunkCoordinates> it = TCMazeHandler.GEN.gettedTE.keySet().iterator();
            while(it.hasNext()) {
                ChunkCoordinates cc = it.next();
                TileEntity te = TCMazeHandler.GEN.gettedTE.get(cc);
                if(te.xCoord >> 4 != x || te.zCoord >> 4 != z) continue;
                ChunkPosition pos = new ChunkPosition(te.xCoord & 15, te.yCoord, te.zCoord & 15);
                if(!chunk.chunkTileEntityMap.containsKey(pos)) {
                    System.out.println("INVALID TE at: " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " --- Not found in its parent chunk " + chunk.xPosition + ", " + chunk.zPosition);
                    throw new IllegalStateException("INVALID TE");
                }
                TileEntity correspondingTE = (TileEntity) chunk.chunkTileEntityMap.get(pos);
                if(te instanceof TileEntityMobSpawner) {
                    if(!(correspondingTE instanceof TileEntityMobSpawner)) {
                        System.out.println("DATA INCONSISTENCY - TE at " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " EXPECTED: " + te.getClass().getSimpleName() + " GIVEN " + correspondingTE.getClass().getSimpleName());
                        throw new IllegalStateException();
                    }

                    ((TileEntityMobSpawner) correspondingTE).func_145881_a().setEntityName(((TileEntityMobSpawner) te).func_145881_a().getEntityNameToSpawn());
                } else if(te instanceof TileEldritchLock) {
                    if(!(correspondingTE instanceof TileEldritchLock)) {
                        System.out.println("DATA INCONSISTENCY - TE at " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " EXPECTED: " + te.getClass().getSimpleName() + " GIVEN " + correspondingTE.getClass().getSimpleName());
                        throw new IllegalStateException();
                    }

                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setByte("facing", ((TileEldritchLock) te).getFacing());
                    compound.setShort("count", (short) -1);
                    ((TileEldritchLock) correspondingTE).readCustomNBT(compound);
                } else if(te instanceof TileCrystal) {
                    if(!(correspondingTE instanceof TileCrystal)) {
                        System.out.println("DATA INCONSISTENCY - TE at " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " EXPECTED: " + te.getClass().getSimpleName() + " GIVEN " + correspondingTE.getClass().getSimpleName());
                        throw new IllegalStateException();
                    }

                    ((TileCrystal) correspondingTE).orientation = ((TileCrystal) te).orientation;
                } else if(te instanceof TileEldritchCrabSpawner) {
                    if(!(correspondingTE instanceof TileEldritchCrabSpawner)) {
                        System.out.println("DATA INCONSISTENCY - TE at " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " EXPECTED: " + te.getClass().getSimpleName() + " GIVEN " + correspondingTE.getClass().getSimpleName());
                        throw new IllegalStateException();
                    }

                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setByte("facing", ((TileEldritchCrabSpawner) te).getFacing());
                    ((TileEldritchCrabSpawner) correspondingTE).readCustomNBT(compound);
                } else if(te instanceof TileEldritchNothing) {
                    if (!(correspondingTE instanceof TileEldritchNothing)) {
                        System.out.println("DATA INCONSISTENCY - TE at " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " EXPECTED: " + te.getClass().getSimpleName() + " GIVEN " + correspondingTE.getClass().getSimpleName());
                        throw new IllegalStateException();
                    }
                } else {
                    throw new IllegalStateException("Unexpected TileEntity: " + te);
                }
                it.remove();
            }*/

            Iterator<Object> itEntity = TCMazeHandler.GEN.bufferedEntities.iterator();
            while(itEntity.hasNext()) {
                Object bufE = itEntity.next();

                if(bufE instanceof FakeWorldTCGeneration.EntityPermItem) {
                    FakeWorldTCGeneration.EntityPermItem i = (FakeWorldTCGeneration.EntityPermItem) bufE;
                    if(((int) i.x >> 4) != x || ((int) i.z >> 4) != z) continue;
                    EntityPermanentItem item = new EntityPermanentItem(worldObj, i.x, i.y, i.z, i.buffItemStack);
                    item.motionX = 0;
                    item.motionY = 0;
                    item.motionZ = 0;
                    chunk.addEntity(item);
                    itEntity.remove();
                } else if(bufE instanceof FakeWorldTCGeneration.EntityGuardianBuf) {
                    FakeWorldTCGeneration.EntityGuardianBuf b = (FakeWorldTCGeneration.EntityGuardianBuf) bufE;
                    if(((int) b.x >> 4) != x || ((int) b.z >> 4) != z) continue;
                    EntityEldritchGuardian guardian = new EntityEldritchGuardian(worldObj);
                    guardian.setPosition(b.x, b.y, b.z);
                    guardian.setHomePosAndDistance(b.coordinates, (int) b.dst);
                    //guardian.setHomeArea(b.coordinates.posX, b.coordinates.posY, b.coordinates.posZ, (int) b.dst);
                    chunk.addEntity(guardian);
                    itEntity.remove();
                } else {
                    throw new IllegalStateException("Unexpected Entity: " + bufE);
                }
            }

            for(int cX = 0; cX < 16; cX++) {
                for(int cY = 50; cY < 64; cY++) {
                    for(int cZ = 0; cZ < 16; cZ++) {
                        int lightKey = ((cX & 15) << 4 | (cZ & 15)) << 7 | cY;
                        int lightVal = buf.lightData[lightKey];
                        if(lightVal > 0) {
                            chunk.setLightFor(EnumSkyBlock.BLOCK, new BlockPos(cX, cY, cZ), lightVal);
                            //chunk.setLightValue(EnumSkyBlock.Block, cX, cY, cZ, lightVal);
                        }
                    }
                }
            }

        } else {
            //Block[] ablock = new Block[blockArr.length];
            //System.arraycopy(blockArr, 0, ablock, 0, blockArr.length);
            //byte[] meta = new byte[metaArr.length];
            //System.arraycopy(metaArr, 0, meta, 0, metaArr.length);

            chunk = new Chunk(this.worldObj, new ChunkPrimer(), x, z); //Empty chunk. well that was easy.
            byte[] abyte = chunk.getBiomeArray();
            System.arraycopy(biomeArr, 0, abyte, 0, biomeArr.length);
        }

        chunk.generateSkylightMap();

        return chunk;
    }

    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        net.minecraft.block.BlockFalling.fallInstantly = true;
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(p_73153_1_, this.worldObj, this.worldObj.rand, p_73153_2_, p_73153_3_, false));

        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(k + 16, 10, l + 16));
        biomegenbase.decorate(this.worldObj, this.worldObj.rand, new BlockPos(k, 0, l));

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(p_73153_1_, this.worldObj, this.worldObj.rand, p_73153_2_, p_73153_3_, false));
        net.minecraft.block.BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        return false;
    }

    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    public void saveExtraData() {}

    public boolean unloadQueuedChunks() {
        return false;
    }

    public boolean canSave() {
        return false;
    }

    public String makeString() {
        return "RandomLevelSource";
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType p_73155_1_, BlockPos p_177458_2_) {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_177458_2_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {

    }

    public static int getKey(int x, int y, int z) {
        return (x << 4 | z) << 7 | y;
    }

    static {
        blockArr = new Block[32768];
        metaArr = new byte[blockArr.length];
        biomeArr = new byte[16 * 16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 127; y >= 0; y--) {
                    int index = (x << 4 | z) << 7 | y;
                    blockArr[index] = null;
                    metaArr[index] = 0;
                }
            }
        }
        for(int i = 0; i < biomeArr.length; i++) {
            biomeArr[i] = (byte) BiomeHandler.biomeEldritchLands.biomeID;
        }
    }
}
