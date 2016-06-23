package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.common.blocks.BlockAdditionalEldritchPortal;
import makeo.gadomancy.common.blocks.BlockArcaneDropper;
import makeo.gadomancy.common.blocks.BlockAuraPylon;
import makeo.gadomancy.common.blocks.BlockEssentiaCompressor;
import makeo.gadomancy.common.blocks.BlockExtendedNodeJar;
import makeo.gadomancy.common.blocks.BlockInfusionClaw;
import makeo.gadomancy.common.blocks.BlockKnowledgeBook;
import makeo.gadomancy.common.blocks.BlockNode;
import makeo.gadomancy.common.blocks.BlockNodeManipulator;
import makeo.gadomancy.common.blocks.BlockRemoteJar;
import makeo.gadomancy.common.blocks.BlockStickyJar;
import makeo.gadomancy.common.blocks.BlockStoneMachine;
import makeo.gadomancy.common.blocks.tiles.*;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.items.ItemBlockAdditionalEldritchPortal;
import makeo.gadomancy.common.items.ItemBlockAuraPylon;
import makeo.gadomancy.common.items.ItemBlockEssentiaCompressor;
import makeo.gadomancy.common.items.ItemBlockKnowledgeBook;
import makeo.gadomancy.common.items.ItemBlockRemoteJar;
import makeo.gadomancy.common.items.ItemBlockStoneMachine;
import makeo.gadomancy.common.items.ItemNodeManipulator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileInfusionMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 18:38
 */
public class RegisteredBlocks {
    private RegisteredBlocks() {}

    public static int rendererTransparentBlock;
    public static int rendererExtendedNodeJarBlock;
    public static int rendererBlockStoneMachine;

    public static BlockStickyJar blockStickyJar;
    public static BlockArcaneDropper blockArcaneDropper;
    public static BlockInfusionClaw blockInfusionClaw;
    public static BlockRemoteJar blockRemoteJar;
    public static BlockAiry blockNode = (BlockAiry) ConfigBlocks.blockAiry;
    public static BlockExtendedNodeJar blockExtendedNodeJar;
    public static BlockNodeManipulator blockNodeManipulator;
    public static BlockStoneMachine blockStoneMachine;
    public static BlockAdditionalEldritchPortal blockAdditionalEldrichPortal;
    public static BlockAuraPylon blockAuraPylon;
    public static BlockKnowledgeBook blockKnowledgeBook;
    public static BlockEssentiaCompressor blockEssentiaCompressor;

    public static void init() {
        registerBlocks();

        registerTileEntities();

        registerDefaultStickyJars();
        registerDefaultClawBehaviors();
    }

    //Blocks
    private static void registerBlocks() {
        blockStickyJar = registerBlock(new BlockStickyJar());
        blockArcaneDropper = registerBlock(new BlockArcaneDropper());
        blockInfusionClaw = registerBlock(new BlockInfusionClaw());
        blockRemoteJar = registerBlock(new BlockRemoteJar(), ItemBlockRemoteJar.class);
        blockNode = ModConfig.enableAdditionalNodeTypes ? (BlockAiry) new BlockNode().setBlockName("blockAiry") : (BlockAiry) ConfigBlocks.blockAiry;
        blockExtendedNodeJar = registerBlock(new BlockExtendedNodeJar());
        blockNodeManipulator = registerBlock(new BlockNodeManipulator(), ItemNodeManipulator.class);
        blockStoneMachine = registerBlock(new BlockStoneMachine(), ItemBlockStoneMachine.class);
        blockAdditionalEldrichPortal = registerBlock(new BlockAdditionalEldritchPortal(), ItemBlockAdditionalEldritchPortal.class);
        blockAuraPylon = registerBlock(new BlockAuraPylon(), ItemBlockAuraPylon.class);
        blockKnowledgeBook = registerBlock(new BlockKnowledgeBook(), ItemBlockKnowledgeBook.class);
        blockEssentiaCompressor = registerBlock(new BlockEssentiaCompressor(), ItemBlockEssentiaCompressor.class);
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        block.setBlockName(name);
        GameRegistry.registerBlock(block, name);
        return block;
    }

    private static <T extends Block> T registerBlock(String name, T block, Class<? extends ItemBlock> itemClass) {
        block.setBlockName(name);
        GameRegistry.registerBlock(block, itemClass, name);
        return block;
    }

    private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass) {
        registerBlock(block.getClass().getSimpleName(), block, itemClass);
        return block;
    }

    private static <T extends Block> T registerBlock(T block) {
        registerBlock(block.getClass().getSimpleName(), block);
        return block;
    }

    //Tiles
    private static void registerTileEntities() {
        registerTile(TileStickyJar.class);
        registerTile(TileArcaneDropper.class);
        registerTile(TileInfusionClaw.class);
        registerTile(TileRemoteJar.class);
        registerTile(TileExtendedNode.class);
        registerTile(TileExtendedNodeJar.class);
        registerTile(TileNodeManipulator.class);
        registerTile(TileManipulatorPillar.class);
        registerTile(TileManipulationFocus.class);
        registerTile(TileAdditionalEldritchPortal.class);
        registerTile(TileBlockProtector.class);
        registerTile(TileAuraPylon.class);
        registerTile(TileAuraPylonTop.class);
        registerTile(TileArcanePackager.class);
        registerTile(TileKnowledgeBook.class);
        registerTile(TileEssentiaCompressor.class);
        //registerTile(TileAIShutdown.class);
    }

    private static void registerTile(Class<? extends TileEntity> tile, String name) {
        GameRegistry.registerTileEntity(tile, name);
    }

    private static void registerTile(Class<? extends TileEntity> tile) {
        registerTile(tile, tile.getSimpleName());
    }


    //Sticky Jars
    private static void registerDefaultStickyJars() {
        registerStickyJar(ConfigBlocks.blockJar, 0, true, true);
        registerStickyJar(ConfigBlocks.blockJar, 3, true, true);

        registerStickyJar(RegisteredBlocks.blockRemoteJar, 0, false, false);
    }

    private static List<StickyJarInfo> stickyJars = new ArrayList<StickyJarInfo>();

    public static void registerStickyJar(Block block, int metadata, boolean isLabelable, boolean isPhialable) {
        stickyJars.add(0, new StickyJarInfo(block, metadata, isLabelable, isPhialable));
    }

    public static StickyJarInfo getStickyJarInfo(Block block, int metadata) {
        for(StickyJarInfo info : stickyJars) {
            if(info.getBlock() == block && info.getMetadata() == metadata) {
                return info;
            }
        }
        return null;
    }

    //Infusion Claw
    private static void registerDefaultClawBehaviors() {
        registerClawClickBehavior(new ClickBehavior() {
            @Override
            public boolean isValidForBlock() {
                return (block == Blocks.bookshelf && metadata == 0)
                        || (block == Blocks.cauldron && metadata == 0);
            }
        });

        registerClawClickBehavior(new ClickBehavior(true) {
            private TileInfusionMatrix matrix;

            @Override
            public boolean isValidForBlock() {
                if(block == ConfigBlocks.blockStoneDevice && metadata == 2) {
                    matrix = (TileInfusionMatrix) world.getTileEntity(x, y, z);
                    return true;
                }
                return false;
            }

            @Override
            public void addInstability(int instability) {
                matrix.instability += instability;
            }

            @Override
            public int getComparatorOutput() {
                return matrix.crafting ? 15 : 0;
            }
        });

        /*registerClawClickBehavior(new ClickBehavior(true) {
            @Override
            public boolean isValidForBlock() {
                return block.equals(ConfigBlocks.blockCrystal) && metadata <= 6;
            }

            @Override
            public AspectList getVisCost() {
                return RegisteredRecipes.costsAuraCoreStart;
            }
        });*/
    }

    private static List<ClickBehavior> clawBehaviors = new ArrayList<ClickBehavior>();

    public static void registerClawClickBehavior(ClickBehavior behavior) {
        clawBehaviors.add(behavior);
    }

    public static ClickBehavior getClawClickBehavior(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);

        for(ClickBehavior behavior : clawBehaviors) {
            behavior.init(world, block, x, y, z, metadata);
            if(behavior.isValidForBlock()) {
                return behavior;
            }
        }
        return null;
    }

    public static List<ClickBehavior> getClawClickBehaviors() {
        return clawBehaviors;
    }
}
