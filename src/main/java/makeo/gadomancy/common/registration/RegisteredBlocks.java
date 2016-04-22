package makeo.gadomancy.common.registration;

import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.BlockArcaneDropper;
import makeo.gadomancy.common.blocks.BlockAuraPylon;
import makeo.gadomancy.common.blocks.BlockInfusionClaw;
import makeo.gadomancy.common.blocks.BlockKnowledgeBook;
import makeo.gadomancy.common.blocks.BlockRemoteJar;
import makeo.gadomancy.common.blocks.BlockStickyJar;
import makeo.gadomancy.common.blocks.BlockStoneMachine;
import makeo.gadomancy.common.blocks.tiles.TileArcaneDropper;
import makeo.gadomancy.common.blocks.tiles.TileArcanePackager;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.blocks.tiles.TileBlockProtector;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.blocks.tiles.TileKnowledgeBook;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import makeo.gadomancy.common.items.ItemBlockAuraPylon;
import makeo.gadomancy.common.items.ItemBlockKnowledgeBook;
import makeo.gadomancy.common.items.ItemBlockRemoteJar;
import makeo.gadomancy.common.items.ItemBlockStoneMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.devices.BlockJar;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;

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

    //public static int rendererTransparentBlock; TODO NO!
    //public static int rendererExtendedNodeJarBlock;
    //public static int rendererBlockStoneMachine;

    public static BlockStickyJar blockStickyJar;
    public static BlockArcaneDropper blockArcaneDropper;
    public static BlockInfusionClaw blockInfusionClaw;
    public static BlockRemoteJar blockRemoteJar;
    //public static BlockAiry blockNode = (BlockAiry) ConfigBlocks.blockAiry;
    //public static BlockExtendedNodeJar blockExtendedNodeJar;
    //public static BlockNodeManipulator blockNodeManipulator;
    public static BlockStoneMachine blockStoneMachine;
    //public static BlockAdditionalEldritchPortal blockAdditionalEldrichPortal;
    public static BlockAuraPylon blockAuraPylon;
    public static BlockKnowledgeBook blockKnowledgeBook;

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
        //blockNode = ModConfig.enableAdditionalNodeTypes ? (BlockAiry) new BlockNode().setBlockName("blockAiry") : (BlockAiry) ConfigBlocks.blockAiry;
        //blockExtendedNodeJar = registerBlock(new BlockExtendedNodeJar());
        //blockNodeManipulator = registerBlock(new BlockNodeManipulator(), ItemNodeManipulator.class);
        blockStoneMachine = registerBlock(new BlockStoneMachine(), ItemBlockStoneMachine.class);
        //blockAdditionalEldrichPortal = registerBlock(new BlockAdditionalEldritchPortal(), ItemBlockAdditionalEldritchPortal.class);
        blockAuraPylon = registerBlock(new BlockAuraPylon(), ItemBlockAuraPylon.class);
        blockKnowledgeBook = registerBlock(new BlockKnowledgeBook(), ItemBlockKnowledgeBook.class);
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        GameRegistry.registerBlock(block, name);
        return block;
    }

    private static <T extends Block> T registerBlock(String name, T block, Class<? extends ItemBlock> itemClass) {
        GameRegistry.registerBlock(block, itemClass, name);
        return block;
    }

    private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass) {
        registerBlock(Gadomancy.MODID + "_" + block.getClass().getSimpleName(), block, itemClass);
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
        //registerTile(TileExtendedNode.class);
        //registerTile(TileExtendedNodeJar.class);
        //registerTile(TileNodeManipulator.class);
        //registerTile(TileManipulatorPillar.class);
        //registerTile(TileManipulationFocus.class);
        //registerTile(TileAdditionalEldritchPortal.class);
        registerTile(TileBlockProtector.class);
        registerTile(TileAuraPylon.class);
        registerTile(TileAuraPylonTop.class);
        registerTile(TileArcanePackager.class);
        registerTile(TileKnowledgeBook.class);
    }

    private static void registerTile(Class<? extends TileEntity> tile, String name) {
        GameRegistry.registerTileEntity(tile, name);
    }

    private static void registerTile(Class<? extends TileEntity> tile) {
        registerTile(tile, tile.getSimpleName());
    }


    //Sticky Jars
    private static void registerDefaultStickyJars() {
        registerStickyJar(BlocksTC.jar.getDefaultState().withProperty(BlockJar.TYPE, BlockJar.JarType.NORMAL), true, true);
        registerStickyJar(BlocksTC.jar.getDefaultState().withProperty(BlockJar.TYPE, BlockJar.JarType.VOID), true, true);

        registerStickyJar(blockRemoteJar.getDefaultState(), false, false);
    }

    private static List<StickyJarInfo> stickyJars = new ArrayList<StickyJarInfo>();

    public static void registerStickyJar(IBlockState state, boolean isLabelable, boolean isPhialable) {
        stickyJars.add(0, new StickyJarInfo(state, isLabelable, isPhialable));
    }

    public static StickyJarInfo getStickyJarInfo(IBlockState state) {
        Block b = state.getBlock();
        int meta = b.getMetaFromState(state);
        for(StickyJarInfo info : stickyJars) {
            Block infoBlock = info.getBlockState().getBlock();
            int infoMeta = infoBlock.getMetaFromState(info.getBlockState());
            if(infoBlock.equals(b) && infoMeta == meta) {
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
                Block block = state.getBlock();
                int meta = block.getMetaFromState(state);
                return (block == Blocks.bookshelf && meta == 0)
                        || (block == Blocks.cauldron && meta == 0);
            }
        });

        registerClawClickBehavior(new ClickBehavior(true) {
            private TileInfusionMatrix matrix;

            @Override
            public boolean isValidForBlock() {
                Block block = state.getBlock();
                if(block == BlocksTC.infusionMatrix) {
                    matrix = (TileInfusionMatrix) world.getTileEntity(pos);
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

    public static ClickBehavior getClawClickBehavior(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        //Block block = world.getBlock(x, y, z);
        //int metadata = world.getBlockMetadata(x, y, z);

        for(ClickBehavior behavior : clawBehaviors) {
            behavior.init(world, state, pos);
            //behavior.init(world, block, x, y, z, metadata);
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
