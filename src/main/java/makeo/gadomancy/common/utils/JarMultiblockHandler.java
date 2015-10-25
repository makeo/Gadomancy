package makeo.gadomancy.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileJarNode;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 24.10.2015 18:09
 */
public class JarMultiblockHandler {

    public static int[] evaluateIfValidJarIsPresent(World world, int x, int y, int z, EntityPlayer player, JarPieceEvaluationRunnable topRunnable, JarPieceEvaluationRunnable glassRunnable, JarPieceEvaluationRunnable centerRunnable) {
        for (int xx = x - 2; xx <= x; xx++) {
            for (int yy = y - 3; yy <= y; yy++) {
                for (int zz = z - 2; zz <= z; zz++) {
                    if (isValidJarAt(world, xx, yy, zz, player, topRunnable, glassRunnable, centerRunnable)) {
                        return new int[] {xx, yy, zz};
                    }
                }
            }
        }
        return null;
    }

    public static boolean isValidJarAt(World world, int x, int y, int z, EntityPlayer player, JarPieceEvaluationRunnable topRunnable, JarPieceEvaluationRunnable glassRunnable, JarPieceEvaluationRunnable centerRunnable) {
        int[][][] blueprint = {{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    int index = blueprint[yy][xx][zz];
                    if (index == 1) {
                        if (!topRunnable.isValidPieceAt(world, x + xx, y - yy + 2, z + zz, player)) return false;
                    }
                    if (index == 2) {
                        if (!glassRunnable.isValidPieceAt(world, x + xx, y - yy + 2, z + zz, player)) return false;
                    }
                    if (index == 3) {
                        if (!centerRunnable.isValidPieceAt(world, x + xx, y - yy + 2, z + zz, player)) return false;
                    }
                }
            }
        }
        return true;
    }

    //TODO spawn growingNodeJar and handle renders and such nasty stuff...

    public static void replaceTCJar(World world, int x, int y, int z) {
        int[][][] blueprint = {{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    if (blueprint[yy][xx][zz] == 3) {
                        TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                        INode node = (INode) tile;
                        AspectList na = node.getAspects().copy();
                        int nt = node.getNodeType().ordinal();
                        int nm = -1;
                        if (node.getNodeModifier() != null) {
                            nm = node.getNodeModifier().ordinal();
                        }
                        if (world.rand.nextFloat() < 0.75F) {
                            if (node.getNodeModifier() == null) {
                                nm = NodeModifier.PALE.ordinal();
                            } else if (node.getNodeModifier() == NodeModifier.BRIGHT) {
                                nm = -1;
                            } else if (node.getNodeModifier() == NodeModifier.PALE) {
                                nm = NodeModifier.FADING.ordinal();
                            }
                        }
                        String nid = node.getId();
                        node.setAspects(new AspectList());
                        world.removeTileEntity(x + xx, y - yy + 2, z + zz);
                        world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 2, 3);
                        tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                        TileJarNode jar = (TileJarNode) tile;
                        jar.setAspects(na);
                        if (nm >= 0) {
                            jar.setNodeModifier(NodeModifier.values()[nm]);
                        }
                        jar.setNodeType(NodeType.values()[nt]);
                        jar.setId(nid);
                        world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 9, 0);
                    } else {
                        world.setBlockToAir(x + xx, y - yy + 2, z + zz);
                    }
                }
            }
        }
        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "thaumcraft:wand", 1.0F, 1.0F);
    }

    public static void replaceAutomagyJar(World world, int x, int y, int z) {
        int[][][] blueprint = {{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    int index = blueprint[yy][xx][zz];
                    if (index == 3) {
                        TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                        INode node = (INode) tile;
                        AspectList na = node.getAspects().copy();
                        int nt = node.getNodeType().ordinal();
                        int nm = -1;
                        if (node.getNodeModifier() != null) {
                            nm = node.getNodeModifier().ordinal();
                        }
                        String nid = node.getId();
                        node.setAspects(new AspectList());
                        world.removeTileEntity(x + xx, y - yy + 2, z + zz);
                        world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 2, 3);
                        tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                        TileJarNode jar = (TileJarNode) tile;
                        jar.setAspects(na);
                        if (nm >= 0) {
                            jar.setNodeModifier(NodeModifier.values()[nm]);
                        }
                        jar.setNodeType(NodeType.values()[nt]);
                        jar.setId(nid);
                        world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 9, 0);
                    } else {
                        world.setBlockToAir(x + xx, y - yy + 2, z + zz);
                    }
                }
            }
        }
    }

    public abstract static class JarPieceEvaluationRunnable {

        public abstract boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player);

    }

}
