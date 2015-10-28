package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.utils.MultiblockHelper;
import net.minecraft.init.Blocks;
import thaumcraft.common.config.ConfigBlocks;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 27.10.2015 14:05
 */
public class RegisteredMultiblocks {
    public static final MultiblockHelper.MultiblockPattern incompleteNodeManipulatorMultiblock = new MultiblockHelper.MultiblockPattern(RegisteredBlocks.blockNodeManipulator, 5)
            .addBlock(0, 2, 0, RegisteredBlocks.blockNode, 0).addBlock(0, 1, 0, RegisteredBlocks.blockStoneMachine, 0)
            .addBlock(1, 0, 1, ConfigBlocks.blockCosmeticSolid, 15).addBlock(-1, 0, 1, ConfigBlocks.blockCosmeticSolid, 15).addBlock(-1, 0, -1, ConfigBlocks.blockCosmeticSolid, 15).addBlock(1, 0, -1, ConfigBlocks.blockCosmeticSolid, 15)
            .addBlock(1, 1, 1, ConfigBlocks.blockCosmeticSolid, 11).addBlock(-1, 1, 1, ConfigBlocks.blockCosmeticSolid, 11).addBlock(-1, 1, -1, ConfigBlocks.blockCosmeticSolid, 11).addBlock(1, 1, -1, ConfigBlocks.blockCosmeticSolid, 11);

    public static final MultiblockHelper.MultiblockPattern completeNodeManipulatorMultiblock = new MultiblockHelper.MultiblockPattern(RegisteredBlocks.blockNodeManipulator, 5)
            .addBlock(0, 2, 0, RegisteredBlocks.blockNode, 0).addBlock(0, 1, 0, RegisteredBlocks.blockStoneMachine, 0)
            .addBlock(1, 0, 1, RegisteredBlocks.blockStoneMachine, 15).addBlock(-1, 0, 1, RegisteredBlocks.blockStoneMachine, 15).addBlock(-1, 0, -1, RegisteredBlocks.blockStoneMachine, 15).addBlock(1, 0, -1, RegisteredBlocks.blockStoneMachine, 15)
            .addBlock(1, 1, 1, RegisteredBlocks.blockStoneMachine, 11).addBlock(-1, 1, 1, RegisteredBlocks.blockStoneMachine, 11).addBlock(-1, 1, -1, RegisteredBlocks.blockStoneMachine, 11).addBlock(1, 1, -1, RegisteredBlocks.blockStoneMachine, 11);
}
