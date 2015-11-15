package makeo.gadomancy.common.utils.world;

import makeo.gadomancy.common.blocks.tiles.TileOverrideEldritchLock;
import makeo.gadomancy.common.utils.world.fake.FakeWorldTCGeneration;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileCrystal;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;
import thaumcraft.common.tiles.TileEldritchLock;
import thaumcraft.common.tiles.TileEldritchNothing;

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

    public Chunk loadChunk(int x, int z) {
        return provideChunk(x, z);
    }

    public Chunk provideChunk(int x, int z) {
        Chunk chunk;
        long key = ((long) x) | ((long) z) << 32;
        if(TCMazeHandler.GEN.chunks.containsKey(key)) {
            FakeWorldTCGeneration.ChunkBuffer buf = TCMazeHandler.GEN.chunks.get(key);

            for(int i = 0; i < buf.blockData.length; i++) {
                if(buf.blockData[i] == ConfigBlocks.blockEldritchNothing) {
                    ConfigBlocks.blockEldritchNothing.onNeighborBlockChange(TCMazeHandler.GEN, (i >> 11 & 15) | (x << 4), i & 127, (i >> 7 & 15) | (z << 4), ConfigBlocks.blockEldritchNothing);
                }
            }

            TCMazeHandler.GEN.chunks.remove(key);

            chunk = new Chunk(worldObj, buf.blockData, buf.metaBuffer, x, z);

            byte[] abyte = chunk.getBiomeArray();
            System.arraycopy(biomeArr, 0, abyte, 0, biomeArr.length);

            for(Integer[] pos : buf.tiles) {
                TileEntity te = buf.blockData[pos[3]].createTileEntity(worldObj, buf.metaBuffer[pos[3]]);
                if(te != null) {
                    if(te instanceof TileEldritchLock) {
                        te = new TileOverrideEldritchLock();
                    }

                    worldObj.addTileEntity(te);
                    chunk.func_150812_a(pos[0] & 15, pos[1], pos[2] & 15, te);
                    te.blockMetadata = buf.metaBuffer[pos[3]];
                }
            }

            Iterator<ChunkCoordinates> it = TCMazeHandler.GEN.gettedTE.keySet().iterator();
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
            }

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
                    guardian.setHomeArea(b.coordinates.posX, b.coordinates.posY, b.coordinates.posZ, (int) b.dst);
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
                            chunk.setLightValue(EnumSkyBlock.Block, cX, cY, cZ, lightVal);
                        }
                    }
                }
            }

        } else {
            Block[] ablock = new Block[blockArr.length];
            System.arraycopy(blockArr, 0, ablock, 0, blockArr.length);
            byte[] meta = new byte[metaArr.length];
            System.arraycopy(metaArr, 0, meta, 0, metaArr.length);

            chunk = new Chunk(this.worldObj, ablock, meta, x, z);
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
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
        biomegenbase.decorate(this.worldObj, this.worldObj.rand, k, l);

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(p_73153_1_, this.worldObj, this.worldObj.rand, p_73153_2_, p_73153_3_, false));
        net.minecraft.block.BlockFalling.fallInstantly = false;
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

    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }

    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
        return null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_) {}

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
            biomeArr[i] = (byte) ThaumcraftWorldGenerator.biomeEldritchLands.biomeID;
        }
    }
}
