package makeo.gadomancy.common.utils;

import makeo.gadomancy.common.registration.RegisteredIntegrations;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.blocks.BlockMagicalLog;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileJarNode;
import thaumcraft.common.tiles.TileOwned;
import thaumcraft.common.tiles.TileWarded;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 24.10.2015 18:09
 */
public class JarMultiblockHandler {

    public static void handleWandInteract(World world, int x, int y, int z, EntityPlayer entityPlayer, ItemStack i) {
        Block target = world.getBlock(x, y, z);
        if (target.equals(Blocks.glass)) {
            if (ResearchManager.isResearchComplete(entityPlayer.getCommandSenderName(), "NODEJAR")) {
                tryTCJarNodeCreation(i, entityPlayer, world, x, y, z);
            }
        } else if (target.equals(ConfigBlocks.blockWarded)) {
            if (RegisteredIntegrations.automagy.isPresent() &&
                    ResearchManager.isResearchComplete(entityPlayer.getCommandSenderName(), "ADVNODEJAR")) {
                tryAutomagyJarNodeCreation(i, entityPlayer, world, x, y, z);
            }
        }
    }

    private static void tryTCJarNodeCreation(ItemStack wandStack, EntityPlayer player, World world, int x, int y, int z) {
        JarMultiblockHandler.JarPieceEvaluationRunnable slabRunnable = new JarMultiblockHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                Block block = world.getBlock(absX, absY, absZ);
                int md = world.getBlockMetadata(absX, absY, absZ);
                return containsMatch(false, OreDictionary.getOres("slabWood"), new ItemStack(block, 1, md));
            }
        };
        JarMultiblockHandler.JarPieceEvaluationRunnable glassRunnable = new JarMultiblockHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                return world.getBlock(absX, absY, absZ) == Blocks.glass;
            }
        };
        JarMultiblockHandler.JarPieceEvaluationRunnable nodeRunnable = new JarMultiblockHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                TileEntity tile = world.getTileEntity(absX, absY, absZ);
                if ((tile == null) || (!(tile instanceof INode)) || ((tile instanceof TileJarNode))) {
                    return false;
                }
                return true;
            }
        };
        int[] result = JarMultiblockHandler.evaluateIfValidJarIsPresent(world, x, y, z, player, slabRunnable, glassRunnable, nodeRunnable);

        if (result == null){
            return;
        }

        if(!ThaumcraftApiHelper.consumeVisFromWandCrafting(wandStack, player, new AspectList().add(Aspect.FIRE, 70).add(Aspect.EARTH, 70).add(Aspect.ORDER, 70).add(Aspect.AIR, 70).add(Aspect.ENTROPY, 70).add(Aspect.WATER, 70), true))
            return;

        if(world.isRemote) return;
        JarMultiblockHandler.replaceTCJar(world, result[0], result[1], result[2]);
    }

    private static void tryAutomagyJarNodeCreation(ItemStack wandStack, EntityPlayer player, World world, int x, int y, int z) {
        JarMultiblockHandler.JarPieceEvaluationRunnable silverWoodRunnable = new JarMultiblockHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                TileEntity te = world.getTileEntity(absX, absY, absZ);
                if ((te instanceof TileWarded)) {
                    TileWarded warded = (TileWarded) te;
                    if ((warded.block == ConfigBlocks.blockMagicalLog) && (BlockMagicalLog.limitToValidMetadata(warded.blockMd) == 1)) {
                        return player.getCommandSenderName().hashCode() == warded.owner;
                    }
                }
                return false;
            }
        };
        JarMultiblockHandler.JarPieceEvaluationRunnable glassRunnable = new JarMultiblockHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                if ((world.getBlock(absX, absY, absZ) == ConfigBlocks.blockCosmeticOpaque) && (world.getBlockMetadata(absX, absY, absZ) == 2)) {
                    TileEntity te = world.getTileEntity(absX, absY, absZ);
                    if ((te instanceof TileOwned)) {
                        return player.getCommandSenderName().equals(((TileOwned) te).owner);
                    }
                }
                return false;
            }
        };
        JarMultiblockHandler.JarPieceEvaluationRunnable nodeRunnable = new JarMultiblockHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                TileEntity tile = world.getTileEntity(absX, absY, absZ);
                if ((tile == null) || (!(tile instanceof INode)) || ((tile instanceof TileJarNode))) {
                    return false;
                }
                return true;
            }
        };
        int[] result = JarMultiblockHandler.evaluateIfValidJarIsPresent(world, x, y, z, player, silverWoodRunnable, glassRunnable, nodeRunnable);

        if (result == null){
            return;
        }

        if(!RegisteredIntegrations.automagy.handleNodeJarVisCost(wandStack, player)) return;

        if(world.isRemote) return;
        JarMultiblockHandler.replaceAutomagyJar(world, result[0], result[1], result[2]);
    }

    private static boolean containsMatch(boolean strict, List<ItemStack> inputs, ItemStack... targets) {
        for (ItemStack input : inputs) {
            for (ItemStack target : targets) {
                if (OreDictionary.itemMatches(input, target, strict)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] evaluateIfValidJarIsPresent(World world, int x, int y, int z, EntityPlayer player, JarPieceEvaluationRunnable topRunnable, JarPieceEvaluationRunnable glassRunnable, JarPieceEvaluationRunnable centerRunnable) {
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

    private static boolean isValidJarAt(World world, int x, int y, int z, EntityPlayer player, JarPieceEvaluationRunnable topRunnable, JarPieceEvaluationRunnable glassRunnable, JarPieceEvaluationRunnable centerRunnable) {
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

    private static void replaceTCJar(World world, int x, int y, int z) {
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

    private static void replaceAutomagyJar(World world, int x, int y, int z) {
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
