package makeo.gadomancy.common.blocks;

import makeo.gadomancy.common.blocks.tiles.TileAdditionalEldritchPortal;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockEldritchPortal;
import thaumcraft.common.config.ConfigBlocks;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 06.11.2015 23:23
 */
public class BlockAdditionalEldritchPortal extends BlockEldritchPortal {

    public BlockAdditionalEldritchPortal() {
        setCreativeTab(RegisteredItems.creativeTab);
    }

    @Override
    public String getUnlocalizedName() {
        return ConfigBlocks.blockEldritchPortal.getUnlocalizedName();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileAdditionalEldritchPortal();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {}
}
