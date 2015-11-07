package makeo.gadomancy.common.utils.world;

import makeo.gadomancy.common.data.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.WorldEvent;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.lib.world.dim.CellLoc;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 05.11.2015 10:40
 */
public class TCMazeHandler {

    //How maze gen works overall (for us):
    //Defining what chunks are currently occupied with the 'labyrinthCopy' map.
    //If a chunk is not listed in there, it's considered empty.
    //level saving itself is disabled. Players without active session will get teleported out.

    //Our map to work with.
    public static ConcurrentHashMap<CellLoc, Short> labyrinthCopy = new ConcurrentHashMap<CellLoc, Short>();
    public static final int TELEPORT_LAYER_Y = 55;

    private static Map<EntityPlayer, TCMazeSession> runningSessions = new HashMap<EntityPlayer, TCMazeSession>();

    public static void closeAllSessionsAndCleanup() {
        for (EntityPlayer pl : runningSessions.keySet()) {
            runningSessions.get(pl).closeSession(false);
        }
        init();
    }

    public static void tick() {
        WorldServer w = DimensionManager.getWorld(ModConfig.dimOuterId);
        if(w != null) {
            if(!w.levelSaving) w.levelSaving = true;
            //Chunks still get saved...

            WorldServer out = MinecraftServer.getServer().worldServerForDimension(0);
            List playerObjects = w.playerEntities;
            for (int i = 0; i < playerObjects.size(); i++) {
                EntityPlayer player = (EntityPlayer) playerObjects.get(i);
                if(!hasOpenSession(player)) {
                    WorldUtil.tryTeleportBack((EntityPlayerMP) player, 0);
                    ChunkCoordinates cc = out.getSpawnPoint();
                    player.setPosition(cc.posX + 0.5, cc.posY + 0.5, cc.posZ + 0.5);
                }
            }
            for(EntityPlayer player : runningSessions.keySet()) {
                if(player.worldObj.provider.dimensionId != ModConfig.dimOuterId) { //If the player left our dim, but he should still be in the session, ...
                    closeSession(player, false);
                }
            }
        }
    }

    /*
     *  Coordinates wanted here are the absolute Portal coordinates.
     */
    public static boolean createSessionAndTeleport(EntityPlayer player, int pX, int pY, int pZ) {
        if(hasOpenSession(player) || !hasFreeSessionSpace()) return false;
        WorldServer w = MinecraftServer.getServer().worldServerForDimension(ModConfig.dimOuterId);
        Map<CellLoc, Short> chunksAffected = setupChunks(w, pX, pZ);
        Vec3 currentPos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        int originDim = player.worldObj.provider.dimensionId;
        TCMazeSession session = new TCMazeSession(player, chunksAffected, originDim, currentPos);
        session.startSession();
        runningSessions.put(player, session);
        return true;
    }

    private static Map<CellLoc, Short> setupChunks(World world, int pX, int pZ) {
        ConcurrentHashMap<CellLoc, Short> oldDat = MazeHandler.labyrinth;
        ConcurrentHashMap<CellLoc, Short> bufferOld = new ConcurrentHashMap<CellLoc, Short>(labyrinthCopy);
        MazeHandler.labyrinth = labyrinthCopy;
        int chX = pX >> 4;
        int chZ = pZ >> 4;
        int w = randWH(world.rand);
        int h = randWH(world.rand);
        while (MazeHandler.mazesInRange(chX, chZ, w, h)) {
            chX++;
        }
        MazeThread mt = new MazeThread(chX, chZ, w, h, world.rand.nextLong());
        mt.run();
        Map<CellLoc, Short> locs = calculateDifferences(bufferOld);
        for(CellLoc l : locs.keySet()) {
            Cell c = new Cell(locs.get(l));
            boolean oldFlag = false;
            if(c.feature == 1) {
                oldFlag = world.captureBlockSnapshots;
                world.captureBlockSnapshots = true;
            }
            MazeHandler.generateEldritch(world, world.rand, l.x, l.z);
            if(c.feature == 1) {
                world.captureBlockSnapshots = oldFlag;
                ArrayList<BlockSnapshot> snapshots = world.capturedBlockSnapshots;
                for(BlockSnapshot sn : snapshots) {
                    if(sn.getCurrentBlock() == ConfigBlocks.blockEldritch && (sn.world.getBlockMetadata(sn.x, sn.y, sn.z) == 1 || sn.world.getBlockMetadata(sn.x, sn.y, sn.z) == 2 || sn.world.getBlockMetadata(sn.x, sn.y, sn.z) == 3)) {
                        sn.world.setBlockToAir(sn.x, sn.y, sn.z);
                    }
                    if(sn.getCurrentBlock() == ConfigBlocks.blockEldritchPortal) {
                        sn.world.setBlockToAir(sn.x, sn.y, sn.z);
                    }
                }
            }
        }
        labyrinthCopy = MazeHandler.labyrinth;
        MazeHandler.labyrinth = oldDat;
        return locs;
    }

    private static Map<CellLoc, Short> calculateDifferences(ConcurrentHashMap<CellLoc, Short> bufferOld) {
        ConcurrentHashMap<CellLoc, Short> newDat = MazeHandler.labyrinth; //Only the new data has data, the old one doesn't have.
        Map<CellLoc, Short> newlyEvaluatedMaze = new HashMap<CellLoc, Short>();
        for(CellLoc loc : newDat.keySet()) {
            if(!bufferOld.containsKey(loc)) {
                newlyEvaluatedMaze.put(loc, newDat.get(loc));
            }
        }
        return newlyEvaluatedMaze;
    }

    private static boolean hasFreeSessionSpace() {
        return ModConfig.maxMazeCount == -1 || runningSessions.size() < ModConfig.maxMazeCount;
    }

    private static int randWH(Random random) {
        return 15 + random.nextInt(8) * 2;
    }

    public static void init() {
        runningSessions = new HashMap<EntityPlayer, TCMazeSession>();
    }

    public static boolean hasOpenSession(EntityPlayer player) {
        return !player.worldObj.isRemote && runningSessions.get(player) != null;
    }

    public static void closeSession(EntityPlayer player, boolean teleport) {
        if(player.worldObj.isRemote) return;

        if(runningSessions.containsKey(player)) {
            runningSessions.get(player).closeSession(teleport);
            runningSessions.remove(player);
        }
        if(runningSessions.size() == 0) {
            forceWorldUnloading(ModConfig.dimOuterId);
        }
    }

    public static void free(Map<CellLoc, Short> locations) {
        WorldServer ws = DimensionManager.getWorld(ModConfig.dimOuterId);
        for(CellLoc loc : locations.keySet()) {
            labyrinthCopy.remove(loc);
            forceChunkUnloading(ws, loc.x, loc.z);
        }
    }

    private static void forceChunkUnloading(WorldServer ws, int chX, int chZ) {
        if(ws == null) return;
        long chunkPair = ChunkCoordIntPair.chunkXZ2Int(chX, chZ);
        ChunkProviderServer serverChProvider = ws.theChunkProviderServer;
        LongHashMap chunks = serverChProvider.loadedChunkHashMap;
        Chunk c = (Chunk) chunks.getValueByKey(chunkPair);

        serverChProvider.loadedChunkHashMap.remove(chunkPair);
        if(c != null) serverChProvider.loadedChunks.remove(c);

        if(serverChProvider.loadedChunks.size() == 0) {
            forceWorldUnloading(ws.provider.dimensionId);
        }
    }

    private static void forceWorldUnloading(int dimId) {
        WorldServer ws = DimensionManager.getWorld(dimId);
        if(ws != null) {
            MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(ws));
            ws.flush();
            DimensionManager.setWorld(dimId, null);
        }
    }
}
