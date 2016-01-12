package makeo.gadomancy.common.utils;

import cpw.mods.fml.common.network.NetworkRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNodeJar;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.entities.EntityAuraCore;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.node.ExtendedNodeType;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredIntegrations;
import makeo.gadomancy.common.registration.RegisteredRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
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

import java.util.Iterator;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 24.10.2015 18:09
 */
public class WandHandler {

    //All wand interacts are redirected to this.
    public static void handleWandInteract(World world, int x, int y, int z, EntityPlayer entityPlayer, ItemStack i) {
        Block target = world.getBlock(x, y, z);
        if(target == null) return;

        if(ModConfig.enableAdditionalNodeTypes) {
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

        if(target.equals(ConfigBlocks.blockCrystal)) {
            if (ResearchManager.isResearchComplete(entityPlayer.getCommandSenderName(), Gadomancy.MODID.toUpperCase() + ".AURA_CORE"))
                tryAuraCoreCreation(i, entityPlayer, world, x, y, z);
        }
    }

    private static void tryAuraCoreCreation(ItemStack i, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        if(world.isRemote) return;

        List items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
                .expand(EntityAuraCore.CLUSTER_RANGE, EntityAuraCore.CLUSTER_RANGE, EntityAuraCore.CLUSTER_RANGE));
        Iterator it = items.iterator();
        EntityItem itemAuraCore = null;
        while(itemAuraCore == null && it.hasNext()) {
            Object item = it.next();
            if(item != null && item instanceof EntityItem && !((EntityItem) item).isDead) {
                EntityItem entityItem = (EntityItem) item;
                if(entityItem.getEntityItem() != null && entityItem.getEntityItem().getItem() != null
                        && entityItem.getEntityItem().getItem() instanceof ItemAuraCore && !(item instanceof EntityAuraCore)) {
                    if(((ItemAuraCore) entityItem.getEntityItem().getItem()).isBlank(entityItem.getEntityItem())) itemAuraCore = entityItem;
                }
            }
        }

        if(itemAuraCore == null) return;

        int meta = world.getBlockMetadata(x, y, z);
        if(meta <= 6) {
            Aspect[] aspects;
            switch (meta) {
                case 0:
                    aspects = new Aspect[]{Aspect.AIR};
                    break;
                case 1:
                    aspects = new Aspect[]{Aspect.FIRE};
                    break;
                case 2:
                    aspects = new Aspect[]{Aspect.WATER};
                    break;
                case 3:
                    aspects = new Aspect[]{Aspect.EARTH};
                    break;
                case 4:
                    aspects = new Aspect[]{Aspect.ORDER};
                    break;
                case 5:
                    aspects = new Aspect[]{Aspect.ENTROPY};
                    break;
                case 6:
                    aspects = new Aspect[]{Aspect.AIR, Aspect.FIRE, Aspect.EARTH, Aspect.WATER, Aspect.ORDER, Aspect.ENTROPY};
                    break;
                default:
                    return;
            }

            if(!ThaumcraftApiHelper.consumeVisFromWandCrafting(i, entityPlayer, (AspectList)RegisteredRecipes.auraCoreRecipes[meta].get(0), true))
                return;


            PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_BURST, x, y, z);
            PacketHandler.INSTANCE.sendToAllAround(packet, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32.0D));
            world.createExplosion(null, x + 0.5D, y + 0.5D, z + 0.5D, 1.5F, false);
            world.setBlockToAir(x, y, z);

            EntityAuraCore ea =
                    new EntityAuraCore(itemAuraCore.worldObj, itemAuraCore.posX, itemAuraCore.posY, itemAuraCore.posZ,
                            itemAuraCore.getEntityItem(), new ChunkCoordinates(x, y, z), aspects);
            ea.age = itemAuraCore.age;
            ea.hoverStart = itemAuraCore.hoverStart;
            ea.motionX = itemAuraCore.motionX;
            ea.motionY = itemAuraCore.motionY;
            ea.motionZ = itemAuraCore.motionZ;
            itemAuraCore.worldObj.spawnEntityInWorld(ea);
            itemAuraCore.setDead();
        }
    }

    private static void tryTCJarNodeCreation(ItemStack wandStack, EntityPlayer player, World world, int x, int y, int z) {
        WandHandler.JarPieceEvaluationRunnable slabRunnable = new WandHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                Block block = world.getBlock(absX, absY, absZ);
                int md = world.getBlockMetadata(absX, absY, absZ);
                return containsMatch(false, OreDictionary.getOres("slabWood"), new ItemStack(block, 1, md));
            }
        };
        WandHandler.JarPieceEvaluationRunnable glassRunnable = new WandHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                return world.getBlock(absX, absY, absZ) == Blocks.glass;
            }
        };
        WandHandler.JarPieceEvaluationRunnable nodeRunnable = new WandHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                TileEntity tile = world.getTileEntity(absX, absY, absZ);
                if ((tile == null) || (!(tile instanceof INode)) || ((tile instanceof TileJarNode))) {
                    return false;
                }
                return true;
            }
        };
        int[] result = WandHandler.evaluateIfValidJarIsPresent(world, x, y, z, player, slabRunnable, glassRunnable, nodeRunnable);

        if (result == null){
            return;
        }

        if(!ThaumcraftApiHelper.consumeVisFromWandCrafting(wandStack, player, new AspectList().add(Aspect.FIRE, 70).add(Aspect.EARTH, 70).add(Aspect.ORDER, 70).add(Aspect.AIR, 70).add(Aspect.ENTROPY, 70).add(Aspect.WATER, 70), true))
            return;

        if(world.isRemote) return;
        replaceJar(world, result[0], result[1], result[2], true);
    }

    private static void tryAutomagyJarNodeCreation(ItemStack wandStack, EntityPlayer player, World world, int x, int y, int z) {
        WandHandler.JarPieceEvaluationRunnable silverWoodRunnable = new WandHandler.JarPieceEvaluationRunnable() {
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
        WandHandler.JarPieceEvaluationRunnable glassRunnable = new WandHandler.JarPieceEvaluationRunnable() {
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
        WandHandler.JarPieceEvaluationRunnable nodeRunnable = new WandHandler.JarPieceEvaluationRunnable() {
            @Override
            public boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player) {
                TileEntity tile = world.getTileEntity(absX, absY, absZ);
                if ((tile == null) || (!(tile instanceof INode)) || ((tile instanceof TileJarNode))) {
                    return false;
                }
                return true;
            }
        };
        int[] result = WandHandler.evaluateIfValidJarIsPresent(world, x, y, z, player, silverWoodRunnable, glassRunnable, nodeRunnable);

        if (result == null){
            return;
        }

        if(!RegisteredIntegrations.automagy.handleNodeJarVisCost(wandStack, player)) return;

        if(world.isRemote) return;
        if(!RegisteredIntegrations.automagy.isPresent()) return;
        replaceJar(world, result[0], result[1], result[2], false);
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

    private static void replaceJar(World world, int x, int y, int z, boolean isThaumcraft) {
        int[][][] blueprint = {{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    if (blueprint[yy][xx][zz] == 3) {
                        handleJarForming(world, x, y, z, xx, yy, zz, isThaumcraft);
                    } else {
                        world.setBlockToAir(x + xx, y - yy + 2, z + zz);
                    }
                }
            }
        }
        if(isThaumcraft) world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "thaumcraft:wand", 1.0F, 1.0F);
    }

    private static void handleJarForming(World world, int x, int y, int z, int xx, int yy, int zz, boolean degrade) {
        TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
        INode node = (INode) tile;
        AspectList na = node.getAspects().copy();
        int nt = node.getNodeType().ordinal();
        int nm = -1;
        int exNt = -1;
        NBTTagCompound behaviorSnapshot = null;
        if (node.getNodeModifier() != null) {
            nm = node.getNodeModifier().ordinal();
        }
        if(tile instanceof TileExtendedNode && ((TileExtendedNode) tile).getExtendedNodeType() != null) {
            exNt = ((TileExtendedNode) tile).getExtendedNodeType().ordinal();
            behaviorSnapshot = ((TileExtendedNode) tile).getBehaviorSnapshot();
        }
        if(degrade) {
            if (world.rand.nextFloat() < 0.75F) {
                if (node.getNodeModifier() == null) {
                    nm = NodeModifier.PALE.ordinal();
                } else if (node.getNodeModifier() == NodeModifier.BRIGHT) {
                    nm = -1;
                } else if (node.getNodeModifier() == NodeModifier.PALE) {
                    nm = NodeModifier.FADING.ordinal();
                }
            }
        }
        String nid = node.getId();
        node.setAspects(new AspectList());
        world.removeTileEntity(x + xx, y - yy + 2, z + zz);
        if(exNt != -1) {
            world.setBlock(x + xx, y - yy + 2, z + zz, RegisteredBlocks.blockExtendedNodeJar, 0, 3);
            tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
            TileExtendedNodeJar exJar = (TileExtendedNodeJar) tile;
            exJar.setAspects(na);
            if (nm >= 0) {
                exJar.setNodeModifier(NodeModifier.values()[nm]);
            }
            exJar.setNodeType(NodeType.values()[nt]);
            exJar.setExtendedNodeType(ExtendedNodeType.values()[exNt]);
            if(behaviorSnapshot != null) {
                exJar.setBehaviorSnapshot(behaviorSnapshot);
            }
            exJar.setId(nid);
            world.addBlockEvent(x + xx, y - yy + 2, z + zz, RegisteredBlocks.blockExtendedNodeJar, 9, 0);
        } else {
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
        }
    }

    public abstract static class JarPieceEvaluationRunnable {

        public abstract boolean isValidPieceAt(World world, int absX, int absY, int absZ, EntityPlayer player);

    }

}
