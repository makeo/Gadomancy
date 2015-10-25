package makeo.gadomancy.common.utils;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketAnimationAbsorb;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 25.10.2015 10:17
 */
public class ExplosionHelper {

    public static void taintplosion(World world, int x, int y, int z, boolean taintBiome, int chanceToTaint) {
        if(chanceToTaint < 1) chanceToTaint = 1;
        world.createExplosion(null, x + 0.5D, y + 0.5D, z + 0.5D, 3.0F, false);
        for (int a = 0; a < 50; a++) {
            int xx = x + world.rand.nextInt(8) - world.rand.nextInt(8);
            int yy = y + world.rand.nextInt(8) - world.rand.nextInt(8);
            int zz = z + world.rand.nextInt(8) - world.rand.nextInt(8);
            if (world.isAirBlock(xx, yy, zz)) {
                if (yy < y) {
                    world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGoo, 8, 3);
                } else {
                    world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGas, 8, 3);
                }
            }
            if(taintBiome && world.rand.nextInt(chanceToTaint) == 0) {
                Utils.setBiomeAt(world, xx, zz, ThaumcraftWorldGenerator.biomeTaint);
            }
        }
    }

    public static class VortexExplosion {

        //Phase 0:   Lightnings to blocks and entities around
        //Phase 1:   Creating FallingBlocks and drawing them in
        //Phase END: Shooting all FallingBlocks into random directories around the node + taint explosion around the node
        private int tick;
        private int phase;
        private World world;
        private int x, y, z;
        private List<Vec3> pastTickBlocks = null;

        public VortexExplosion(TileNode exNode) {
            this.world = exNode.getWorldObj();
            this.x = exNode.xCoord;
            this.y = exNode.yCoord;
            this.z = exNode.zCoord;
            this.phase = 0;
            this.tick = 0;
        }

        public void update() {
            switch (phase) {
                case 0: {
                    tick++;
                    if(tick > 200) {
                        tick = 0;
                        phase = 1;
                    }

                    sendRadomVortexLightningPacket(world, x, y, z);
                    break;
                }
                case 1: {
                    tick++;
                    int range = tick > 100 ? tick > 200 ? 8 : 6 : 4;
                    if(pastTickBlocks != null) {
                        for(Vec3 v : pastTickBlocks) {
                            world.setBlockToAir((int) v.xCoord, (int) v.yCoord, (int) v.zCoord);
                        }
                    }
                    pastTickBlocks = new ArrayList<Vec3>();
                    int cnt = 10;
                    do {
                        int xx = x + world.rand.nextInt(range) - world.rand.nextInt(range);
                        int yy = y + world.rand.nextInt(range) - world.rand.nextInt(range);
                        int zz = z + world.rand.nextInt(range) - world.rand.nextInt(range);
                        Block b = world.getBlock(xx, yy, zz);
                        float hardness = b.getBlockHardness(world, xx, yy, zz);
                        if(b != Blocks.air && hardness > 0 && hardness <= 50 && b != RegisteredBlocks.blockNode) {
                            PacketAnimationAbsorb absorb = new PacketAnimationAbsorb(x + 0.5D, y + 0.5D, z + 0.5D, xx, yy, zz);
                            PacketHandler.INSTANCE.sendToAllAround(absorb, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32D));
                            pastTickBlocks.add(Vec3.createVectorHelper(xx, yy, zz));
                        }
                        cnt--;
                    } while (cnt > 0);

                    if(tick > 300) {
                        phase = 2;
                        taintplosion(world, x, y, z, true, 5);
                    }
                    break;
                }
            }
        }

        private void sendRadomVortexLightningPacket(World world, int x, int y, int z) {
            PacketStartAnimation animationPacket = new PacketStartAnimation(PacketStartAnimation.ID_EX_VORTEX, x, y, z);
            NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32.0D);
            PacketHandler.INSTANCE.sendToAllAround(animationPacket, point);
        }

        public static void vortexLightning(TileExtendedNode te) {
            World world = te.getWorldObj();
            int x = te.xCoord;
            int y = te.yCoord;
            int z = te.zCoord;
            float xx = x + world.rand.nextInt(3) - world.rand.nextInt(3);
            float yy = y + world.rand.nextInt(3) - world.rand.nextInt(3);
            float zz = z + world.rand.nextInt(3) - world.rand.nextInt(3);
            xx += world.rand.nextFloat() * (world.rand.nextBoolean() ? 1 : -1);
            yy += world.rand.nextFloat() * (world.rand.nextBoolean() ? 1 : -1);
            zz += world.rand.nextFloat() * (world.rand.nextBoolean() ? 1 : -1);
            Thaumcraft.proxy.nodeBolt(world, x + 0.5F, y + 0.5F, z + 0.5F, xx, yy, zz);
        }

        public boolean isFinished() {
            return phase > 1;
        }

    }

}
