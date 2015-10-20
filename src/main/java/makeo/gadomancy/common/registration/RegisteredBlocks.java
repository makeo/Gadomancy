package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.common.blocks.BlockArcaneDropper;
import makeo.gadomancy.common.blocks.BlockInfusionClaw;
import makeo.gadomancy.common.blocks.BlockRemoteJar;
import makeo.gadomancy.common.blocks.BlockStickyJar;
import makeo.gadomancy.common.blocks.tiles.TileArcaneDropper;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.blocks.tiles.TileRemoteJar;
import makeo.gadomancy.common.blocks.tiles.TileStickyJar;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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

    public static BlockStickyJar blockStickyJar;
    public static BlockArcaneDropper blockArcaneDropper;
    public static BlockInfusionClaw blockInfusionClaw;
    public static BlockRemoteJar blockRemoteJar;

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
        blockRemoteJar = registerBlock(new BlockRemoteJar());
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
