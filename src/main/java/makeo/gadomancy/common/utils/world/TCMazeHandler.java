package makeo.gadomancy.common.utils.world;

import makeo.gadomancy.common.blocks.tiles.TileAdditionalEldritchPortal;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.world.fake.FakeWorldTCGeneration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.dim.CellLoc;
import thaumcraft.common.lib.world.dim.GenCommon;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
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
    private static int tickCounter = 0;

    public static final FakeWorldTCGeneration GEN = new FakeWorldTCGeneration();

    private static List<TCMazeSession> flaggedSessions = new ArrayList<TCMazeSession>();

    private static Map<EntityPlayer, TCMazeSession> runningSessions = new HashMap<EntityPlayer, TCMazeSession>();
    private static Map<TCMazeSession, Entity[]> watchedBosses = new HashMap<TCMazeSession, Entity[]>();

    public static void closeAllSessionsAndCleanup() {
        for (EntityPlayer pl : runningSessions.keySet()) {
            TCMazeSession session = runningSessions.get(pl);
            session.closeSession(false);
            watchedBosses.remove(session);
        }
        init();
    }

    public static void tick() {
        WorldServer w = DimensionManager.getWorld(ModConfig.dimOuterId);
        if (w != null) {
            if (!w.levelSaving) w.levelSaving = true;

            WorldServer out = MinecraftServer.getServer().worldServerForDimension(0);
            List playerObjects = w.playerEntities;
            for (int i = 0; i < playerObjects.size(); i++) {
                EntityPlayer player = (EntityPlayer) playerObjects.get(i);
                if (!hasOpenSession(player)) {
                    WorldUtil.tryTeleportBack((EntityPlayerMP) player, 0);
                    ChunkCoordinates cc = out.getSpawnPoint();
                    int y = out.getTopSolidOrLiquidBlock(cc.posX, cc.posZ);
                    player.setPositionAndUpdate(cc.posX + 0.5, y, cc.posZ + 0.5);
                }
            }
            Iterator<Map.Entry<EntityPlayer, TCMazeSession>> it = runningSessions.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<EntityPlayer, TCMazeSession> entry = it.next();
                EntityPlayer player = entry.getKey();
                if (player.worldObj.provider.dimensionId != ModConfig.dimOuterId) { //If the player left our dim, but he should still be in the session, ...
                    //We close the session.
                    entry.getValue().closeSession(false);
                    it.remove();
                }
            }
        }
    }

    public static void handleBossFinish(TCMazeSession session) {
        CellLoc spawnChunk = session.portalCell;
        WorldServer world = MinecraftServer.getServer().worldServerForDimension(ModConfig.dimOuterId);
        int lX = (spawnChunk.x << 4) + 8;
        int lZ = (spawnChunk.z << 4) + 8;
        world.setBlock(lX, 52, lZ, ConfigBlocks.blockEldritch, 3, 3);
        world.setBlock(lX, 53, lZ, RegisteredBlocks.blockAdditionalEldrichPortal);
        GenCommon.genObelisk(world, lX, 54, lZ);
        session.player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + StatCollector.translateToLocal("gadomancy.eldritch.portalSpawned")));
    }

    /*
     *  Coordinates wanted here are the absolute Portal coordinates.
     */
    public static boolean createSessionWaitForTeleport(EntityPlayer player) {
        if (hasOpenSession(player) || !hasFreeSessionSpace()) return false;
        WorldServer w = MinecraftServer.getServer().worldServerForDimension(ModConfig.dimOuterId);
        reserveSessionSpace(player);
        setupSession(player, w);
        return true;
    }

    private static void reserveSessionSpace(EntityPlayer player) {
        runningSessions.put(player, TCMazeSession.placeholder());
    }

    private static void setupSession(EntityPlayer player, WorldServer world) {
        Vec3 currentPos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
        int originDim = player.worldObj.provider.dimensionId;
        Map<CellLoc, Short> locs = calculateCellLocs(world);
        MazeBuilderThread t = new MazeBuilderThread((EntityPlayerMP) player, locs, originDim, currentPos);
        t.start();
    }

    private static Map<CellLoc, Short> calculateCellLocs(WorldServer world) {
        ConcurrentHashMap<CellLoc, Short> oldDat = MazeHandler.labyrinth;
        ConcurrentHashMap<CellLoc, Short> bufferOld = new ConcurrentHashMap<CellLoc, Short>(labyrinthCopy);
        MazeHandler.labyrinth = labyrinthCopy;
        int chX = getHighestPossibleRandWH(); //To ensure we're always +x and +z
        int chZ = getHighestPossibleRandWH();
        int w = randWH(world.rand);
        int h = randWH(world.rand);
        while (MazeHandler.mazesInRange(chX, chZ, w, h)) {
            chX++; //We grow the mazes in +x direction!
        }
        MazeThread mt = new MazeThread(chX, chZ, w, h, world.rand.nextLong());
        mt.run();
        Map<CellLoc, Short> locs = calculateDifferences(bufferOld);
        labyrinthCopy = MazeHandler.labyrinth;
        MazeHandler.labyrinth = oldDat;
        return locs;
    }

    private static Map<CellLoc, Short> calculateDifferences(ConcurrentHashMap<CellLoc, Short> bufferOld) {
        ConcurrentHashMap<CellLoc, Short> newDat = MazeHandler.labyrinth; //Only the new data has data, the old one doesn't have.
        Map<CellLoc, Short> newlyEvaluatedMaze = new HashMap<CellLoc, Short>();
        for (CellLoc loc : newDat.keySet()) {
            if (!bufferOld.containsKey(loc)) {
                newlyEvaluatedMaze.put(loc, newDat.get(loc));
            }
        }
        return newlyEvaluatedMaze;
    }

    private static boolean hasFreeSessionSpace() {
        return ModConfig.maxMazeCount == -1 || runningSessions.size() < ModConfig.maxMazeCount;
    }

    /*
    Change getHighestPossibleRandWH accordingly if modifying this.
     */
    private static int randWH(Random random) {
        return 17 + random.nextInt(2) * 2;
    }

    private static int getHighestPossibleRandWH() {
        //Add something to ensure 100% we never get in minus coordinates!
        return 29; //21 + ensured 8
    }

    public static void init() {
        runningSessions = new HashMap<EntityPlayer, TCMazeSession>();
    }

    public static boolean hasOpenSession(EntityPlayer player) {
        return !player.worldObj.isRemote && runningSessions.get(player) != null;
    }

    public static void closeSession(EntityPlayer player, boolean teleport) {
        if (player.worldObj.isRemote) return;

        if (runningSessions.containsKey(player)) {
            runningSessions.get(player).closeSession(teleport);
            runningSessions.remove(player);
        }
    }

    public static Map<EntityPlayer, TCMazeSession> getSessions() {
        return runningSessions;
    }

    public static void free(Map<CellLoc, Short> locations) {
        if (locations == null) return;
        for (CellLoc loc : locations.keySet()) {
            labyrinthCopy.remove(loc);
        }
    }

    public static void scheduleTick() {
        tickCounter++;

        Iterator<TCMazeSession> it = flaggedSessions.iterator();
        while(it.hasNext()) {
            TCMazeSession s = it.next();
            it.remove();
            s.startSession();
            runningSessions.put(s.player, s);
        }

        if((tickCounter & 15) == 0) {
            Iterator<Map.Entry<TCMazeSession, Entity[]>> iterator = watchedBosses.entrySet().iterator();
            labelWhile: while (iterator.hasNext()) {
                Map.Entry<TCMazeSession, Entity[]> entry = iterator.next();
                TCMazeSession session = entry.getKey();
                Entity[] bosses = entry.getValue();
                for(Entity entity : bosses) {
                    if(entity != null && !entity.isDead) {
                        continue labelWhile;
                    }
                }
                TCMazeHandler.handleBossFinish(session);
                iterator.remove();
            }
        }

        tick();
    }

    public static void flagSessionForStart(TCMazeSession session) {
        flaggedSessions.add(session);
    }

    public static void putBosses(TCMazeSession session, Entity[] bosses) {
        watchedBosses.put(session, bosses);
    }

    public static class MazeBuilderThread extends Thread {

        private final EntityPlayerMP player;
        private final Map<CellLoc, Short> chunksAffected;
        private final int originDimId;
        private final Vec3 originLocation;

        public MazeBuilderThread(EntityPlayerMP player, Map<CellLoc, Short> chunksAffected, int originDimId, Vec3 originLocation) {
            this.player = player;
            this.chunksAffected = chunksAffected;
            this.originDimId = originDimId;
            this.originLocation = originLocation;
            setName("GadomancyEldritchGen (SERVER/ThreadID=" + getId() + ")");
        }

        @Override
        public void run() {
            ConcurrentHashMap<CellLoc, Short> old = MazeHandler.labyrinth;
            MazeHandler.labyrinth = labyrinthCopy;
            for (CellLoc l : chunksAffected.keySet()) {
                MazeHandler.generateEldritch(GEN, GEN.rand, l.x, l.z);
            }
            MazeHandler.labyrinth = old;

            if(ModConfig.doLightCalculations) {
                for(CellLoc l : chunksAffected.keySet()) {
                    for(int cX = 0; cX < 16; cX++) {
                        for(int cY = 51; cY < 61; cY++) {
                            for(int cZ = 0; cZ < 16; cZ++) {
                                TCMazeHandler.GEN.updateLightByType(EnumSkyBlock.Block, (l.x << 4)+cX, cY, (l.z << 4)+cZ);
                            }
                        }
                    }
                }
            }

            finishBuild();
        }

        private void finishBuild() {
            TileAdditionalEldritchPortal.informSessionStart(player);
            TCMazeSession session = new TCMazeSession(player, chunksAffected, originDimId, originLocation);
            flagSessionForStart(session);
        }

    }
}
